package net.kalinec.dndencounters.activities.players;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import net.kalinec.dndencounters.db.AppDatabase;
import net.kalinec.dndencounters.db.CharacterDao;
import net.kalinec.dndencounters.db.PlayerDao;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.players.Player;

import java.util.List;

public class ViewPlayer extends DnDEncountersActivity
{
	protected Player selectedPlayer;
	protected TextView playerNameTv;
	private RecyclerView recyclerview_characters;
	private CharacterListAdapter characterListAdapter;
	private LiveData<List<Character>> pcList;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_player);
		Bundle bundle = getIntent().getExtras();
		selectedPlayer = (Player)bundle.getSerializable(Player.PASSED_PLAYER);
		
		playerNameTv = findViewById(R.id.playerName);
		playerNameTv.setText(selectedPlayer.getName());

		CharacterDao characterDao = AppDatabase.getDatabase(getApplicationContext()).characterDao();
		pcList = characterDao.getCharactersByPlayerId(selectedPlayer.getUid());

		characterListAdapter = new CharacterListAdapter(getApplicationContext(), new RvClickListener() {
			@Override
			public void onClick(View view, int position) {
				Character pc = characterListAdapter.get(position);
				editPc(pc);
			}
		});
		pcList.observe(this, new Observer<List<Character>>() {
			@Override
			public void onChanged(@Nullable List<Character> characters) {
				characterListAdapter.setCharacterList(characters);
			}
		});


		recyclerview_characters = findViewById(R.id.recyclerview_characters);
		recyclerview_characters.setAdapter(characterListAdapter);
		recyclerview_characters.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



	}
	
	public void newPC(View target)
	{
		Intent myIntent = new Intent(ViewPlayer.this, AddCharacter.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Player.PASSED_PLAYER, selectedPlayer);
		myIntent.putExtras(bundle);
		ViewPlayer.this.startActivity(myIntent);
	}

	public void editPc(Character pc)
	{
		Intent myIntent = new Intent(ViewPlayer.this, EditCharacter.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Character.PASSED_CHARACTER, pc);
		myIntent.putExtras(bundle);
		ViewPlayer.this.startActivity(myIntent);
	}
	
	public void editPlayer(View target)
	{
		Intent myIntent = new Intent(ViewPlayer.this, EditPlayer.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Player.PASSED_PLAYER, selectedPlayer);
		myIntent.putExtras(bundle);
		ViewPlayer.this.startActivity(myIntent);
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
		PlayerDao playerDao = AppDatabase.getDatabase(getApplicationContext()).playerDao();
		playerDao.delete(selectedPlayer);
		finish();
	}
	
}
