package net.kalinec.dndencounters.activities.custom_monsters;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.players.Players;
import net.kalinec.dndencounters.activities.players.ViewPlayer;
import net.kalinec.dndencounters.custom_monsters.CustomMonsterViewPagerAdapter;
import net.kalinec.dndencounters.players.Player;

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
