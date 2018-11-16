package net.kalinec.dndencounters;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import net.kalinec.dndencounters.encounter.AdventureEncounter;
import net.kalinec.dndencounters.encounter.EncounterMonster;
import net.kalinec.dndencounters.encounter.EncounterPlayer;
import net.kalinec.dndencounters.players.Player;
import net.kalinec.dndencounters.playsessions.PlaySession;

import java.util.List;

public class SetupEncounter extends AppCompatActivity {

    public static final int SETUP_ADVENTURE_ENCOUNTER = 63;
    private TextView monsterTokensCompletedIconTv, playerInitiativeCompletedIconTv;
    private Switch assignIndividualMonsterInitiativeSw, rollMonsterHp;
    private Button StartEncounterBtn;
    private AdventureEncounter selectedEncounter;
    private List<EncounterMonster> encounterMonsterList;
    private List<EncounterPlayer> encounterPlayers;


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

        if(!selectedEncounter.isSetup())
        {
            StartEncounterBtn.setVisibility(View.GONE);
        }
    }

    private void setMonstersCompletedIcon(boolean completed)
    {
        if(completed)
        {
            monsterTokensCompletedIconTv.setText(R.string.fa_check);
            monsterTokensCompletedIconTv.setTextColor(Color.GREEN);
        }
        else
        {
            monsterTokensCompletedIconTv.setText(R.string.fa_times);
            monsterTokensCompletedIconTv.setTextColor(Color.RED);
        }
    }

    private void setPcsCompletedIcon(boolean completed)
    {
        if(completed)
        {
            playerInitiativeCompletedIconTv.setText(R.string.fa_check);
            playerInitiativeCompletedIconTv.setTextColor(Color.GREEN);
        }
        else
        {
            playerInitiativeCompletedIconTv.setText(R.string.fa_times);
            playerInitiativeCompletedIconTv.setTextColor(Color.RED);
        }
    }

    public void setupMonsterTokens(View v)
    {
        Intent myIntent = new Intent(SetupEncounter.this, AssignMonsterTokens.class);
        myIntent.putExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER, selectedEncounter);
        startActivityForResult(myIntent, AssignMonsterTokens.REQUEST_ENCOUNTER_MONSTERS);
    }
}
