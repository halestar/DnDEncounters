package net.kalinec.dndencounters.activities.modules;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.modules.Module;
import net.kalinec.dndencounters.modules.ModuleListAdapter;
import net.kalinec.dndencounters.modules.Modules;
import net.kalinec.dndencounters.monsters.Monster;

import java.util.ArrayList;
import java.util.List;

public class SelectModule extends DnDEncountersActivity
{
	public static final int REQUEST_EXISTING_MODULE = 125;
	private List<Module> modules, currentModuleList;
	private ModuleListAdapter moduleAdapter;
	private RecyclerView ModuleRv;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_module);

		modules = currentModuleList = Modules.getAllModules(getApplicationContext());
		moduleAdapter = new ModuleListAdapter(getApplicationContext(), new RvClickListener()
		{
			@Override
			public void onClick(View view, int position)
			{
				//click on a module to return it.
				Module selectedModule = moduleAdapter.get(position);
				Intent data = new Intent();
				data.putExtra(Module.PASSED_MODULE, selectedModule);
				setResult(RESULT_OK, data);
				finish();
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
}
