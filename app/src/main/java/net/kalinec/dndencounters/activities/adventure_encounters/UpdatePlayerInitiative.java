package net.kalinec.dndencounters.activities.adventure_encounters;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.characters.AddCharacter;
import net.kalinec.dndencounters.activities.characters.SelectCharacter;
import net.kalinec.dndencounters.activities.monsters.SelectMonster;
import net.kalinec.dndencounters.activities.parties.CreateParty;
import net.kalinec.dndencounters.activities.players.CreatePlayerAndCharacter;
import net.kalinec.dndencounters.activities.players.SelectPlayer;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterActor;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterPlayer;
import net.kalinec.dndencounters.adventure_encounters.AdventurePcListAdapter;
import net.kalinec.dndencounters.adventure_encounters.PcInitiativeListAdapter;
import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.dice.InitiativeRoller;
import net.kalinec.dndencounters.encounters.EncounterPcUpdater;
import net.kalinec.dndencounters.encounters.EncounterUpdater;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.players.Player;

import java.util.ArrayList;

public class UpdatePlayerInitiative extends DnDEncountersActivity
{
	private AdventureEncounter adventureEncounter;
	private ArrayList<AdventureEncounterPlayer> encounterPcs;
	public static final int REQUEST_UPDATED_PLAYER_INITIATIVE = 170;
	private RecyclerView PlayerInitiativeRv;
	private AdventurePcListAdapter pcInitiativeListAdapter;
	private ArrayList<EncounterPcUpdater> pcUpdates;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		adventureEncounter = (AdventureEncounter) bundle.getSerializable(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
		setContentView(R.layout.activity_update_player_initiative);
		
		assert adventureEncounter != null;
		encounterPcs = adventureEncounter.getAvailablePlayers();
		pcUpdates = new ArrayList<>();
		for(AdventureEncounterPlayer pc: encounterPcs)
			pcUpdates.add(new EncounterPcUpdater(pc));
		
		pcInitiativeListAdapter = new AdventurePcListAdapter(getApplicationContext(), pcUpdates);

		PlayerInitiativeRv = findViewById(R.id.PlayerInitiativeRv);
		PlayerInitiativeRv.setAdapter(pcInitiativeListAdapter);
		PlayerInitiativeRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		//super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == SelectCharacter.REQUEST_SELECT_CHARACTER || requestCode == CreatePlayerAndCharacter.REQUEST_NEW_CHARACTER || requestCode == AddCharacter.REQUEST_NEW_CHARACTER)
		{
			if(resultCode == RESULT_OK)
			{
				assert data != null;
				Character pc = (Character)data.getSerializableExtra(Character.PASSED_CHARACTER);
				AdventureEncounterPlayer adventurePc = new AdventureEncounterPlayer(pc);
				EncounterPcUpdater newUpdater = new EncounterPcUpdater(adventurePc);
				pcUpdates.add(newUpdater);
				pcInitiativeListAdapter.notifyDataSetChanged();
			}
		}
		else if(requestCode == SelectPlayer.REQUEST_SELECT_PLAYER)
		{
			if(resultCode == RESULT_OK)
			{
				//create a character for this player.
				assert data != null;
				Player player = (Player)data.getSerializableExtra(Player.PASSED_PLAYER);
				Intent myIntent = new Intent(UpdatePlayerInitiative.this, AddCharacter.class);
				myIntent.putExtra(Player.PASSED_PLAYER, player);
				startActivityForResult(myIntent, AddCharacter.REQUEST_NEW_CHARACTER);
			}
		}
	}
	
	public void completeAssignment(View v)
	{
		ArrayList<AdventureEncounterPlayer> newEncounterPcs = new ArrayList<>();
		for(EncounterPcUpdater m: pcUpdates)
			newEncounterPcs.add(m.getUpdatedPc());
		Intent data = new Intent();
		data.putExtra(AdventureEncounterPlayer.PASSED_ENCOUNTER_PLAYERS, newEncounterPcs);
		setResult(RESULT_OK, data);
		finish();
	}

	public void selectExistingPc(View v)
	{
		Intent myIntent = new Intent(UpdatePlayerInitiative.this, SelectCharacter.class);
		startActivityForResult(myIntent, SelectCharacter.REQUEST_SELECT_CHARACTER);
	}

	public void createPlayerAndCharacter(View v)
	{
		Intent myIntent = new Intent(UpdatePlayerInitiative.this, CreatePlayerAndCharacter.class);
		startActivityForResult(myIntent, CreatePlayerAndCharacter.REQUEST_NEW_CHARACTER);
	}

	public void selectPlayer(View v)
	{
		Intent myIntent = new Intent(UpdatePlayerInitiative.this, SelectPlayer.class);
		startActivityForResult(myIntent, SelectPlayer.REQUEST_SELECT_PLAYER);
	}
}
