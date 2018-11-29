package net.kalinec.dndencounters.activities.encounter_turn;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterPlayer;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterTurn;
import net.kalinec.dndencounters.characters.CharacterEncounterListAdapter;
import net.kalinec.dndencounters.fragments.AvailableMonsters;
import net.kalinec.dndencounters.fragments.MonsterTarget;

import java.util.ArrayList;

public class MonsterTurn extends AppCompatActivity implements MonsterTarget.OnMonsterCompletedListener{

    public static final int MONSTER_TURN = 72;
    private AdventureEncounter selectedEncounter;
    private AdventureEncounterTurn currentTurn;
    private AdventureEncounterMonster currentMonster;
    private ArrayList<AdventureEncounterPlayer> availablePlayers;
    private RecyclerView AvailablePlayersRv;
    private CharacterEncounterListAdapter characterEncounterListAdapter;
    private MonsterTarget monsterTarget;
    private FragmentManager fManager;
    private FragmentTransaction fTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        selectedEncounter = (AdventureEncounter)bundle.getSerializable(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
        setContentView(R.layout.activity_monster_turn);
        currentTurn = selectedEncounter.getCurrentTurn();
        currentMonster = (AdventureEncounterMonster)currentTurn.getCurrentActor();
        availablePlayers = selectedEncounter.getAvailablePlayers();

        AvailablePlayersRv = findViewById(R.id.AvailablePlayersRv);
        characterEncounterListAdapter = new CharacterEncounterListAdapter(getApplicationContext());
        characterEncounterListAdapter.setCharacterList(availablePlayers);
        AvailablePlayersRv.setAdapter(characterEncounterListAdapter);
        AvailablePlayersRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        monsterTarget = MonsterTarget.newInstance(currentMonster);
        fManager = getSupportFragmentManager();
        fTransaction = fManager.beginTransaction();
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
