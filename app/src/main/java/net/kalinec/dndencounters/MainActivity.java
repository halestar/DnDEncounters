package net.kalinec.dndencounters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import net.kalinec.dndencounters.activities.adventure_encounters.PlayAdventure;
import net.kalinec.dndencounters.activities.encounters.ViewEncounters;
import net.kalinec.dndencounters.activities.monster_tokens.ViewMonsterTokens;
import net.kalinec.dndencounters.activities.parties.CreateParty;
import net.kalinec.dndencounters.activities.players.Players;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.parties.Party;
import net.kalinec.dndencounters.playsessions.PlaySession;
import net.kalinec.dndencounters.playsessions.PlaySessionManager;
import net.kalinec.dndencounters.playsessions.PlaySessionsListAdapter;

import java.util.ArrayList;

public class MainActivity extends DnDEncountersActivity
		implements NavigationView.OnNavigationItemSelectedListener
{
	private PlaySession activeSession = null;
	private Button continueSessionBtn;
	private ConstraintLayout activeSessionView;
	private TextView partyNameTv, activeSessionNumPartyTv, activeSessionAplTv;
	private Group activeSessionGroup, activeAdventruesGroup;
	private RecyclerView ActiveAdventuresRv;
	private ArrayList<PlaySession> activeAdventures;
	private PlaySessionsListAdapter playSessionsListAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();
		
		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		activeSession = PlaySessionManager.getCurrentSession(getApplicationContext());

		activeSessionView = findViewById(R.id.activeSessionView);
		continueSessionBtn = findViewById(R.id.continueSessionBtn);
		partyNameTv = findViewById(R.id.partyNameTv);
		activeSessionNumPartyTv = findViewById(R.id.activeSessionNumPartyTv);
		activeSessionAplTv = findViewById(R.id.activeSessionAplTv);
		activeSessionGroup = findViewById(R.id.activeSessionGroup);
		activeAdventruesGroup = findViewById(R.id.activeAdventruesGroup);
		ActiveAdventuresRv = findViewById(R.id.ActiveAdventuresRv);

		if(activeSession == null) {
			activeSessionGroup.setVisibility(View.GONE);
		}
		else {
			activeSessionGroup.setVisibility(View.VISIBLE);
			partyNameTv.setText(activeSession.getPlayers().getName());
			activeSessionNumPartyTv.setText(Integer.toString(activeSession.getPlayers().getMembers().size()));
			activeSessionAplTv.setText(Integer.toString(activeSession.getPlayers().getApl()));
			continueSessionBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					continueAdventure();
				}
			});
		}

		//any saved adventures?
		activeAdventures = PlaySessionManager.getActiveSessions(getApplicationContext());
		if(activeAdventures == null || activeAdventures.size() == 0)
		{
			//no adventures to show, so hide all
			activeAdventruesGroup.setVisibility(View.GONE);
		}
		else
		{
			activeAdventruesGroup.setVisibility(View.VISIBLE);
			playSessionsListAdapter = new PlaySessionsListAdapter(getApplicationContext(), new RvClickListener() {
				@Override
				public void onClick(View view, int position) {
					PlaySession wantToPlay = playSessionsListAdapter.get(position);
					if(activeSession != null)
						PlaySessionManager.saveCurrentSession(getApplicationContext());
					PlaySessionManager.removeFromActiveSessions(getApplicationContext(), wantToPlay);
					activeSession = wantToPlay;
					continueAdventure();
				}
			});
			playSessionsListAdapter.setSessionList(activeAdventures);
			ActiveAdventuresRv.setAdapter(playSessionsListAdapter);
			ActiveAdventuresRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		}
	}
	
	@Override
	public void onBackPressed()
	{
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
		}
		else
		{
			super.onBackPressed();
		}
	}
	
	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item)
	{
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		
		if (id == R.id.nav_players)
		{
			Intent myIntent = new Intent(MainActivity.this, Players.class);
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
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
		MainActivity.this.startActivity(myIntent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		if(requestCode == CreateParty.REQUEST_NEW_PARTY)
		{
			if(resultCode == RESULT_OK)
			{
				Party newParty = (Party)data.getSerializableExtra(Party.PASSED_PARTY);
				activeSession.setPlayers(newParty);
				//go to play.
				continueAdventure();
			}
		}
	}
}
