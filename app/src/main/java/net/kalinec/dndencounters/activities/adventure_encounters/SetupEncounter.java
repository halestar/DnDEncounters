package net.kalinec.dndencounters.activities.adventure_encounters;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.monster_tokens.AssignMonsterTokens;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterPlayer;

import java.util.ArrayList;

public class SetupEncounter extends AppCompatActivity {

    public static final int SETUP_ADVENTURE_ENCOUNTER = 63;
    private TextView monsterTokensCompletedIconTv, playerInitiativeCompletedIconTv;
    private Switch assignIndividualMonsterInitiativeSw, rollMonsterHp;
    private Button StartEncounterBtn;
    private AdventureEncounter selectedEncounter;
    private ArrayList<AdventureEncounterMonster> encounterMonsterList;
    private ArrayList<AdventureEncounterPlayer> encounterPlayers;
    private boolean completedMonsterTokens, completedPlayerInitiative;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        selectedEncounter = (AdventureEncounter)bundle.getSerializable(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
        setContentView(R.layout.activity_setup_encounter);

        monsterTokensCompletedIconTv = findViewById(R.id.monsterTokensCompletedIconTv);
        playerInitiativeCompletedIconTv = findViewById(R.id.playerInitiativeCompletedIconTv);
        assignIndividualMonsterInitiativeSw = findViewById(R.id.assignIndividualMonsterInitiativeSw);
        rollMonsterHp = findViewById(R.id.rollMonsterHp);
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode == AssignMonsterTokens.REQUEST_ENCOUNTER_MONSTERS && resultCode == RESULT_OK)
        {
            encounterMonsterList = (ArrayList<AdventureEncounterMonster>)data.getSerializableExtra(AdventureEncounterMonster.PASSED_ENCOUNTER_MONSTERS);
            completeMonsterTokens();
        }
    }
}
