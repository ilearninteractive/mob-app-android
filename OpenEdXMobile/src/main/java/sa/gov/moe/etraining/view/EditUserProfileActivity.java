package sa.gov.moe.etraining.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import sa.gov.moe.etraining.R;

import sa.gov.moe.etraining.base.BaseSingleFragmentActivity;
import sa.gov.moe.etraining.module.analytics.Analytics;

public class EditUserProfileActivity extends BaseSingleFragmentActivity {
    public static final String EXTRA_USERNAME = "username";

    public static Intent newIntent(@NonNull Context context, @NonNull String username) {
        return new Intent(context, EditUserProfileActivity.class)
                .putExtra(EXTRA_USERNAME, username);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.edit_user_profile_title));
        environment.getAnalyticsRegistry().trackScreenView(Analytics.Screens.PROFILE_EDIT);
    }

    @Override
    public Fragment getFirstFragment() {
        final Fragment fragment = new EditUserProfileFragment();
        fragment.setArguments(getIntent().getExtras());
        return fragment;
    }
}
