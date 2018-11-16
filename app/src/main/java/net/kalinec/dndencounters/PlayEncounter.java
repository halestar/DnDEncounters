package net.kalinec.dndencounters;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.kalinec.dndencounters.encounter.AdventureEncounter;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.parties.Party;
import net.kalinec.dndencounters.playsessions.PlaySession;

import java.util.List;

public class PlayEncounter extends AppCompatActivity {
    public static final int PLAY_ENCOUNTER = 60;
    private PlaySession activeSession;
    private AdventureEncounter adventureEncounter;

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
        }
    }
}
