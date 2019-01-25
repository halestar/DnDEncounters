package net.kalinec.dndencounters.activities.modules;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SearchView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.encounters.AddEncounter;
import net.kalinec.dndencounters.activities.encounters.EditEncounter;
import net.kalinec.dndencounters.activities.encounters.ViewEncounters;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.encounters.EncounterListAdapter;
import net.kalinec.dndencounters.encounters.Encounters;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.modules.Module;
import net.kalinec.dndencounters.modules.ModuleListAdapter;
import net.kalinec.dndencounters.modules.Modules;

import java.util.ArrayList;
import java.util.List;

public class ListModules extends DnDEncountersActivity
{
	private List<Module> modules, currentModuleList;
	private ModuleListAdapter moduleAdapter;
	private RecyclerView ModuleRv;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_modules);

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent myIntent = new Intent(ListModules.this, AddModule.class);
				startActivityForResult(myIntent, AddModule.REQUEST_NEW_MODULE);
			}
		});

		modules = currentModuleList = Modules.getAllModules(getApplicationContext());
		moduleAdapter = new ModuleListAdapter(getApplicationContext(), new RvClickListener()
		{
			@Override
			public void onClick(View view, int position)
			{
				Module selectedModule = moduleAdapter.get(position);
				Intent myIntent = new Intent(ListModules.this, EditModule.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Module.PASSED_MODULE, selectedModule);
				myIntent.putExtras(bundle);
				startActivityForResult(myIntent, EditModule.REQUEST_UPDATED_MODULE);
			}
		});
		moduleAdapter.setModuleList(modules);

		ModuleRv = findViewById(R.id.ModuleRv);
		ModuleRv.setAdapter(moduleAdapter);
		ModuleRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

		SearchView ModuleSv = findViewById(R.id.ModuleSv);
		ModuleSv.setActivated(true);
		ModuleSv.onActionViewExpanded();
		ModuleSv.setIconified(false);
		ModuleSv.clearFocus();
		ModuleSv.setOnQueryTextListener(new SearchView.OnQueryTextListener()
		{
			private List<Module> filter(List<Module> models, String query) {
				final String lowerCaseQuery = query.toLowerCase();

				final List<Module> filteredModelList = new ArrayList<>();
				for (Module model : models) {
					final String text = model.getModuleName().toLowerCase();
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
				currentModuleList = filter(modules, newText);
				moduleAdapter.setModuleList(currentModuleList);
				ModuleRv.scrollToPosition(0);
				return true;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		modules = currentModuleList = Modules.getAllModules(getApplicationContext());
		moduleAdapter.setModuleList(modules);
	}
}
