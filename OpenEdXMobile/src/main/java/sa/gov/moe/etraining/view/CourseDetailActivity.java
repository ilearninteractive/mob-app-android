/*
 * CourseDetailActivity
 *
 * Activity that holds the fragments related to the course detail.
 */

package sa.gov.moe.etraining.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import sa.gov.moe.etraining.base.BaseSingleFragmentActivity;
import sa.gov.moe.etraining.course.CourseDetail;
import sa.gov.moe.etraining.module.analytics.Analytics;

public class CourseDetailActivity extends BaseSingleFragmentActivity {

    public static Intent newIntent(@NonNull Context context, @NonNull CourseDetail courseDetail) {
        return new Intent(context, CourseDetailActivity.class)
                .putExtra(CourseDetailFragment.COURSE_DETAIL, courseDetail);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        environment.getAnalyticsRegistry().trackScreenView(Analytics.Screens.COURSE_INFO_SCREEN);
    }

    @Override
    public Fragment getFirstFragment() {
        return new CourseDetailFragment();
    }
}
