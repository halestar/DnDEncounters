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
import net.kalinec.dndencounters.activities.adventure_encounters.UpdateAdventureEncounter;
import net.kalinec.dndencounters.activities.adventure_encounters.UpdatePlayerInitiative;
import net.kalinec.dndencounters.activities.encounter_turn.MonsterTurn;
import net.kalinec.dndencounters.activities.encounter_turn.PlayerTurn;
import net.kalinec.dndencounters.adventure_encounters.AdventureActorsListAdapter;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterActor;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterPlayer;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterTurn;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.players.Players;
import net.kalinec.dndencounters.playsessions.PlaySession;

import java.util.ArrayList;
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
    private AdventureActorsListAdapter adventureActorsListAdapter;

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
    	adventureEncounter = activeSession.getAdventureEncounter();
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
        adventureActorsListAdapter = new AdventureActorsListAdapter(
                getApplicationContext(), new RvClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                AdventureEncounterActor actor = adventureActorsListAdapter.get(position);
                Intent myIntent = new Intent(PlayEncounter.this, EditEncounterActor.class);
                myIntent.putExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER, adventureEncounter);
                myIntent.putExtra(AdventureEncounterActor.PASSED_ACTOR, actor);
                startActivityForResult(myIntent, EditEncounterActor.REQUEST_UPDATED_ACTOR);
            }
        });
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
        }
        else if((requestCode == PlayerTurn.PLAYER_TURN || requestCode == MonsterTurn.MONSTER_TURN) && resultCode == RESULT_OK)
        {
	        assert data != null;
            adventureEncounter = (AdventureEncounter)data.getSerializableExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
            activeSession.updateAdventureEncounter(adventureEncounter);
            activeSession.saveSession(getApplicationContext());
            playEncounter();
        }
        else if(requestCode == UpdatePlayerInitiative.REQUEST_UPDATED_PLAYER_INITIATIVE && resultCode == RESULT_OK)
        {
	        assert data != null;
	        ArrayList<AdventureEncounterPlayer> encounterPlayers = (ArrayList<AdventureEncounterPlayer>)data.getSerializableExtra(AdventureEncounterPlayer.PASSED_ENCOUNTER_PLAYERS);
	        for(AdventureEncounterPlayer pc: encounterPlayers)
	        {
		        adventureEncounter.updateActor(pc);
		        Players.updatePc(getApplicationContext(), pc.getPc());
	        }
	        //check for removed pcs
	        for(AdventureEncounterPlayer pc: adventureEncounter.getAvailablePlayers())
	        {
		        if(encounterPlayers.indexOf(pc) == -1)
			        adventureEncounter.getActors().remove(pc);
	        }
	        adventureEncounter.getCurrentTurn().recalculateInitiative();
	        activeSession.updateAdventureEncounter(adventureEncounter);
	        activeSession.saveSession(getApplicationContext());
        }
        else if(requestCode == UpdateAdventureEncounter.REQUEST_UPDATED_ENCOUNTER && resultCode == RESULT_OK)
        {
	        assert data != null;
	        ArrayList<AdventureEncounterMonster> encounterMonsters = (ArrayList<AdventureEncounterMonster>)data.getSerializableExtra(AdventureEncounterMonster.PASSED_ENCOUNTER_MONSTERS);
	        for(AdventureEncounterMonster monster: encounterMonsters)
		        adventureEncounter.updateActor(monster);
	        //check for removed monsters
	        for(AdventureEncounterMonster monster: adventureEncounter.getAllAvailableMonsters())
	        {
		        if(encounterMonsters.indexOf(monster) == -1)
			        adventureEncounter.getActors().remove(monster);
	        }
	        adventureEncounter.getCurrentTurn().recalculateInitiative();
	        activeSession.updateAdventureEncounter(adventureEncounter);
	        activeSession.saveSession(getApplicationContext());
        }
        else if(requestCode == EditEncounterActor.REQUEST_UPDATED_ACTOR && resultCode == RESULT_OK)
        {
	        assert data != null;
	        AdventureEncounter updatedAdventureEncounter = (AdventureEncounter)data.getSerializableExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
	        updatedAdventureEncounter.getCurrentTurn().recalculateInitiative();
			activeSession.updateAdventureEncounter(updatedAdventureEncounter);
	        activeSession.saveSession(getApplicationContext());
        }
	    updateTurn();
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
    
    public void updatePlayerInitiative(View v)
    {
	    Intent myIntent = new Intent(PlayEncounter.this, UpdatePlayerInitiative.class);
	    myIntent.putExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER, adventureEncounter);
	    startActivityForResult(myIntent, UpdatePlayerInitiative.REQUEST_UPDATED_PLAYER_INITIATIVE);
    }
	
	public void updateAdventureEncounter(View v)
	{
		Intent myIntent = new Intent(PlayEncounter.this, UpdateAdventureEncounter.class);
		myIntent.putExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER, adventureEncounter);
		startActivityForResult(myIntent, UpdateAdventureEncounter.REQUEST_UPDATED_ENCOUNTER);
	}
}
