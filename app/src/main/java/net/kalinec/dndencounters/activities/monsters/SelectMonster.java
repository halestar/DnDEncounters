package net.kalinec.dndencounters.activities.monsters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.custom_monsters.AddCustomMonster;
import net.kalinec.dndencounters.activities.custom_monsters.ViewMonsters;
import net.kalinec.dndencounters.custom_monsters.CustomMonster;
import net.kalinec.dndencounters.custom_monsters.CustomMonsterViewPagerAdapter;
import net.kalinec.dndencounters.custom_monsters.CustomMonsters;
import net.kalinec.dndencounters.lib.OnMonsterSelectedListener;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.monsters.AllMonsterViewPagerAdapter;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.monsters.MonsterListAdapter;
import net.kalinec.dndencounters.monsters.Monsters;

import java.util.ArrayList;
import java.util.List;

public class SelectMonster extends DnDEncountersActivity implements OnMonsterSelectedListener
{
	public static final int REQUEST_SELECT_MONSTER = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_monster);

		// Set up the ViewPager with the sections adapter.
		ViewPager mViewPager = findViewById(R.id.TabContainerFv);
		AllMonsterViewPagerAdapter customMonsterViewPagerAdapter = new AllMonsterViewPagerAdapter(getSupportFragmentManager(), this);
		mViewPager.setAdapter(customMonsterViewPagerAdapter);

		TabLayout tabLayout = findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(mViewPager);

		FloatingActionButton CustomMonsterAdd = findViewById(R.id.CustomMonsterAdd);
		CustomMonsterAdd.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent myIntent = new Intent(SelectMonster.this, AddCustomMonster.class);
				startActivityForResult(myIntent, AddCustomMonster.REQUEST_NEW_MONSTER);
			}
		});
	}

	@Override
	public void onMonsterSelected(Monster monster)
	{
		Intent data = new Intent();
		data.putExtra(Monster.PASSED_MONSTER, monster);
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		if(requestCode == AddCustomMonster.REQUEST_NEW_MONSTER && resultCode == RESULT_OK)
		{
			ViewPager mViewPager = findViewById(R.id.TabContainerFv);
			mViewPager.getAdapter().notifyDataSetChanged();
		}
	}
}
