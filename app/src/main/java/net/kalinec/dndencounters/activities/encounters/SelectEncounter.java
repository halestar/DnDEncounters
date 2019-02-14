package net.kalinec.dndencounters.activities.encounters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.encounters.EncounterListAdapter;
import net.kalinec.dndencounters.encounters.Encounters;
import net.kalinec.dndencounters.lib.RvClickListener;

import java.util.ArrayList;
import java.util.List;

public class SelectEncounter extends DnDEncountersActivity {
    public static final int REQUEST_SELECT_ENCOUNTER = 61;
    private List<Encounter> encounters, currentEncounterList;
    private EncounterListAdapter encounterListAdapter;
    private RecyclerView encounterSearchRv;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_encounter);

        encounters = currentEncounterList = Encounters.getAllEncounters(getApplicationContext());
        encounterListAdapter = new EncounterListAdapter(getApplicationContext(), new RvClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                Encounter e = currentEncounterList.get(position);
                selectEncounter(e);
            }
        });
        encounterListAdapter.setEncounterList(encounters);
		
		
		encounterSearchRv = findViewById(R.id.encounterSearchRv);
        encounterSearchRv.setAdapter(encounterListAdapter);
        encounterSearchRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		
		
		SearchView encounterSearchSv = findViewById(R.id.encounterSearchSv);
        encounterSearchSv.setActivated(true);
        encounterSearchSv.onActionViewExpanded();
        encounterSearchSv.setIconified(false);
        encounterSearchSv.clearFocus();
        encounterSearchSv.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            private List<Encounter> filter(List<Encounter> models, String query) {
                final String lowerCaseQuery = query.toLowerCase();

                final List<Encounter> filteredModelList = new ArrayList<>();
                for (Encounter model : models) {
                    final String text = model.getEncounterName().toLowerCase();
                    if (text.contains(lowerCaseQuery)) {
                        filteredModelList.add(model);
                    }
                }
                return filteredModelList;
            }

            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                currentEncounterList = filter(encounters, newText);
                encounterListAdapter.setEncounterList(currentEncounterList);
                encounterSearchRv.scrollToPosition(0);
                return true;
            }
        });
        
        FloatingActionButton addEncounterBt = findViewById(R.id.addEncounterBt);
        addEncounterBt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
	            Intent myIntent = new Intent(SelectEncounter.this, AddEncounter.class);
	            startActivityForResult(myIntent, AddEncounter.REQUEST_NEW_ENCOUNTER);
            }
        });
    }

    public void selectEncounter(Encounter encounter)
    {
        Intent data = new Intent();
        data.putExtra(Encounter.PASSED_ENCOUNTER, encounter);
        setResult(RESULT_OK, data);
        finish();
    }
	
	@Override
	protected void onActivityResult(
			int requestCode, int resultCode, @Nullable Intent data
	                               )
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == AddEncounter.REQUEST_NEW_ENCOUNTER && resultCode == RESULT_OK)
		{
			assert data != null;
			Encounter newEncounter = (Encounter)data.getSerializableExtra(Encounter.PASSED_ENCOUNTER);
			selectEncounter(newEncounter);
		}
	}
}
