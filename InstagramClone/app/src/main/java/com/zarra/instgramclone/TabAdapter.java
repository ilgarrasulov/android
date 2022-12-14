package com.zarra.instgramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int tabPosition) {

        switch (tabPosition){
            case 0:
                return new ProfileTab();
            case 1:
                return new UsersTab();
            case 2:
                return new SharePictureTab();
            case 3:
                return new MyPosts();
            default:
                return null;

        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Profile";
            case 1:
                return "Users";
            case 2:
                return "Share Picture";
            case 3:
                return "My Posts";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
