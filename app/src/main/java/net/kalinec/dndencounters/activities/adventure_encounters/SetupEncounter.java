package net.kalinec.dndencounters.activities.adventure_encounters;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.monster_tokens.AssignMonsterTokens;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterActor;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterPlayer;
import net.kalinec.dndencounters.dice.InitiativeRoller;
import net.kalinec.dndencounters.encounters.Encounter;

import java.util.ArrayList;

public class SetupEncounter extends AppCompatActivity {

    public static final int SETUP_ADVENTURE_ENCOUNTER = 63;
    private TextView monsterTokensCompletedIconTv, playerInitiativeCompletedIconTv;
    private Switch assignIndividualMonsterInitiativeSw, rollMonsterHp;
    private Button StartEncounterBtn;
    private AdventureEncounter selectedEncounter;
    private ArrayList<AdventureEncounterMonster> encounterMonsterList;
    private ArrayList<AdventureEncounterPlayer> encounterPlayers;
    private boolean completedMonsterTokens, completedPlayerInitiative, individualInitiative, rollForHp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        selectedEncounter = (AdventureEncounter)bundle.getSerializable(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
        setContentView(R.layout.activity_setup_encounter);

        monsterTokensCompletedIconTv = findViewById(R.id.monsterTokensCompletedIconTv);
        playerInitiativeCompletedIconTv = findViewById(R.id.playerInitiativeCompletedIconTv);

        individualInitiative = false;
        assignIndividualMonsterInitiativeSw = findViewById(R.id.assignIndividualMonsterInitiativeSw);
        assignIndividualMonsterInitiativeSw.setChecked(individualInitiative);
        assignIndividualMonsterInitiativeSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                individualInitiative = isChecked;
            }
        });

        rollForHp = false;
        rollMonsterHp = findViewById(R.id.rollMonsterHp);
        rollMonsterHp.setChecked(rollForHp);
        rollMonsterHp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rollForHp = isChecked;
            }
        });

        StartEncounterBtn = findViewById(R.id.StartEncounterBtn);
        completedMonsterTokens = completedPlayerInitiative = false;

        if(!selectedEncounter.isSetup())
        {
            StartEncounterBtn.setVisibility(View.GONE);
        }
        
    }
    
    private void setCompletedAll()
    {
        if(completedMonsterTokens && completedPlayerInitiative)
            StartEncounterBtn.setVisibility(View.VISIBLE);
        else
            StartEncounterBtn.setVisibility(View.GONE);
    }

    private void setMonstersCompletedIcon()
    {
        if(completedMonsterTokens)
        {
            monsterTokensCompletedIconTv.setText(R.string.fa_check);
            monsterTokensCompletedIconTv.setTextColor(Color.GREEN);
        }
        else
        {
            monsterTokensCompletedIconTv.setText(R.string.fa_times);
            monsterTokensCompletedIconTv.setTextColor(Color.RED);
        }
        setCompletedAll();
    }

    private void setPcsCompletedIcon()
    {
        if(completedPlayerInitiative)
        {
            playerInitiativeCompletedIconTv.setText(R.string.fa_check);
            playerInitiativeCompletedIconTv.setTextColor(Color.GREEN);
        }
        else
        {
            playerInitiativeCompletedIconTv.setText(R.string.fa_times);
            playerInitiativeCompletedIconTv.setTextColor(Color.RED);
        }
        setCompletedAll();
    }

    public void setupMonsterTokens(View v)
    {
        Intent myIntent = new Intent(SetupEncounter.this, AssignMonsterTokens.class);
        myIntent.putExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER, selectedEncounter);
        startActivityForResult(myIntent, AssignMonsterTokens.REQUEST_ENCOUNTER_MONSTERS);
    }
    
    public void completeMonsterTokens()
    {
        if(encounterMonsterList.size() == selectedEncounter.getEncounter().getMonsters().size())
            completedMonsterTokens = true;
        else
            completedMonsterTokens = false;
        setMonstersCompletedIcon();
    }

    public void setupPcInitiative(View v)
    {
        Intent myIntent = new Intent(SetupEncounter.this, AssignPlayerInitiative.class);
        myIntent.putExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER, selectedEncounter);
        startActivityForResult(myIntent, AssignPlayerInitiative.REQUEST_PLAYER_INITIATIVE);
    }

    public void completePlayerInitiative()
    {
        if(encounterPlayers.size() == selectedEncounter.getParty().getMembers().size())
            completedPlayerInitiative = true;
        else
            completedPlayerInitiative = false;
        setPcsCompletedIcon();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode == AssignMonsterTokens.REQUEST_ENCOUNTER_MONSTERS && resultCode == RESULT_OK)
        {
            encounterMonsterList = (ArrayList<AdventureEncounterMonster>)data.getSerializableExtra(AdventureEncounterMonster.PASSED_ENCOUNTER_MONSTERS);
            completeMonsterTokens();
        }
        if(requestCode == AssignPlayerInitiative.REQUEST_PLAYER_INITIATIVE && resultCode == RESULT_OK)
        {
            encounterPlayers = (ArrayList<AdventureEncounterPlayer>)data.getSerializableExtra(AdventureEncounterPlayer.PASSED_ENCOUNTER_PLAYERS);
            completePlayerInitiative();
        }
    }

    public void completeSetup(View v)
    {
        selectedEncounter.clearActors();
        for(AdventureEncounterPlayer pc: encounterPlayers)
        {
            pc.setHasActed(false);
            pc.setStatus(AdventureEncounterActor.ALIVE);
            selectedEncounter.addActor(pc);
        }
        int max_mod = -10;
        for(AdventureEncounterMonster monster: encounterMonsterList)
        {
            if(monster.getMonster().getDex_mod() > max_mod)
                max_mod = monster.getMonster().getDex_mod();
        }
        InitiativeRoller initiativeDice = new InitiativeRoller(max_mod);
        int initiative = initiativeDice.roll();
        for(AdventureEncounterMonster monster: encounterMonsterList)
        {
            monster.setHasActed(false);
            monster.setStatus(AdventureEncounterActor.ALIVE);
            if(individualInitiative)
            {
                initiativeDice.setModifier(monster.getMonster().getDex_mod());
                monster.setInitiative(initiativeDice.roll());
            }
            else
                monster.setInitiative(initiative);
            if(rollForHp)
                monster.setHp(monster.getMonster().rollHp());
            else
                monster.setHp(monster.getMonster().getHp());
            selectedEncounter.addActor(monster);
        }
        if(selectedEncounter.isSetup())
        {
            selectedEncounter.beginEncounter();
            Intent data = new Intent();
            data.putExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER, selectedEncounter);
            setResult(RESULT_OK, data);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Error setting up! Did you enter all the information?", Toast.LENGTH_LONG).show();
        }
    }
}
