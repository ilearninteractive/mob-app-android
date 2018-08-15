package sa.gov.moe.etraining.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

import sa.gov.moe.etraining.base.BaseSingleFragmentActivity;
import sa.gov.moe.etraining.logger.Logger;
import sa.gov.moe.etraining.util.Config;

public class AccountActivity extends BaseSingleFragmentActivity {
    protected Logger logger = new Logger(getClass().getSimpleName());

    @Inject
    private Config config;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, AccountActivity.class);
    }

    @Override
    public Fragment getFirstFragment() {
        return new AccountFragment();
    }
}
