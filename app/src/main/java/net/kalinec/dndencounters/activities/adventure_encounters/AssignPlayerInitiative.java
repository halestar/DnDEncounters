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
import net.kalinec.dndencounters.characters.Character;

import java.util.ArrayList;
import java.util.List;

public class AssignPlayerInitiative extends DnDEncountersActivity
{
	public static final int REQUEST_PLAYER_INITIATIVE = 70;
	private RecyclerView PlayerInitiativeRv;
	private ArrayList<AdventureEncounterPlayer> encounterPcs;
	private PcInitiativeListAdapter pcInitiativeListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		AdventureEncounter adventureEncounter = (AdventureEncounter) bundle
				.getSerializable(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
		setContentView(R.layout.activity_assign_player_initiative);
		
		assert adventureEncounter != null;
		List<Character> pcs = adventureEncounter.getParty().getMembers();
		encounterPcs = new ArrayList<>();
		for(Character pc: pcs)
			encounterPcs.add(new AdventureEncounterPlayer(pc));

		pcInitiativeListAdapter = new PcInitiativeListAdapter(getApplicationContext());
		pcInitiativeListAdapter.setPlayerList(encounterPcs);

		PlayerInitiativeRv = findViewById(R.id.PlayerInitiativeRv);
		PlayerInitiativeRv.setAdapter(pcInitiativeListAdapter);
		PlayerInitiativeRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
	}

	public void completeAssignment(View v)
	{
		Log.d("AssignPlayerInitiative", "encounterPcs: " + encounterPcs);
		Intent data = new Intent();
		data.putExtra(AdventureEncounterPlayer.PASSED_ENCOUNTER_PLAYERS, pcInitiativeListAdapter.playerList);
		setResult(RESULT_OK, data);
		finish();
	}
}
