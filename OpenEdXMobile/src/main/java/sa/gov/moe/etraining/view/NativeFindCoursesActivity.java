package sa.gov.moe.etraining.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import sa.gov.moe.etraining.base.BaseSingleFragmentActivity;
import sa.gov.moe.etraining.module.analytics.Analytics;
import sa.gov.moe.etraining.view.dialog.NativeFindCoursesFragment;

public class NativeFindCoursesActivity extends BaseSingleFragmentActivity {

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, NativeFindCoursesActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        environment.getAnalyticsRegistry().trackScreenView(Analytics.Screens.FIND_COURSES);
    }

    @Override
    public Fragment getFirstFragment() {
        return new NativeFindCoursesFragment();
    }
}
