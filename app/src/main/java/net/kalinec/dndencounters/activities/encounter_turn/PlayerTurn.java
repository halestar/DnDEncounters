package net.kalinec.dndencounters.activities.encounter_turn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterPlayer;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterTurn;
import net.kalinec.dndencounters.fragments.AvailableMonsters;
import net.kalinec.dndencounters.fragments.MonsterTarget;

import java.util.ArrayList;
import java.util.Locale;

public class PlayerTurn extends DnDEncountersActivity implements AvailableMonsters.OnMonsterSelectedListener, MonsterTarget.OnMonsterCompletedListener {
    public static final int PLAYER_TURN = 71;
    private AdventureEncounter selectedEncounter;
    private AvailableMonsters availableMonstersFragment;
    private FragmentManager fManager;
    private FragmentTransaction fTransaction;
    private ArrayList<AdventureEncounterMonster> availableMonsters;
    private Button FinishPlayerTurnBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
	    assert bundle != null;
        selectedEncounter = (AdventureEncounter)bundle.getSerializable(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
        setContentView(R.layout.activity_player_turn);
        AdventureEncounterTurn currentTurn = selectedEncounter.getCurrentTurn();
        AdventureEncounterPlayer currentPc = (AdventureEncounterPlayer) currentTurn.getCurrentActor();

        //fill in PC stats
        TextView playerNameTv = findViewById(R.id.PlayerNameTv);
        playerNameTv.setText(currentPc.getPc().getName());
        TextView playerAcTv = findViewById(R.id.PlayerAcTv);
	    playerAcTv.setText(String.format(Locale.getDefault(), "%d", currentPc.getPc().getAc()));
        TextView playerSpellDcTv = findViewById(R.id.PlayerSpellDcTv);
	    playerSpellDcTv.setText(String.format(Locale.getDefault(), "%d", currentPc.getPc()
			    .getSpellDc()));


        //insert the available monsters.
        fManager = getSupportFragmentManager();
        fTransaction = fManager.beginTransaction();
        availableMonsters = selectedEncounter.getAvailableMonsters();
        availableMonstersFragment = AvailableMonsters.newInstance(availableMonsters);
        fTransaction.add(R.id.FragmentContainerSv, availableMonstersFragment);
        fTransaction.commit();


        FinishPlayerTurnBt = findViewById(R.id.FinishPlayerTurnBt);
    }

    @Override
    public void onMonsterSelectedListener(AdventureEncounterMonster selectedMonster) {
        FinishPlayerTurnBt.setVisibility(View.GONE);
        showSelectedMonster(selectedMonster);
    }

    @Override
    public void onMonsterCompletedListener(AdventureEncounterMonster activeMonster) {
        selectedEncounter.updateActor(activeMonster);
        FinishPlayerTurnBt.setVisibility(View.VISIBLE);
        showAvailableMonsters();
    }

    private void showAvailableMonsters()
    {
        fManager = getSupportFragmentManager();
        fTransaction = fManager.beginTransaction();
        availableMonsters = selectedEncounter.getAvailableMonsters();
        availableMonstersFragment = AvailableMonsters.newInstance(availableMonsters);
        fTransaction.replace(R.id.FragmentContainerSv, availableMonstersFragment);
        fTransaction.commit();
    }

    private void showSelectedMonster(AdventureEncounterMonster selectedMonster)
    {
        MonsterTarget monsterTarget = MonsterTarget.newInstance(selectedMonster);
        fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.FragmentContainerSv, monsterTarget);
        fTransaction.addToBackStack(null);
        fTransaction.commit();
    }

    public void finishTurn(View v)
    {
        selectedEncounter.getCurrentTurn().completeRound();
        Intent data = new Intent();
        data.putExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER, selectedEncounter);
        setResult(RESULT_OK, data);
        finish();
    }
}
