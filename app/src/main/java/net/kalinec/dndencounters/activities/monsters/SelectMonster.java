package net.kalinec.dndencounters.activities.monsters;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.custom_monsters.CustomMonsterViewPagerAdapter;
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
	private ViewPager mViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_monster);

		// Set up the ViewPager with the sections adapter.
		mViewPager = findViewById(R.id.TabContainerFv);
		AllMonsterViewPagerAdapter customMonsterViewPagerAdapter = new AllMonsterViewPagerAdapter(getSupportFragmentManager(), this);
		mViewPager.setAdapter(customMonsterViewPagerAdapter);

		TabLayout tabLayout = findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(mViewPager);
	}

	@Override
	public void onMonsterSelected(Monster monster)
	{
		Intent data = new Intent();
		data.putExtra(Monster.PASSED_MONSTER, monster);
		setResult(RESULT_OK, data);
		finish();
	}
}
