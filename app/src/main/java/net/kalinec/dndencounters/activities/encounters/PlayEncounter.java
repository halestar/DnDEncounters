package net.kalinec.dndencounters.activities.encounters;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.adventure_encounters.SetupEncounter;
import net.kalinec.dndencounters.activities.encounter_turn.PlayerTurn;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterActor;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterTurn;
import net.kalinec.dndencounters.playsessions.PlaySession;

public class PlayEncounter extends AppCompatActivity {
    public static final int PLAY_ENCOUNTER = 60;
    private PlaySession activeSession;
    private AdventureEncounter adventureEncounter;
    private AdventureEncounterTurn currentTurn;
    private AdventureEncounterActor currentActor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        activeSession = (PlaySession)bundle.getSerializable(PlaySession.PASSED_SESSION);
        setContentView(R.layout.activity_play_encounter);
        adventureEncounter = activeSession.getAdventureEncounter();
        //do we need to fuill this in?
        if(!adventureEncounter.isSetup())
        {
            //then we will go set this up.
            setupEncounter();
        }
        else
        {
            //we are ready to go
            playEncounter();
        }
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
        if(requestCode == SetupEncounter.SETUP_ADVENTURE_ENCOUNTER && resultCode == RESULT_OK)
        {
            adventureEncounter = (AdventureEncounter)data.getSerializableExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
            Log.d("PlayEncounter", "Encounter: " + adventureEncounter);

            if(!adventureEncounter.isSetup())
                setupEncounter();
            else
                playEncounter();
        }
    }

    protected void playEncounter()
    {
        Log.d("PlayEncounter", "Encounter: " + adventureEncounter);
        if(!adventureEncounter.isCompleted())
        {
            if(currentTurn == null)
                currentTurn = adventureEncounter.nextTurn();
            if(!currentTurn.isCompleted())
            {
                currentActor = currentTurn.getCurrentActor();
                if (currentActor.getActorType() == AdventureEncounterActor.PLAYER_ACTOR)
                {
                    Intent myIntent = new Intent(PlayEncounter.this, PlayerTurn.class);
                    myIntent.putExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER, adventureEncounter);
                    startActivityForResult(myIntent, PlayerTurn.PLAYER_TURN);
                }
                else if (currentActor.getActorType() == AdventureEncounterActor.MONSTER_ACTOR)
                {

                }
            }
        }
    }
}
