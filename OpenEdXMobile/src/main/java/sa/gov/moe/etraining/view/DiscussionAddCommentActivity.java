package sa.gov.moe.etraining.view;

import android.support.v4.app.Fragment;

import com.google.inject.Inject;

import sa.gov.moe.etraining.base.BaseSingleFragmentActivity;

public class DiscussionAddCommentActivity extends BaseSingleFragmentActivity {
    @Inject
    DiscussionAddCommentFragment discussionAddCommentFragment;

    @Override
    public Fragment getFirstFragment() {
        discussionAddCommentFragment.setArguments(getIntent().getExtras());
        return discussionAddCommentFragment;
    }
}
