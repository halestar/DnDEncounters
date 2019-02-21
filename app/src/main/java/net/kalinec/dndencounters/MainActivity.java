package net.kalinec.dndencounters;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.kalinec.dndencounters.activities.adventure_encounters.PlayAdventure;
import net.kalinec.dndencounters.activities.custom_monsters.ViewMonsters;
import net.kalinec.dndencounters.activities.encounters.ViewEncounters;
import net.kalinec.dndencounters.activities.modules.ListModules;
import net.kalinec.dndencounters.activities.monster_tokens.ViewMonsterTokens;
import net.kalinec.dndencounters.activities.parties.CreateParty;
import net.kalinec.dndencounters.activities.players.ListPlayers;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.monster_tokens.MonsterToken;
import net.kalinec.dndencounters.monster_tokens.MonsterTokens;
import net.kalinec.dndencounters.parties.Party;
import net.kalinec.dndencounters.playsessions.PlaySession;
import net.kalinec.dndencounters.playsessions.PlaySessionManager;
import net.kalinec.dndencounters.playsessions.PlaySessionsListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends DnDEncountersActivity
		implements NavigationView.OnNavigationItemSelectedListener
{
	private PlaySession activeSession = null;
	private PlaySessionsListAdapter playSessionsListAdapter;
	private Button continueSessionBtn, beingAdventureBtn;
	private TextView partyNameTv, activeSessionNumPartyTv, activeSessionAplTv, MonsterTokenWarning;
	private Group activeSessionGroup, activeAdventruesGroup;
	private RecyclerView activeAdventuresRv;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();
		
		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);


		continueSessionBtn = findViewById(R.id.continueSessionBtn);
		partyNameTv = findViewById(R.id.partyNameTv);
		activeSessionNumPartyTv = findViewById(R.id.activeSessionNumPartyTv);
		activeSessionAplTv = findViewById(R.id.activeSessionAplTv);
		activeSessionGroup = findViewById(R.id.activeSessionGroup);
		activeAdventruesGroup = findViewById(R.id.activeAdventruesGroup);
		activeAdventuresRv = findViewById(R.id.ActiveAdventuresRv);
		MonsterTokenWarning = findViewById(R.id.MonsterTokenWarning);
		beingAdventureBtn = findViewById(R.id.beingAdventureBtn);

		activeAdventuresRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		playSessionsListAdapter =
				new PlaySessionsListAdapter(getApplicationContext(), new RvClickListener()
				{
					@Override
					public void onClick(View view, int position)
					{
						PlaySession wantToPlay = playSessionsListAdapter.get(position);
						if (activeSession != null)
							PlaySessionManager.saveCurrentSession(getApplicationContext());
						PlaySessionManager.removeFromActiveSessions(getApplicationContext(),
						                                            wantToPlay);
						activeSession = wantToPlay;
						activeSession.saveSession(getApplicationContext());
						continueAdventure();
					}
				});
	}
	
	private void updateFront()
	{
		activeSession = PlaySessionManager.getCurrentSession(getApplicationContext());

		List<MonsterToken> monsterTokens = MonsterTokens.getAllMonsterTokens(getApplicationContext());

		if(monsterTokens.size() == 0)
		{
			MonsterTokenWarning.setVisibility(View.VISIBLE);
			activeSessionGroup.setVisibility(View.GONE);
			activeAdventruesGroup.setVisibility(View.GONE);
			beingAdventureBtn.setVisibility(View.GONE);
		}
		else
		{
			beingAdventureBtn.setVisibility(View.VISIBLE);
			MonsterTokenWarning.setVisibility(View.GONE);
			if (activeSession == null)
			{
				activeSessionGroup.setVisibility(View.GONE);
			}
			else
			{
				activeSessionGroup.setVisibility(View.VISIBLE);
				partyNameTv.setText(activeSession.getPlayers().getName());
				activeSessionNumPartyTv
						.setText(String.format(Locale.getDefault(), "%d", activeSession
								.getPlayers().getMembers().size()));
				activeSessionAplTv.setText(String.format(Locale.getDefault(), "%d", activeSession
						.getPlayers().getApl()));
				continueSessionBtn.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						continueAdventure();
					}
				});
			}

			//any saved adventures?
			ArrayList<PlaySession> activeAdventures = PlaySessionManager
					.getActiveSessions(getApplicationContext());
			if (activeAdventures == null || activeAdventures.size() == 0)
			{
				//no adventures to show, so hide all
				activeAdventruesGroup.setVisibility(View.GONE);
			}
			else
			{
				activeAdventruesGroup.setVisibility(View.VISIBLE);
				playSessionsListAdapter.setSessionList(activeAdventures);
				activeAdventuresRv.setAdapter(playSessionsListAdapter);
			}
		}
	}
	
	@Override
	public void onBackPressed()
	{
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
		}
		else
		{
			super.onBackPressed();
		}
		updateFront();
	}
	
	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item)
	{
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		
		if (id == R.id.nav_players)
		{
			Intent myIntent = new Intent(MainActivity.this, ListPlayers.class);
			MainActivity.this.startActivity(myIntent);
		}
		else if (id == R.id.nav_modules)
		{
			Intent myIntent = new Intent(MainActivity.this, ListModules.class);
			MainActivity.this.startActivity(myIntent);
		}
		else if (id == R.id.nav_encounters)
		{
			Intent myIntent = new Intent(MainActivity.this, ViewEncounters.class);
			MainActivity.this.startActivity(myIntent);
		}
		else if(id == R.id.nav_monster_tokens)
		{
			Intent myIntent = new Intent(MainActivity.this, ViewMonsterTokens.class);
			MainActivity.this.startActivity(myIntent);
		}
		else if(id == R.id.nav_project_link)
		{
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/halestar/DnDEncounters"));
			startActivity(browserIntent);
		}
		else if(id == R.id.nav_all_monsters)
		{
			Intent myIntent = new Intent(MainActivity.this, ViewMonsters.class);
			MainActivity.this.startActivity(myIntent);
		}
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	public void beginPlaySession(View v)
	{
		if(activeSession != null)
			PlaySessionManager.saveCurrentSession(getApplicationContext());

		activeSession = new PlaySession();
		Intent myIntent = new Intent(MainActivity.this, CreateParty.class);
		myIntent.putExtra(PlaySession.PASSED_SESSION, activeSession);
		startActivityForResult(myIntent, CreateParty.REQUEST_NEW_PARTY);
	}

	public void continueAdventure()
	{
		Intent myIntent = new Intent(MainActivity.this, PlayAdventure.class);
		myIntent.putExtra(PlaySession.PASSED_SESSION, activeSession);
		startActivityForResult(myIntent, PlayAdventure.PLAY_ADVENTURE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == CreateParty.REQUEST_NEW_PARTY)
		{
			if(resultCode == RESULT_OK)
			{
				assert data != null;
				Party newParty = (Party)data.getSerializableExtra(Party.PASSED_PARTY);
				activeSession.setPlayers(newParty);
				//go to play.
				continueAdventure();
			}
		}
	}

	@Override
	public void onResume()
	{  // After a pause OR at startup
		super.onResume();
		//Refresh your stuff here
		updateFront();
	}
}
