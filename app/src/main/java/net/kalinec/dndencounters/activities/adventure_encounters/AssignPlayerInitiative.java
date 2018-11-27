package net.kalinec.dndencounters.activities.adventure_encounters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterPlayer;
import net.kalinec.dndencounters.adventure_encounters.PcInitiativeListAdapter;
import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.db.AppDatabase;
import net.kalinec.dndencounters.db.PlayerDao;
import net.kalinec.dndencounters.players.Player;

import java.util.ArrayList;
import java.util.List;

public class AssignPlayerInitiative extends AppCompatActivity
{
	public static final int REQUEST_PLAYER_INITIATIVE = 70;
	private AdventureEncounter adventureEncounter;
	private RecyclerView PlayerInitiativeRv;
	private List<Character> pcs;
	private ArrayList<AdventureEncounterPlayer> encounterPcs;
	private PcInitiativeListAdapter pcInitiativeListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		adventureEncounter = (AdventureEncounter)bundle.getSerializable(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
		setContentView(R.layout.activity_assign_player_initiative);

		pcs = adventureEncounter.getParty().getMembers();
		encounterPcs = new ArrayList<>();
		for(Character pc: pcs)
		{
			PlayerDao playerDao = AppDatabase.getDatabase(getApplicationContext()).playerDao();
			Player owner = playerDao.getPlayerById(pc.getPlayerId());
			encounterPcs.add(new AdventureEncounterPlayer(pc, owner));
		}
		pcInitiativeListAdapter = new PcInitiativeListAdapter(getApplicationContext());
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
