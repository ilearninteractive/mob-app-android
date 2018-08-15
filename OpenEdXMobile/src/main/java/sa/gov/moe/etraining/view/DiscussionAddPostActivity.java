package sa.gov.moe.etraining.view;

import android.support.v4.app.Fragment;

import com.google.inject.Inject;

import sa.gov.moe.etraining.base.BaseSingleFragmentActivity;

public class DiscussionAddPostActivity extends BaseSingleFragmentActivity {
    @Inject
    DiscussionAddPostFragment discussionAddPostFragment;

    @Override
    public Fragment getFirstFragment() {
        discussionAddPostFragment.setArguments(getIntent().getExtras());
        return discussionAddPostFragment;
    }
}
