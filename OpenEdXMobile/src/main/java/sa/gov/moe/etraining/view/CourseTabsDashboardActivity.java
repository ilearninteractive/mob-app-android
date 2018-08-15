package sa.gov.moe.etraining.view;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import sa.gov.moe.etraining.event.CourseDashboardRefreshEvent;
import sa.gov.moe.etraining.model.api.EnrolledCoursesResponse;

import static sa.gov.moe.etraining.view.Router.EXTRA_ANNOUNCEMENTS;
import static sa.gov.moe.etraining.view.Router.EXTRA_COURSE_DATA;

public class CourseTabsDashboardActivity extends OfflineSupportBaseActivity {
    public static Intent newIntent(@NonNull Activity activity,
                                   @NonNull EnrolledCoursesResponse courseData,
                                   boolean announcements) {
        Intent intent = new Intent(activity, CourseTabsDashboardActivity.class);
        intent.putExtra(EXTRA_COURSE_DATA, courseData);
        intent.putExtra(EXTRA_ANNOUNCEMENTS, announcements);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    @Override
    public Fragment getFirstFragment() {
        return CourseTabsDashboardFragment.newInstance(
                (EnrolledCoursesResponse) getIntent().getExtras().getSerializable(EXTRA_COURSE_DATA));
    }

    @Override
    public Object getRefreshEvent() {
        return new CourseDashboardRefreshEvent();
    }
}
