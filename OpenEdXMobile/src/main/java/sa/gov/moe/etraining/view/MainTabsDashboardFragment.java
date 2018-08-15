package sa.gov.moe.etraining.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import sa.gov.moe.etraining.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import sa.gov.moe.etraining.event.AccountDataLoadedEvent;
import sa.gov.moe.etraining.event.ProfilePhotoUpdatedEvent;
import sa.gov.moe.etraining.model.FragmentItemModel;
import sa.gov.moe.etraining.model.api.ProfileModel;
import sa.gov.moe.etraining.module.analytics.Analytics;
import sa.gov.moe.etraining.module.prefs.LoginPrefs;
import sa.gov.moe.etraining.user.Account;
import sa.gov.moe.etraining.user.ProfileImage;
import sa.gov.moe.etraining.user.UserAPI;
import sa.gov.moe.etraining.user.UserService;
import sa.gov.moe.etraining.util.UserProfileUtils;
import sa.gov.moe.etraining.view.dialog.NativeFindCoursesFragment;

public class MainTabsDashboardFragment extends TabsBaseFragment {

    private ProfileModel profile;

    private MainDashboardToolbarCallbacks toolbarCallbacks;

    @Nullable
    private Call<Account> getAccountCall;

    @Inject
    private LoginPrefs loginPrefs;

    @Inject
    private UserService userService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final boolean isUserProfileEnabled = environment.getConfig().isUserProfilesEnabled();
        if (isUserProfileEnabled) {
            profile = loginPrefs.getCurrentUserProfile();
            sendGetUpdatedAccountCall();
        }
        if (!isUserProfileEnabled) {
            toolbarCallbacks.getProfileView().setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.my_courses, menu);
        menu.findItem(R.id.menu_item_account).setVisible(true);
        menu.findItem(R.id.menu_item_account).setIcon(
                new IconDrawable(getContext(), FontAwesomeIcons.fa_gear)
                        .colorRes(getContext(), R.color.toolbar_controls_color)
                        .actionBarSize(getContext()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_account: {
                environment.getRouter().showAccountActivity(getActivity());
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        toolbarCallbacks = (MainDashboardToolbarCallbacks) getActivity();
    }

    public void sendGetUpdatedAccountCall() {
        getAccountCall = userService.getAccount(profile.username);
        getAccountCall.enqueue(new UserAPI.AccountDataUpdatedCallback(
                getActivity(),
                profile.username,
                null, // Disable global loading indicator
                null)); // No place to show an error notification
    }

    @Override
    protected boolean showTitleInTabs() {
        return true;
    }

    @Override
    public List<FragmentItemModel> getFragmentItems() {
        ArrayList<FragmentItemModel> items = new ArrayList<>();

        items.add(new FragmentItemModel(MyCoursesListFragment.class,
                getResources().getString(R.string.label_my_courses), FontAwesomeIcons.fa_list_alt,
                new FragmentItemModel.FragmentStateListener() {
                    @Override
                    public void onFragmentSelected() {
                        environment.getAnalyticsRegistry().trackScreenView(Analytics.Screens.MY_COURSES);
                    }
                }));

        if (environment.getConfig().getCourseDiscoveryConfig().isCourseDiscoveryEnabled()) {
            items.add(new FragmentItemModel(
                    environment.getConfig().getCourseDiscoveryConfig().isWebviewCourseDiscoveryEnabled()
                            ? WebViewDiscoverCoursesFragment.class : NativeFindCoursesFragment.class,
                    getResources().getString(R.string.label_discovery), FontAwesomeIcons.fa_search,
                    new FragmentItemModel.FragmentStateListener() {
                        @Override
                        public void onFragmentSelected() {
                            environment.getAnalyticsRegistry().trackScreenView(Analytics.Screens.FIND_COURSES);
                        }
                    }));
        }

        return items;
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(@NonNull ProfilePhotoUpdatedEvent event) {
        if (!environment.getConfig().isUserProfilesEnabled()) {
            return;
        }
        final ImageView profileImage = toolbarCallbacks.getProfileView();
        if (event.getUsername().equalsIgnoreCase(profile.username)) {
            UserProfileUtils.loadProfileImage(getContext(), event, profileImage);
        }
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(@NonNull AccountDataLoadedEvent event) {
        if (!environment.getConfig().isUserProfilesEnabled()) {
            return;
        }
        final Account account = event.getAccount();
        if (account.getUsername().equalsIgnoreCase(profile.username)) {
            final ImageView profileImage = toolbarCallbacks.getProfileView();
            if (profileImage != null) {
                loadProfileImage(account.getProfileImage(), profileImage);
            }
        }
    }

    private void loadProfileImage(@NonNull ProfileImage profileImage, @NonNull ImageView imageView) {
        if (profileImage.hasImage()) {
            Glide.with(this)
                    .load(profileImage.getImageUrlMedium())
                    .into(imageView);
        } else {
            Glide.with(this)
                    .load(R.drawable.profile_photo_placeholder)
                    .into(imageView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != getAccountCall) {
            getAccountCall.cancel();
        }
        EventBus.getDefault().unregister(this);
    }
}
