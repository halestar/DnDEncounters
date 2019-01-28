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
import net.kalinec.dndencounters.lib.RvClickListener;
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
		encounterMonsters = adventureEncounter.getAvailableMonsters();
		MonsterListRv = findViewById(R.id.MonsterListRv);
		adventureEncounterMonsterActorListAdapter = new AdventureEncounterMonsterActorListAdapter(getApplicationContext(), new RvClickListener()
		{
			@Override
			public void onClick(View view, int position)
			{
				encounterMonsters.remove(position);
				adventureEncounterMonsterActorListAdapter.setMonsterList(encounterMonsters);
			}
		});
		adventureEncounterMonsterActorListAdapter.setMonsterList(encounterMonsters);
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
				encounterMonsters.add(adventureMonster);
				adventureEncounterMonsterActorListAdapter.setMonsterList(encounterMonsters);
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
		for(int i = 0; i < encounterMonsters.size(); i++)
		{
			AdventureEncounterMonsterActorListAdapter.AdventureEncounterMonsterActorViewHolder holder =
					(AdventureEncounterMonsterActorListAdapter.AdventureEncounterMonsterActorViewHolder)MonsterListRv.findViewHolderForAdapterPosition(i);
			assert holder != null;
			encounterMonsters.get(i).setInitiative(holder.getInitiative());
			encounterMonsters.get(i).setToken(holder.getMonsterToken());
			encounterMonsters.get(i).setHp(holder.getCurrentHp());
			if(holder.isAlive())
				encounterMonsters.get(i).setStatus(AdventureEncounterActor.ALIVE);
			else
				encounterMonsters.get(i).setStatus(AdventureEncounterActor.DEAD);
		}
		Intent data = new Intent();
		data.putExtra(AdventureEncounterMonster.PASSED_ENCOUNTER_MONSTERS, encounterMonsters);
		setResult(RESULT_OK, data);
		finish();
	}
}
