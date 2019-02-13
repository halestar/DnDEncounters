package net.kalinec.dndencounters.activities.custom_monsters;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.custom_monsters.CustomMonsterViewPagerAdapter;

public class ViewMonsters extends DnDEncountersActivity {

    private ViewPager mViewPager;
    private FloatingActionButton CustomMonsterAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_monsters);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.TabContainerFv);
        CustomMonsterViewPagerAdapter customMonsterViewPagerAdapter = new CustomMonsterViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(customMonsterViewPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        CustomMonsterAdd = findViewById(R.id.CustomMonsterAdd);
        CustomMonsterAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ViewMonsters.this, AddCustomMonster.class);
                startActivityForResult(myIntent, AddCustomMonster.REQUEST_NEW_MONSTER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mViewPager.getAdapter().notifyDataSetChanged();
    }
}
