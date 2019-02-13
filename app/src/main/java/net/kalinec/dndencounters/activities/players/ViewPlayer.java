package net.kalinec.dndencounters.activities.players;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.characters.AddCharacter;
import net.kalinec.dndencounters.activities.characters.EditCharacter;
import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.characters.CharacterListAdapter;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.players.Player;
import net.kalinec.dndencounters.players.Players;

import java.util.ArrayList;
import java.util.List;

public class ViewPlayer extends DnDEncountersActivity
{
	protected Player selectedPlayer;
	protected TextView playerNameTv;
	private CharacterListAdapter characterListAdapter;
	public static int REQUEST_VIEW_PLAYER = 349;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_player);
		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		selectedPlayer = (Player)bundle.getSerializable(Player.PASSED_PLAYER);
		
		playerNameTv = findViewById(R.id.playerName);
		playerNameTv.setText(selectedPlayer.getName());

		ArrayList<Character > pcList = selectedPlayer.getPcs();

		characterListAdapter = new CharacterListAdapter(getApplicationContext(), new RvClickListener() {
			@Override
			public void onClick(View view, int position) {
				Character pc = characterListAdapter.get(position);
				editPc(pc);
			}
		});
		characterListAdapter.setCharacterList(pcList);
		
		
		RecyclerView recyclerview_characters = findViewById(R.id.recyclerview_characters);
		recyclerview_characters.setAdapter(characterListAdapter);
		recyclerview_characters.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		List<Player> players = Players.getAllPlayers(getApplicationContext());
		selectedPlayer = players.get(players.indexOf(selectedPlayer));
		ArrayList<Character> pcList = selectedPlayer.getPcs();
		characterListAdapter.setCharacterList(pcList);
		playerNameTv.setText(selectedPlayer.getName());
	}
	
	public void newPC(View target)
	{
		Intent myIntent = new Intent(ViewPlayer.this, AddCharacter.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Player.PASSED_PLAYER, selectedPlayer);
		myIntent.putExtras(bundle);
		startActivityForResult(myIntent, AddCharacter.REQUEST_NEW_CHARACTER);
	}

	public void editPc(Character pc)
	{
		Intent myIntent = new Intent(ViewPlayer.this, EditCharacter.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Character.PASSED_CHARACTER, pc);
		myIntent.putExtras(bundle);
		startActivityForResult(myIntent, EditCharacter.REQUEST_UPDATED_CHARACTER);
	}
	
	public void editPlayer(View target)
	{
		Intent myIntent = new Intent(ViewPlayer.this, EditPlayer.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Player.PASSED_PLAYER, selectedPlayer);
		myIntent.putExtras(bundle);
		startActivityForResult(myIntent, EditPlayer.REQUEST_UPDATED_PLAYER);
	}
	
	public void confirmDeletePlayer(View target)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(ViewPlayer.this);
		builder.setMessage(R.string.ConfirmPlayerDelete)
				.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						deletePlayer();
					}
				})
				.setNegativeButton(R.string.No, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.cancel();
					}
				})
				.show();
	}
	
	public void deletePlayer()
	{
		Players.removePlayer(getApplicationContext(), selectedPlayer);
		finish();
	}
	
}
