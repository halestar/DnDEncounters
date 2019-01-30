package net.kalinec.dndencounters.activities.adventure_encounters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.MainActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.characters.PcCentral;
import net.kalinec.dndencounters.activities.encounters.AddEncounter;
import net.kalinec.dndencounters.activities.encounters.PlayEncounter;
import net.kalinec.dndencounters.activities.encounters.SelectEncounter;
import net.kalinec.dndencounters.activities.encounters.SelectEncounters;
import net.kalinec.dndencounters.activities.modules.AddModule;
import net.kalinec.dndencounters.activities.modules.SelectModule;
import net.kalinec.dndencounters.activities.parties.CreateParty;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.modules.Module;
import net.kalinec.dndencounters.parties.Party;
import net.kalinec.dndencounters.playsessions.PlaySession;
import net.kalinec.dndencounters.playsessions.PlaySessionManager;
import net.kalinec.dndencounters.playsessions.PlayingEncounterListAdapter;
import net.kalinec.dndencounters.playsessions.SimpleEncounterListAdapter;

import java.util.List;
import java.util.Locale;

public class PlayAdventure extends DnDEncountersActivity {

    public static final int PLAY_ADVENTURE = 80;
    private PlaySession activeSession;
    private TextView aplTxt, NumMembersTxt, PlayAdventurePartyNameTv, ModuleNameTv;
    private PlayingEncounterListAdapter playingEncounterListAdapter;
    private SimpleEncounterListAdapter simpleEncounterListAdapter;
    private Button ModuleAssignBt;

    private void hasModule()
    {
        ModuleNameTv.setText(activeSession.getModule().getModuleName());
        ModuleAssignBt.setText(R.string.fa_times_circle);
        ModuleAssignBt.setTextColor(getResources().getColor(R.color.colorAccent, null));
    }

    private void noModule()
    {
        ModuleNameTv.setText(R.string.NoneTxt);
        ModuleAssignBt.setText(R.string.fa_plus_circle);
        ModuleAssignBt.setTextColor(getResources().getColor(R.color.colorPrimaryDark, null));
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //get the active session
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        activeSession = (PlaySession)bundle.getSerializable(PlaySession.PASSED_SESSION);
        activeSession.beginSession(getApplicationContext());

        setContentView(R.layout.activity_play_adventure);
        //start setting up the info.  Party information first.
        PlayAdventurePartyNameTv = findViewById(R.id.PlayAdventurePartyNameTv);
        PlayAdventurePartyNameTv.setText(activeSession.getPlayers().getName());
        aplTxt = findViewById(R.id.aplTxt);
        aplTxt.setText(String.format(Locale.getDefault(), "%d", activeSession.getPlayers()
                .getApl()));
        NumMembersTxt = findViewById(R.id.NumMembersTxt);
        NumMembersTxt.setText(String.format(Locale.getDefault(), "%d", activeSession.getPlayers()
                .getMembers().size()));

        //Next, set up the module info.
        ModuleNameTv = findViewById(R.id.ModuleNameTv);
        ModuleAssignBt = findViewById(R.id.ModuleAssignBt);
        if(activeSession.hasModule())
            hasModule();
        else
            noModule();

        //current encounter
        Button ContinueEncounterBt = findViewById(R.id.ContinueEncounterBt);
        TextView EncounterPlayedTv = findViewById(R.id.EncounterPlayedTv);
        if(activeSession.encounterInProgress())
        {
            EncounterPlayedTv.setText(activeSession.getCurrentEncounter().getEncounterName());
            ContinueEncounterBt.setVisibility(View.VISIBLE);
        }
        else
        {
            EncounterPlayedTv.setText(R.string.NoneTxt);
            ContinueEncounterBt.setVisibility(View.INVISIBLE);
        }


        //Encounter Queue
        playingEncounterListAdapter = new PlayingEncounterListAdapter(getApplicationContext(), new RvClickListener() {
            @Override
            public void onClick(View view, int position) {

                Encounter e = activeSession.getEncounters().get(position);
                if(view.getId() == R.id.playEncounterBtn)
                    playEncounter(e);
                else if(view.getId() == R.id.removeEncounterBtn)
                    removePlayingEncounter(e);
            }
        });
        playingEncounterListAdapter.setEncounterList(activeSession.getEncounters());
        RecyclerView adventureEncountersRv = findViewById(R.id.AdventureEncountersRv);
        adventureEncountersRv.setAdapter(playingEncounterListAdapter);
        adventureEncountersRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //completed encounter queue
        RecyclerView completedEncountersRv = findViewById(R.id.CompletedEncountersRv);
        simpleEncounterListAdapter = new SimpleEncounterListAdapter(getApplicationContext(), new RvClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        });
        simpleEncounterListAdapter.setEncounterList(activeSession.getCompletedEncounters());
        completedEncountersRv.setAdapter(simpleEncounterListAdapter);
        completedEncountersRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    public void changeParty(View v)
    {
        Intent myIntent = new Intent(PlayAdventure.this, CreateParty.class);
        myIntent.putExtra(PlaySession.PASSED_SESSION, activeSession);
        startActivityForResult(myIntent, CreateParty.REQUEST_NEW_PARTY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CreateParty.REQUEST_NEW_PARTY && resultCode == RESULT_OK)
        {
            assert data != null;
            Party newParty = (Party)data.getSerializableExtra(Party.PASSED_PARTY);

            activeSession.setPlayers(newParty);
            activeSession.saveSession(getApplicationContext());
            aplTxt.setText(String.format(Locale.getDefault(), "%d", activeSession.getPlayers()
                    .getApl()));
            NumMembersTxt.setText(String.format(Locale.getDefault(), "%d", activeSession
                    .getPlayers().getMembers().size()));
            PlayAdventurePartyNameTv.setText(activeSession.getPlayers().getName());
        }
        else if(requestCode == SelectEncounters.REQUEST_ENCOUNTER_LIST && resultCode == RESULT_OK)
        {
            assert data != null;
            List<Encounter> newEncounters = (List<Encounter>) data
                    .getSerializableExtra(Encounter.PASSED_ENCOUNTERS);
            activeSession.addEncounters(newEncounters);
            playingEncounterListAdapter.setEncounterList(activeSession.getEncounters());
            activeSession.saveSession(getApplicationContext());
        }
        else if(requestCode == SelectEncounter.REQUEST_SELECT_ENCOUNTER && resultCode == RESULT_OK)
        {
            assert data != null;
            Encounter encounter = (Encounter)data.getSerializableExtra(Encounter.PASSED_ENCOUNTER);
            activeSession.setCurrentEncounter(encounter);
            activeSession.saveSession(getApplicationContext());
            playEncounter(encounter);
        }
        else if(requestCode == PlayEncounter.PLAY_ENCOUNTER && resultCode == RESULT_OK)
        {
            assert data != null;
            activeSession = (PlaySession)data.getSerializableExtra(PlaySession.PASSED_SESSION);
            activeSession.saveSession(getApplicationContext());
            simpleEncounterListAdapter.setEncounterList(activeSession.getCompletedEncounters());
        }
        else if(requestCode == AddEncounter.REQUEST_NEW_ENCOUNTER && resultCode == RESULT_OK)
        {
            assert data != null;
            Encounter newEncounter = (Encounter)data.getSerializableExtra(Encounter.PASSED_ENCOUNTER);
            activeSession.setCurrentEncounter(newEncounter);
            activeSession.saveSession(getApplicationContext());
            playEncounter(newEncounter);
        }
        else if(requestCode == SelectModule.REQUEST_EXISTING_MODULE && resultCode == RESULT_OK)
        {
	        assert data != null;
	        Module selectedModule = (Module)data.getSerializableExtra(Module.PASSED_MODULE);
            activeSession.setModule(selectedModule);
		    activeSession.saveSession(getApplicationContext());
	        playingEncounterListAdapter.setEncounterList(activeSession.getEncounters());
		    hasModule();
        }
    }

