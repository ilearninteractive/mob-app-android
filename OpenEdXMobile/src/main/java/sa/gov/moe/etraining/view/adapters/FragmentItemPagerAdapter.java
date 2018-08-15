package sa.gov.moe.etraining.view.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import sa.gov.moe.etraining.model.FragmentItemModel;

public class FragmentItemPagerAdapter extends FragmentStatePagerAdapter {
    @NonNull
    private List<FragmentItemModel> fragmentItems;

    public FragmentItemPagerAdapter(@NonNull FragmentManager fm,
                                    @NonNull List<FragmentItemModel> fragmentItems) {
        super(fm);
        this.fragmentItems = fragmentItems;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentItems.get(position).generateFragment();
    }

    @Override
    public int getCount() {
        return fragmentItems.size();
    }
}
