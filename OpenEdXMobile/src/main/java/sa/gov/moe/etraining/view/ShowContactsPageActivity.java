package sa.gov.moe.etraining.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import sa.gov.moe.etraining.R;

import sa.gov.moe.etraining.base.BaseSingleFragmentActivity;

public class ShowContactsPageActivity extends BaseSingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.contacts_txt));
    }

    @Override
    public Fragment getFirstFragment() {
        return new ShowContactsPageActivityFragment();
    }

}
