package net.kalinec.dndencounters.activities.encounter_turn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterPlayer;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterTurn;
import net.kalinec.dndencounters.characters.CharacterEncounterListAdapter;
import net.kalinec.dndencounters.fragments.MonsterTarget;

import java.util.ArrayList;

public class MonsterTurn extends DnDEncountersActivity implements MonsterTarget.OnMonsterCompletedListener{

    public static final int MONSTER_TURN = 72;
    private AdventureEncounter selectedEncounter;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
		assert bundle != null;
        selectedEncounter = (AdventureEncounter)bundle.getSerializable(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
        setContentView(R.layout.activity_monster_turn);
		AdventureEncounterTurn currentTurn = selectedEncounter.getCurrentTurn();
		AdventureEncounterMonster currentMonster = (AdventureEncounterMonster) currentTurn
				.getCurrentActor();
		ArrayList<AdventureEncounterPlayer> availablePlayers = selectedEncounter
				.getAvailablePlayers();
		
		RecyclerView availablePlayersRv = findViewById(R.id.AvailablePlayersRv);
		CharacterEncounterListAdapter characterEncounterListAdapter
				= new CharacterEncounterListAdapter(getApplicationContext());
        characterEncounterListAdapter.setCharacterList(availablePlayers);
		availablePlayersRv.setAdapter(characterEncounterListAdapter);
		availablePlayersRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		
		MonsterTarget monsterTarget = MonsterTarget.newInstance(currentMonster);
		FragmentManager fManager = getSupportFragmentManager();
		FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.MonsterTargetFl, monsterTarget);
        fTransaction.addToBackStack(null);
        fTransaction.commit();
    }

    @Override
    public void onMonsterCompletedListener(AdventureEncounterMonster activeMonster) {
        selectedEncounter.updateActor(activeMonster);
        finishTurn();
    }

    public void finishTurn()
    {
        selectedEncounter.getCurrentTurn().completeRound();
        Intent data = new Intent();
        data.putExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER, selectedEncounter);
        setResult(RESULT_OK, data);
        finish();
    }
}
