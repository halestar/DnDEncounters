package net.kalinec.dndencounters;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.encounters.PlayingEncounterListAdapter;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.parties.Party;
import net.kalinec.dndencounters.players.Player;
import net.kalinec.dndencounters.playsessions.PlaySession;

import java.util.List;

public class PlayAdventure extends AppCompatActivity {

    private PlaySession activeSession;
    private TextView aplTxt, NumMembersTxt, PlayAdventurePartyNameTv;
    private Button continueLastEncounterBtn, PlaySingleEncounterBtn, AddEncounterToAdventureBtn, FinishAdventureBtn;
    private RecyclerView AdventureEncountersRv, CompletedEncountersRv;
    private PlayingEncounterListAdapter playingEncounterListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_adventure);
        Bundle bundle = getIntent().getExtras();
        activeSession = (PlaySession)bundle.getSerializable(PlaySession.PASSED_SESSION);
        activeSession.beginSession(getApplicationContext());

        PlayAdventurePartyNameTv = findViewById(R.id.PlayAdventurePartyNameTv);
        PlayAdventurePartyNameTv.setText(activeSession.getPlayers().getName());
        aplTxt = findViewById(R.id.aplTxt);
        aplTxt.setText(Integer.toString(activeSession.getPlayers().getApl()));
        NumMembersTxt = findViewById(R.id.NumMembersTxt);
        NumMembersTxt.setText(Integer.toString(activeSession.getPlayers().getMembers().size()));

        playingEncounterListAdapter = new PlayingEncounterListAdapter(getApplicationContext(), new RvClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(view.getId() == R.id.playEncounterBtn)
                {

                }
                else if(view.getId() == R.id.removeEncounterBtn)
                {
                    Encounter e = activeSession.getEncounters().get(position);
                    removePlayingEncounter(e);
                }
            }
        });
        playingEncounterListAdapter.setEncounterList(activeSession.getEncounters());
        AdventureEncountersRv = findViewById(R.id.AdventureEncountersRv);
        AdventureEncountersRv.setAdapter(playingEncounterListAdapter);
        AdventureEncountersRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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
        if(requestCode == CreateParty.REQUEST_NEW_PARTY)
        {
            if(resultCode == RESULT_OK)
            {
                Party newParty = (Party)data.getSerializableExtra(Party.PASSED_PARTY);

                activeSession.setPlayers(newParty);
                activeSession.saveSession(getApplicationContext());
                aplTxt.setText(Integer.toString(activeSession.getPlayers().getApl()));
                NumMembersTxt.setText(Integer.toString(activeSession.getPlayers().getMembers().size()));
                PlayAdventurePartyNameTv.setText(activeSession.getPlayers().getName());
            }
        }
        else if(requestCode == SelectEncounters.REQUEST_ENCOUNTER_LIST)
        {
            try {
                List<Encounter> newEncounters = (List<Encounter>) data.getSerializableExtra(Encounter.PASSED_ENCOUNTERS);
                activeSession.addEncounters(newEncounters);
                playingEncounterListAdapter.setEncounterList(activeSession.getEncounters());
                activeSession.saveSession(getApplicationContext());
            }
            catch(NullPointerException e)
            {

            }
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

}