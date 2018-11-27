package net.kalinec.dndencounters.activities.encounter_turn;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterPlayer;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterTurn;
import net.kalinec.dndencounters.fragments.AvailableMonsters;
import net.kalinec.dndencounters.fragments.MonsterTarget;
import net.kalinec.dndencounters.lib.RvClickListener;

import java.util.ArrayList;
import java.util.List;

public class PlayerTurn extends AppCompatActivity implements AvailableMonsters.OnMonsterSelectedListener {
    public static final int PLAYER_TURN = 71;
    private AdventureEncounter selectedEncounter;
    private AdventureEncounterTurn currentTurn;
    private AdventureEncounterPlayer currentPc;
    private AvailableMonsters availableMonstersFragment;
    private MonsterTarget monsterTarget;
    private TextView PlayerNameTv, PlayerAcTv, PlayerSpellDcTv;
    private FragmentManager fManager;
    private FragmentTransaction fTransaction;
    private ArrayList<AdventureEncounterMonster> availableMonsters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        selectedEncounter = (AdventureEncounter)bundle.getSerializable(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
        setContentView(R.layout.activity_player_turn);
        currentTurn = selectedEncounter.getCurrentTurn();
        currentPc = (AdventureEncounterPlayer)currentTurn.getCurrentActor();

        //fill in PC stats
        PlayerNameTv = findViewById(R.id.PlayerNameTv);
        PlayerNameTv.setText(currentPc.getPc().getName());
        PlayerAcTv = findViewById(R.id.PlayerAcTv);
        PlayerAcTv.setText(Integer.toString(currentPc.getPc().getAc()));
        PlayerSpellDcTv = findViewById(R.id.PlayerSpellDcTv);
        PlayerSpellDcTv.setText(Integer.toString(currentPc.getPc().getSpellDc()));


        //insert the available monsters.
        fManager = getSupportFragmentManager();
        fTransaction = fManager.beginTransaction();
        availableMonsters = selectedEncounter.getAvailableMonsters();
        availableMonstersFragment = AvailableMonsters.newInstance(availableMonsters);
        fTransaction.add(R.id.FragmentContainerSv, availableMonstersFragment);
        fTransaction.commit();
    }

    @Override
    public void onMonsterSelectedListener(AdventureEncounterMonster selectedMonster) {
        monsterTarget = MonsterTarget.newInstance(selectedMonster);
        fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.FragmentContainerSv, monsterTarget);
        fTransaction.addToBackStack(null);
        fTransaction.commit();
    }
}
