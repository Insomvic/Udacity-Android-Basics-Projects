package com.insomvic.atourofclermont_ferrand;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context newContext;

    public CategoryAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        newContext = context;
    }

    // Initializes fragments based on position
    @Override
    public Fragment getItem(int position) {
        String pageTitle = String.valueOf(getPageTitle(position));
        Bundle bundle = new Bundle();
        bundle.putString("Category", pageTitle);
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    // Returns tab amount
    @Override
    public int getCount() {
        return 4;
    }

    // Returns name of tab based on position
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return newContext.getString(R.string.attractions);
        } else if (position == 1) {
            return newContext.getString(R.string.restaurants);
        } else if (position == 2) {
            return newContext.getString(R.string.parks);
        } else {
            return newContext.getString(R.string.events);
        }
    }

}