package net.kalinec.dndencounters.activities.parties;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.characters.AddCharacter;
import net.kalinec.dndencounters.activities.characters.SelectCharacter;
import net.kalinec.dndencounters.activities.players.CreatePlayerAndCharacter;
import net.kalinec.dndencounters.activities.players.SelectPlayer;
import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.parties.Party;
import net.kalinec.dndencounters.parties.PartyListAdapter;
import net.kalinec.dndencounters.players.Player;
import net.kalinec.dndencounters.playsessions.PlaySession;

public class CreateParty extends DnDEncountersActivity
{
	public static final int REQUEST_NEW_PARTY = 30;
	private RecyclerView partyMemberListRv;
	private PlaySession activeSession;
	private Party selectedParty;
	private EditText partyNameEt;
	private PartyListAdapter partyListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		activeSession = (PlaySession) bundle.getSerializable(PlaySession.PASSED_SESSION);
		selectedParty = activeSession.getPlayers();
		setContentView(R.layout.activity_create_party);

		partyNameEt = findViewById(R.id.partyNameEt);
		partyNameEt.setText(selectedParty.getName());
		partyMemberListRv = findViewById(R.id.partyMemberListRv);

		partyListAdapter = new PartyListAdapter(getApplicationContext(), new RvClickListener() {
			@Override
			public void onClick(View view, int position) {
                Character pc = partyListAdapter.get(position);
                removePartyMember(pc);
			}
		});
		partyListAdapter.setParty(selectedParty);
		partyMemberListRv.setAdapter(partyListAdapter);
		partyMemberListRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
	}

	public void selectExistingPc(View v)
	{
		Intent myIntent = new Intent(CreateParty.this, SelectCharacter.class);
		startActivityForResult(myIntent, SelectCharacter.REQUEST_SELECT_CHARACTER);
	}

	public void createPlayerAndCharacter(View v)
	{
		Intent myIntent = new Intent(CreateParty.this, CreatePlayerAndCharacter.class);
		startActivityForResult(myIntent, CreatePlayerAndCharacter.REQUEST_NEW_CHARACTER);
	}

	public void selectPlayer(View v)
	{
		Intent myIntent = new Intent(CreateParty.this, SelectPlayer.class);
		startActivityForResult(myIntent, SelectPlayer.REQUEST_SELECT_PLAYER);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		if(requestCode == SelectCharacter.REQUEST_SELECT_CHARACTER || requestCode == CreatePlayerAndCharacter.REQUEST_NEW_CHARACTER || requestCode == AddCharacter.REQUEST_NEW_CHARACTER)
		{
			if(resultCode == RESULT_OK)
			{
				Character pc = (Character)data.getSerializableExtra(Character.PASSED_CHARACTER);
				selectedParty.addMember(pc);
				partyListAdapter.setParty(selectedParty);
			}
		}
		else if(requestCode == SelectPlayer.REQUEST_SELECT_PLAYER)
		{
			if(resultCode == RESULT_OK)
			{
				//create a character for this player.
				Player player = (Player)data.getSerializableExtra(Player.PASSED_PLAYER);
				Intent myIntent = new Intent(CreateParty.this, AddCharacter.class);
				myIntent.putExtra(Player.PASSED_PLAYER, player);
				startActivityForResult(myIntent, AddCharacter.REQUEST_NEW_CHARACTER);
			}
		}
	}

	public void removePartyMember(Character pc)
    {
        selectedParty.removeMember(pc);
        partyListAdapter.setParty(selectedParty);
    }

    public void createParty(View v)
    {
        selectedParty.setName(partyNameEt.getText().toString());
        Intent data = new Intent();
        data.putExtra(Party.PASSED_PARTY, selectedParty);
        setResult(RESULT_OK, data);
        finish();
    }
}
