package net.kalinec.dndencounters.activities.players;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
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
		List<Player> playerList = net.kalinec.dndencounters.players.Players.getAllPlayers(getApplicationContext());

		playerListAdapter = new PlayerListAdapter(getApplicationContext(), new RvClickListener() {
			@Override
			public void onClick(View view, int position) {
				Player p = playerListAdapter.get(position);
				viewPlayer(p);
			}
		});
		playerListAdapter.setPlayerList(playerList);
		
		RecyclerView recyclerview_players = findViewById(R.id.recyclerview_players);
		recyclerview_players.setAdapter(playerListAdapter);
		recyclerview_players.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
	}
	
	public void newPlayer(View target)
	{
		Intent myIntent = new Intent(Players.this, AddPlayer.class);
		startActivityForResult(myIntent, AddPlayer.REQUEST_NEW_PLAYER_CAPTURE);
	}

	public void viewPlayer(Player p)
	{
		Intent myIntent = new Intent(Players.this, ViewPlayer.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Player.PASSED_PLAYER, p);
		myIntent.putExtras(bundle);
		startActivityForResult(myIntent, ViewPlayer.REQUEST_VIEW_PLAYER);
	}
	
	@Override
	protected void onActivityResult(
			int requestCode, int resultCode, @Nullable Intent data
	                               )
	{
		super.onActivityResult(requestCode, resultCode, data);
		List<Player> playerList = net.kalinec.dndencounters.players.Players.getAllPlayers(getApplicationContext());
		playerListAdapter.setPlayerList(playerList);
	}
}
