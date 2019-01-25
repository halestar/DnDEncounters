package net.kalinec.dndencounters.activities.encounters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.adventure_encounters.SetupEncounter;
import net.kalinec.dndencounters.activities.encounter_turn.MonsterTurn;
import net.kalinec.dndencounters.activities.encounter_turn.PlayerTurn;
import net.kalinec.dndencounters.adventure_encounters.AdventureActorsListAdapter;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterActor;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterTurn;
import net.kalinec.dndencounters.playsessions.PlaySession;

import java.util.Locale;

public class PlayEncounter extends DnDEncountersActivity
{
    public static final int PLAY_ENCOUNTER = 60;
    private PlaySession activeSession;
    private AdventureEncounter adventureEncounter;
    private AdventureEncounterTurn currentTurn;
    private TextView PartyNameTv, EncounterNameTv, RoundNumTv, NextUpNameTv, encounterOverTv;
    private Group TurnFinishedGroup, ContinueTurnGroup;
    private RecyclerView ActorListRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
	    assert bundle != null;
        activeSession = (PlaySession)bundle.getSerializable(PlaySession.PASSED_SESSION);
        setContentView(R.layout.activity_play_encounter);
        adventureEncounter = activeSession.getAdventureEncounter();
        PartyNameTv = findViewById(R.id.PartyNameTv);
        EncounterNameTv = findViewById(R.id.EncounterNameTv);
        RoundNumTv = findViewById(R.id.RoundNumTv);
        NextUpNameTv = findViewById(R.id.NextUpNameTv);
        TurnFinishedGroup = findViewById(R.id.TurnFinishedGroup);
        ContinueTurnGroup = findViewById(R.id.ContinueTurnGroup);
        ActorListRv = findViewById(R.id.ActorListRv);
        encounterOverTv = findViewById(R.id.encounterOverTv);

        //do we need to fuill this in?
        if(!adventureEncounter.isSetup())
        {
            //then we will go set this up.
            setupEncounter();
        }
        else {
            updateTurn();
        }
    }

    private void updateTurn()
    {
        //fill in basics
        PartyNameTv.setText(adventureEncounter.getParty().getName());
        EncounterNameTv.setText(adventureEncounter.getEncounter().getEncounterName());
	    RoundNumTv.setText(String.format(Locale.getDefault(), "%d", adventureEncounter
			    .getTurnNumber()));
        //determine if we're in the middle of a turn
        currentTurn = adventureEncounter.nextTurn();
        if(adventureEncounter.isCompleted())
        {
            TurnFinishedGroup.setVisibility(View.GONE);
            ContinueTurnGroup.setVisibility(View.GONE);
            encounterOverTv.setVisibility(View.VISIBLE);
        }
        else if (currentTurn.isCompleted()) {
            //in this case set up for the next turn.
            TurnFinishedGroup.setVisibility(View.VISIBLE);
            ContinueTurnGroup.setVisibility(View.GONE);

        } else {
            //in this case Allow to continue
            TurnFinishedGroup.setVisibility(View.GONE);
            ContinueTurnGroup.setVisibility(View.VISIBLE);
            NextUpNameTv.setText(currentTurn.getCurrentActor().getName());
        }
        //actors
        AdventureActorsListAdapter adventureActorsListAdapter = new AdventureActorsListAdapter(getApplicationContext());
        adventureActorsListAdapter.setActorList(adventureEncounter.getActors());
        ActorListRv.setAdapter(adventureActorsListAdapter);
        ActorListRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void setupEncounter()
    {
        Intent myIntent = new Intent(PlayEncounter.this, SetupEncounter.class);
        myIntent.putExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER, adventureEncounter);
        startActivityForResult(myIntent, SetupEncounter.SETUP_ADVENTURE_ENCOUNTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SetupEncounter.SETUP_ADVENTURE_ENCOUNTER && resultCode == RESULT_OK)
        {
	        assert data != null;
            adventureEncounter = (AdventureEncounter)data.getSerializableExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
            Log.d("PlayEncounter", "Encounter: " + adventureEncounter);

            if(!adventureEncounter.isSetup())
                setupEncounter();
            activeSession.updateAdventureEncounter(adventureEncounter);
            activeSession.saveSession(getApplicationContext());
            updateTurn();
        }
        else if((requestCode == PlayerTurn.PLAYER_TURN || requestCode == MonsterTurn.MONSTER_TURN) && resultCode == RESULT_OK)
        {
	        assert data != null;
            adventureEncounter = (AdventureEncounter)data.getSerializableExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
            activeSession.updateAdventureEncounter(adventureEncounter);
            activeSession.saveSession(getApplicationContext());
            playEncounter();
        }
    }

    public void continueEncounter(View v)
    {
        playEncounter();
    }

    protected void playEncounter()
    {
        Log.d("PlayEncounter", "Encounter: " + adventureEncounter);
        if(!adventureEncounter.isCompleted())
        {
            currentTurn = adventureEncounter.nextTurn();
            if(!currentTurn.isCompleted())
            {
                AdventureEncounterActor currentActor = currentTurn.getCurrentActor();
                if (currentActor.getActorType() == AdventureEncounterActor.PLAYER_ACTOR)
                {
                    Intent myIntent = new Intent(PlayEncounter.this, PlayerTurn.class);
                    myIntent.putExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER, adventureEncounter);
                    startActivityForResult(myIntent, PlayerTurn.PLAYER_TURN);
                }
                else if (currentActor.getActorType() == AdventureEncounterActor.MONSTER_ACTOR)
                {
                    Intent myIntent = new Intent(PlayEncounter.this, MonsterTurn.class);
                    myIntent.putExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER, adventureEncounter);
                    startActivityForResult(myIntent, MonsterTurn.MONSTER_TURN);
                }
            }
            else
                updateTurn();
        }
        else
            updateTurn();
    }

    public void nextTurn(View v)
    {
        adventureEncounter.finishTurn();
        playEncounter();
    }

    public void finishEncounter(View v)
    {
        activeSession.completeCurrentEncounter();
        Intent data = new Intent();
        data.putExtra(PlaySession.PASSED_SESSION, activeSession);
        setResult(RESULT_OK, data);
        finish();
    }
}
