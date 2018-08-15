package sa.gov.moe.etraining.profiles;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.Injector;

import sa.gov.moe.etraining.R;
import sa.gov.moe.etraining.databinding.FragmentUserProfileAccomplishmentsBinding;

import java.util.HashMap;
import java.util.Map;

import roboguice.RoboGuice;
import sa.gov.moe.etraining.core.EdxEnvironment;
import sa.gov.moe.etraining.module.prefs.UserPrefs;
import sa.gov.moe.etraining.user.UserService;
import sa.gov.moe.etraining.util.ResourceUtil;
import sa.gov.moe.etraining.util.images.ShareUtils;
import sa.gov.moe.etraining.view.PresenterFragment;
import sa.gov.moe.etraining.view.adapters.InfiniteScrollUtils;

public class UserProfileAccomplishmentsFragment extends PresenterFragment<UserProfileAccomplishmentsPresenter, UserProfileAccomplishmentsPresenter.ViewInterface> implements ScrollingPreferenceChild {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile_accomplishments, container, false).getRoot();
    }

    @NonNull
    @Override
    protected UserProfileAccomplishmentsPresenter createPresenter() {
        final Injector injector = RoboGuice.getInjector(getActivity());
        return new UserProfileAccomplishmentsPresenter(
                injector.getInstance(UserService.class),
                injector.getInstance(UserPrefs.class),
                ((UserProfileBioTabParent) getParentFragment()).getBioInteractor().getUsername());
    }

    @NonNull
    @Override
    protected UserProfileAccomplishmentsPresenter.ViewInterface createView() {
        final FragmentUserProfileAccomplishmentsBinding binding = DataBindingUtil.getBinding(getView());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.list.setLayoutManager(linearLayoutManager);
        binding.list.addOnScrollListener(new InfiniteScrollUtils.RecyclerViewOnScrollListener(linearLayoutManager, new Runnable() {
            @Override
            public void run() {
                presenter.onScrolledToEnd();
            }
        }));
        final AccomplishmentListAdapter adapter = createAdapter(
                new AccomplishmentListAdapter.Listener() {
                    @Override
                    public void onShare(@NonNull BadgeAssertion badgeAssertion) {
                        presenter.onClickShare(badgeAssertion);
                    }
                });
        binding.list.setAdapter(adapter);
        return new UserProfileAccomplishmentsPresenter.ViewInterface() {
            @Override
            public void setModel(@NonNull UserProfileAccomplishmentsPresenter.ViewModel model) {
                adapter.setItems(model.badges);
                adapter.setPageLoading(model.pageLoading);
                adapter.setSharingEnabled(model.enableSharing);
            }

            @Override
            public void startBadgeShareIntent(@NonNull String badgeUrl) {
                final Map<String, CharSequence> shareTextParams = new HashMap<>();
                shareTextParams.put("platform_name", getString(R.string.platform_name));
                shareTextParams.put("badge_url", badgeUrl);
                final String shareText = ResourceUtil.getFormattedString(getResources(), R.string.share_accomplishment_message, shareTextParams).toString();
                startActivity(ShareUtils.newShareIntent(shareText));
            }
        };
    }

    @NonNull
    @VisibleForTesting
    protected AccomplishmentListAdapter createAdapter(@NonNull AccomplishmentListAdapter.Listener listener) {
        return new AccomplishmentListAdapter(
                RoboGuice.getInjector(getContext()).getInstance(EdxEnvironment.class).getConfig().getApiHostURL(),
                listener);
    }

    @Override
    public boolean prefersScrollingHeader() {
        return true;
    }
}
