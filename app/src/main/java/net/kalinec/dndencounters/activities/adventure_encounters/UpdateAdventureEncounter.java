package net.kalinec.dndencounters.activities.adventure_encounters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.monsters.SelectMonster;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterActor;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonsterActorListAdapter;
import net.kalinec.dndencounters.dice.InitiativeRoller;
import net.kalinec.dndencounters.encounters.EncounterUpdater;
import net.kalinec.dndencounters.monster_tokens.MonsterToken;
import net.kalinec.dndencounters.monster_tokens.MonsterTokens;
import net.kalinec.dndencounters.monsters.Monster;

import java.util.ArrayList;
import java.util.List;

public class UpdateAdventureEncounter extends DnDEncountersActivity
{
	public static final int REQUEST_UPDATED_ENCOUNTER = 178;
	private AdventureEncounter adventureEncounter;
	private ArrayList<AdventureEncounterMonster> encounterMonsters;
	private ArrayList<EncounterUpdater> updatedMonsters;
	private RecyclerView MonsterListRv;
	private AdventureEncounterMonsterActorListAdapter adventureEncounterMonsterActorListAdapter;
	private List<MonsterToken> monsterTokens;
	private int baseInitiative;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		adventureEncounter = (AdventureEncounter) bundle.getSerializable(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
		setContentView(R.layout.activity_update_adventure_encounter);

		encounterMonsters = adventureEncounter.getAllAvailableMonsters();
		//mkae the updater list.
		updatedMonsters = new ArrayList<>();
		for(AdventureEncounterMonster m: encounterMonsters)
			updatedMonsters.add(new EncounterUpdater(m));
		MonsterListRv = findViewById(R.id.MonsterListRv);
		adventureEncounterMonsterActorListAdapter = new AdventureEncounterMonsterActorListAdapter(getApplicationContext(), updatedMonsters);
		MonsterListRv.setAdapter(adventureEncounterMonsterActorListAdapter);
		MonsterListRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		monsterTokens = MonsterTokens.getAllMonsterTokens(getApplicationContext());
		if(encounterMonsters.size() > 0)
			baseInitiative = encounterMonsters.get(0).getInitiative();
		else
			baseInitiative = (new InitiativeRoller(0)).roll();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == SelectMonster.REQUEST_SELECT_MONSTER)
		{
			if(resultCode == RESULT_OK)
			{
				assert data != null;
				Monster selectedMonster = (Monster)data.getSerializableExtra(Monster.PASSED_MONSTER);
				AdventureEncounterMonster adventureMonster = new AdventureEncounterMonster(selectedMonster, monsterTokens.get(0));
				adventureMonster.setHasActed(false);
				adventureMonster.setStatus(AdventureEncounterActor.ALIVE);
				if(adventureEncounter.isMonsterInitiative())
				{
					InitiativeRoller initiativeDice = new InitiativeRoller(adventureMonster.getMonster().getDex_mod());
					adventureMonster.setInitiative(initiativeDice.roll());
				}
				else
				{
					adventureMonster.setInitiative(baseInitiative);
				}
				if(adventureEncounter.isMonsterHp())
				{
					int monsterHp = adventureMonster.getMonster().rollHp();
					adventureMonster.setHp(monsterHp);
					adventureMonster.setMaxHp(monsterHp);
				}
				else
				{
					adventureMonster.setHp(adventureMonster.getMonster().getHp());
					adventureMonster.setMaxHp(adventureMonster.getMonster().getHp());
				}
				updatedMonsters.add(new EncounterUpdater(adventureMonster));
				adventureEncounterMonsterActorListAdapter.setMonsterList(updatedMonsters);
			}
		}
	}
	
	public void addMonster(View v)
	{
		Intent myIntent = new Intent(UpdateAdventureEncounter.this, SelectMonster.class);
		startActivityForResult(myIntent, SelectMonster.REQUEST_SELECT_MONSTER);
	}
	
	public void updateEncounter(View v)
	{
		ArrayList<AdventureEncounterMonster> newEncounterMonsters = new ArrayList<>();
		for(EncounterUpdater m: updatedMonsters)
			newEncounterMonsters.add(m.getUpdatedMonster());
		Intent data = new Intent();
		data.putExtra(AdventureEncounterMonster.PASSED_ENCOUNTER_MONSTERS, newEncounterMonsters);
		setResult(RESULT_OK, data);
		finish();
	}
}
