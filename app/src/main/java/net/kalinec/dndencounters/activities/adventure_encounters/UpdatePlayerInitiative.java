package net.kalinec.dndencounters.activities.adventure_encounters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterPlayer;
import net.kalinec.dndencounters.adventure_encounters.PcInitiativeListAdapter;

import java.util.ArrayList;

public class UpdatePlayerInitiative extends DnDEncountersActivity
{
	private AdventureEncounter adventureEncounter;
	private ArrayList<AdventureEncounterPlayer> encounterPcs;
	public static final int REQUEST_UPDATED_PLAYER_INITIATIVE = 170;
	private RecyclerView PlayerInitiativeRv;
	
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
		
		PcInitiativeListAdapter pcInitiativeListAdapter
				= new PcInitiativeListAdapter(getApplicationContext());
		pcInitiativeListAdapter.setPlayerList(encounterPcs);
		
		PlayerInitiativeRv = findViewById(R.id.PlayerInitiativeRv);
		PlayerInitiativeRv.setAdapter(pcInitiativeListAdapter);
		PlayerInitiativeRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
	}
	
	public void completeAssignment(View v)
	{
		for(int i = 0; i < encounterPcs.size(); i++)
		{
			PcInitiativeListAdapter.PcInitiativeViewHolder holder = (PcInitiativeListAdapter.PcInitiativeViewHolder)PlayerInitiativeRv.findViewHolderForAdapterPosition(i);
			assert holder != null;
			encounterPcs.get(i).setInitiative(holder.getInitiative());
			Log.d("AssignPlayerInitiative", "encounterPcs: " + encounterPcs.get(i));
		}
		Log.d("AssignPlayerInitiative", "encounterPcs: " + encounterPcs);
		Intent data = new Intent();
		data.putExtra(AdventureEncounterPlayer.PASSED_ENCOUNTER_PLAYERS, encounterPcs);
		setResult(RESULT_OK, data);
		finish();
	}
}
