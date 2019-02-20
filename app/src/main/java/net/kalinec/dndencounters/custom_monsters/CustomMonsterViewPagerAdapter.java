package net.kalinec.dndencounters.custom_monsters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.kalinec.dndencounters.activities.custom_monsters.ViewCustomMonstersFragment;
import net.kalinec.dndencounters.activities.custom_monsters.ViewMonstersFragment;
import net.kalinec.dndencounters.lib.OnMonsterSelectedListener;

public class CustomMonsterViewPagerAdapter extends FragmentPagerAdapter
{

    public CustomMonsterViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i)
    {
        if(i == 0)
            return ViewMonstersFragment.newInstance();
        else
            return ViewCustomMonstersFragment.newInstance();
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0)
            return "D&D Monsters";
        else
            return "Custom Monsters";
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
