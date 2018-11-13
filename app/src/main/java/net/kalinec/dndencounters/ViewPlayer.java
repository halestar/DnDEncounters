package net.kalinec.dndencounters;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.kalinec.dndencounters.characters.CharacterListFragment;
import net.kalinec.dndencounters.db.AppDatabase;
import net.kalinec.dndencounters.db.PlayerDao;
import net.kalinec.dndencounters.players.Player;

public class ViewPlayer extends AppCompatActivity
{
	protected Player selectedPlayer;
	protected TextView playerNameTv;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_player);
		Bundle bundle = getIntent().getExtras();
		selectedPlayer = (Player)bundle.getSerializable(Player.PASSED_PLAYER);
		
		playerNameTv = findViewById(R.id.playerName);
		playerNameTv.setText(selectedPlayer.getName());
		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.PlayerCharacterHolder, CharacterListFragment.newInstance());
		fragmentTransaction.commitNow();
	}
	
	public void newPC(View target)
	{
		Intent myIntent = new Intent(ViewPlayer.this, AddCharacter.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Player.PASSED_PLAYER, selectedPlayer);
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
