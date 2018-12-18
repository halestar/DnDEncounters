package net.kalinec.dndencounters.activities.players;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
	private PlayerListAdapter playerListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_players);
		PlayerDao playerDao = AppDatabase.getDatabase(getApplicationContext()).playerDao();
		LiveData<List<Player>> playerList = playerDao.getAllPlayers();

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
		
		RecyclerView recyclerview_players = findViewById(R.id.recyclerview_players);
		recyclerview_players.setAdapter(playerListAdapter);
		recyclerview_players.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