    public void removePlayingEncounter(Encounter e)
    {
        activeSession.removeEncounter(e);
        playingEncounterListAdapter.setEncounterList(activeSession.getEncounters());
        activeSession.saveSession(getApplicationContext());
    }

    public void addEncounters(View v)
    {
        Intent myIntent = new Intent(PlayAdventure.this, SelectEncounters.class);
        startActivityForResult(myIntent, SelectEncounters.REQUEST_ENCOUNTER_LIST);
    }

	public void viewParty(View v)
	{
		Intent myIntent = new Intent(PlayAdventure.this, PcCentral.class);
		myIntent.putExtra(PlaySession.PASSED_SESSION, activeSession);
		PlayAdventure.this.startActivity(myIntent);
	}

    public void playEncounter(Encounter e)
    {
        activeSession.setCurrentEncounter(e);
        Intent myIntent = new Intent(PlayAdventure.this, PlayEncounter.class);
        myIntent.putExtra(PlaySession.PASSED_SESSION, activeSession);
        startActivityForResult(myIntent, PlayEncounter.PLAY_ENCOUNTER);
    }

    public void selectEncounterToPlay(View v)
    {
        Intent myIntent = new Intent(PlayAdventure.this, SelectEncounter.class);
        startActivityForResult(myIntent, SelectEncounter.REQUEST_SELECT_ENCOUNTER);
    }

    public void continueEncounter(View v)
    {
        Intent myIntent = new Intent(PlayAdventure.this, PlayEncounter.class);
        myIntent.putExtra(PlaySession.PASSED_SESSION, activeSession);
        startActivityForResult(myIntent, PlayEncounter.PLAY_ENCOUNTER);
    }
    
    public void finishAdventure(View v)
    {
        activeSession.saveSession(getApplicationContext());
        PlaySessionManager.completeCurrentSession(getApplicationContext());
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        finish();
    }

    public void createEncounter(View v)
    {
        Intent myIntent = new Intent(PlayAdventure.this, AddEncounter.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(AddEncounter.WRITE_ENCOUNTER, true);
        myIntent.putExtras(bundle);
        startActivityForResult(myIntent, AddEncounter.REQUEST_NEW_ENCOUNTER);
    }

    public void toggleModule(View v)
    {
    	if(activeSession.hasModule())
	    {
	    	//we have a module, so we requested to remove it.
		    activeSession.removeModule();
		    activeSession.saveSession(getApplicationContext());
		    playingEncounterListAdapter.setEncounterList(activeSession.getEncounters());
		    noModule();
	    }
	    else
	    {
		    Intent myIntent = new Intent(PlayAdventure.this, SelectModule.class);
		    startActivityForResult(myIntent, SelectModule.REQUEST_EXISTING_MODULE);
	    }
    }
}
