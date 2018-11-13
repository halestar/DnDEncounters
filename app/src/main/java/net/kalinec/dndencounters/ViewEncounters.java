package net.kalinec.dndencounters;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SearchView;

import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.encounters.EncounterListAdapter;
import net.kalinec.dndencounters.encounters.Encounters;

import java.util.ArrayList;
import java.util.List;

public class ViewEncounters extends AppCompatActivity
{
	private List<Encounter> encounters;
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
				ViewEncounters.this.startActivity(myIntent);
			}
		});
		
		encounters = Encounters.getAllEncounters(getApplicationContext());
		encounterAdapter = new EncounterListAdapter(getApplicationContext());
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
				final List<Encounter> filteredEncounterList = filter(encounters, newText);
				encounterAdapter.setEncounterList(filteredEncounterList);
				encounterRv.scrollToPosition(0);
				return true;
			}
		});
		
	}
	
}
