package net.kalinec.dndencounters.activities.encounters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class ViewEncounters extends DnDEncountersActivity
{
	private List<Encounter> encounters, currentEncounterList;
	private EncounterListAdapter encounterAdapter;
	private RecyclerView encounterRv;
	private SearchView encounterSv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_encounters);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent myIntent = new Intent(ViewEncounters.this, AddEncounter.class);
				startActivityForResult(myIntent, AddEncounter.REQUEST_NEW_ENCOUNTER);
			}
		});
		
		encounters = currentEncounterList = Encounters.getAllEncounters(getApplicationContext());
		encounterAdapter = new EncounterListAdapter(getApplicationContext(), new RvClickListener()
		{
			@Override
			public void onClick(View view, int position)
			{
				Encounter selectedEncounter = encounterAdapter.get(position);
				Intent myIntent = new Intent(ViewEncounters.this, EditEncounter.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Encounter.PASSED_ENCOUNTER, selectedEncounter);
				myIntent.putExtras(bundle);
				startActivityForResult(myIntent, AddEncounter.REQUEST_NEW_ENCOUNTER);
			}
		});
		encounterAdapter.setEncounterList(encounters);
		
		encounterRv = (RecyclerView)findViewById(R.id.encounterRv);
		encounterRv.setAdapter(encounterAdapter);
		encounterRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		
		encounterSv = (SearchView)findViewById(R.id.encounterSv);
		encounterSv.setActivated(true);
		encounterSv.onActionViewExpanded();
		encounterSv.setIconified(false);
		encounterSv.clearFocus();
		encounterSv.setOnQueryTextListener(new SearchView.OnQueryTextListener()
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
				encounterAdapter.setEncounterList(currentEncounterList);
				encounterRv.scrollToPosition(0);
				return true;
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		Log.d("ViewEncounter", "in activity result");
		encounters = currentEncounterList = Encounters.getAllEncounters(getApplicationContext());
		Log.d("ViewEncounter", "encounters=" + encounters);
		encounterAdapter.setEncounterList(encounters);
		encounterRv.scrollToPosition(0);
	}
}
