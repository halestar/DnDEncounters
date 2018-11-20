package net.kalinec.dndencounters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import net.kalinec.dndencounters.parties.Party;
import net.kalinec.dndencounters.playsessions.PlaySession;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener
{
	private PlaySession activeSession = null;
	private Button continueSessionBtn;
	private ConstraintLayout activeSessionView;
	private TextView partyNameTv, activeSessionNumPartyTv, activeSessionAplTv;


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
		
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		activeSession = PlaySession.existingSession(getApplicationContext());

		activeSessionView = (ConstraintLayout) findViewById(R.id.activeSessionView);
		continueSessionBtn = (Button) findViewById(R.id.continueSessionBtn);
		partyNameTv = (TextView) findViewById(R.id.partyNameTv);
		activeSessionNumPartyTv = (TextView) findViewById(R.id.activeSessionNumPartyTv);
		activeSessionAplTv = (TextView) findViewById(R.id.activeSessionAplTv);

		if(activeSession == null) {
			continueSessionBtn.setVisibility(View.GONE);
			activeSessionView.setVisibility(View.GONE);
		}
		else {
			continueSessionBtn.setVisibility(View.VISIBLE);
			activeSessionView.setVisibility(View.VISIBLE);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings)
		{
			return true;
		}
		
		return super.onOptionsItemSelected(item);
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
		if(activeSession == null)
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
				Intent myIntent = new Intent(MainActivity.this, PlayAdventure.class);
				myIntent.putExtra(PlaySession.PASSED_SESSION, activeSession);
				MainActivity.this.startActivity(myIntent);
			}
		}
	}
}
