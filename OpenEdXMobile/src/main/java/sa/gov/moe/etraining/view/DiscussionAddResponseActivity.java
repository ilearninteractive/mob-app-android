package sa.gov.moe.etraining.view;

import android.support.v4.app.Fragment;

import com.google.inject.Inject;

import sa.gov.moe.etraining.base.BaseSingleFragmentActivity;

public class DiscussionAddResponseActivity extends BaseSingleFragmentActivity {
    @Inject
    DiscussionAddResponseFragment discussionAddResponseFragment;

    @Override
    public Fragment getFirstFragment() {
        discussionAddResponseFragment.setArguments(getIntent().getExtras());
        return discussionAddResponseFragment;
    }
}
