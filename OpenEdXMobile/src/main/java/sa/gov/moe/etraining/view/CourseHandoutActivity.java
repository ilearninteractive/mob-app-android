package sa.gov.moe.etraining.view;

import android.support.v4.app.Fragment;

import sa.gov.moe.etraining.R;

import sa.gov.moe.etraining.base.BaseSingleFragmentActivity;

public class CourseHandoutActivity extends BaseSingleFragmentActivity {
    private Fragment fragment;

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(getString(R.string.tab_label_handouts));
    }

    @Override
    public Fragment getFirstFragment() {
        return new CourseHandoutFragment();
    }
}
