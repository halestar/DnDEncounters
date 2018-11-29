package net.kalinec.dndencounters.activities.players;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.db.AppDatabase;
import net.kalinec.dndencounters.db.PlayerDao;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.players.Player;
import net.kalinec.dndencounters.players.PlayerListAdapter;

import java.util.List;

public class Players extends DnDEncountersActivity
{
	private RecyclerView recyclerview_players;
	private LiveData<List<Player>> playerList;
	private PlayerListAdapter playerListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_players);
		PlayerDao playerDao = AppDatabase.getDatabase(getApplicationContext()).playerDao();
		playerList = playerDao.getAllPlayers();

		playerListAdapter = new PlayerListAdapter(getApplicationContext(), new RvClickListener() {
			@Override
			public void onClick(View view, int position) {
				Player p = playerListAdapter.get(position);
				viewPlayer(p);
			}
		});
		playerList.observe(this, new Observer<List<Player>>() {
			@Override
			public void onChanged(@Nullable List<Player> players) {
				playerListAdapter.setPlayerList(players);
			}
		});

		recyclerview_players = findViewById(R.id.recyclerview_players);
		recyclerview_players.setAdapter(playerListAdapter);
		recyclerview_players.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_players, menu);
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
	
	public void newPlayer(View target)
	{
		Intent myIntent = new Intent(Players.this, AddPlayer.class);
		Players.this.startActivity(myIntent);
	}

	public void viewPlayer(Player p)
	{
		Intent myIntent = new Intent(Players.this, ViewPlayer.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Player.PASSED_PLAYER, p);
		myIntent.putExtras(bundle);
		Players.this.startActivity(myIntent);
	}
}
