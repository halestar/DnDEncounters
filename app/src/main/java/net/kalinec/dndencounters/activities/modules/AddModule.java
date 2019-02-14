package net.kalinec.dndencounters.activities.modules;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.encounters.EditEncounter;
import net.kalinec.dndencounters.activities.encounters.SelectEncounter;
import net.kalinec.dndencounters.activities.encounters.SelectEncounters;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.encounters.EncounterManagerListAdapter;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.modules.Module;
import net.kalinec.dndencounters.modules.Modules;

import java.util.ArrayList;

public class AddModule extends DnDEncountersActivity
{
	public static final int REQUEST_NEW_MODULE = 120;
	private EditText ModuleNameEt, ModuleDescriptionEt, TierEt, TargetLvEt;
	private RecyclerView EncounterListRv;
	private ArrayList<Encounter> encounters;
	private EncounterManagerListAdapter encounterAdapter;
	private int encounterPos = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_add_module);
		ModuleNameEt = findViewById(R.id.ModuleNameEt);
		ModuleDescriptionEt = findViewById(R.id.ModuleDescriptionEt);
		TierEt = findViewById(R.id.TierEt);
		TargetLvEt = findViewById(R.id.TargetLvEt);
		EncounterListRv = findViewById(R.id.EncounterListRv);

		encounters = new ArrayList<>();
		encounterAdapter = new EncounterManagerListAdapter(getApplicationContext(),
               new RvClickListener()
               {
                   @Override
                   public void onClick(View view,
                                       int position)
                   {
                   	    encounterPos = position;
                   	    Intent myIntent = new Intent(AddModule.this, EditEncounter.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable(Encounter.PASSED_ENCOUNTER, encounters.get(position));
						myIntent.putExtras(bundle);
						startActivityForResult(myIntent, EditEncounter.REQUEST_UPDATED_ENCOUNTER);
                   }
               },
               new RvClickListener()
				{
					@Override
					public void onClick(View view, int position)
					{
						encounters.remove(position);
						encounterAdapter.setEncounterList(encounters);
					}
				});
		encounterAdapter.setEncounterList(encounters);
		EncounterListRv.setAdapter(encounterAdapter);
		EncounterListRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

		Button AddEncounterBtn = findViewById(R.id.AddEncounterBtn);
		AddEncounterBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent myIntent = new Intent(AddModule.this, SelectEncounters.class);
				startActivityForResult(myIntent, SelectEncounter.REQUEST_SELECT_ENCOUNTER);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		if(requestCode == SelectEncounter.REQUEST_SELECT_ENCOUNTER && resultCode == RESULT_OK)
		{
			assert data != null;
			ArrayList<Encounter> selectedEncounters = (ArrayList<Encounter>)data.getSerializableExtra(Encounter.PASSED_ENCOUNTERS);
			encounters.addAll(selectedEncounters);
			encounterAdapter.setEncounterList(encounters);
		}
		else if(requestCode == EditEncounter.REQUEST_UPDATED_ENCOUNTER && resultCode == RESULT_OK)
		{
			assert data != null;
			Encounter updatedEncounter = (Encounter)data.getSerializableExtra(Encounter.PASSED_ENCOUNTER);
			if(encounterPos != -1)
				encounters.set(encounterPos, updatedEncounter);
			encounterAdapter.setEncounterList(encounters);
		}
	}

	public void addModule(View v)
	{
		int tier, targetLv;
		if(ModuleNameEt.getText().toString() == "")
		{
			Toast toast = Toast.makeText(getApplicationContext(), "You must enter a module name", Toast.LENGTH_LONG);
			toast.show();
			return;
		}
		if(TierEt.getText().toString() == "")
		{
			Toast toast = Toast.makeText(getApplicationContext(), "You must enter a tier number", Toast.LENGTH_LONG);
			toast.show();
			return;
		}
		try
		{
			tier = Integer.parseInt(TierEt.getText().toString());
		}
		catch(NumberFormatException e)
		{
			Toast toast = Toast.makeText(getApplicationContext(), "You must enter a tier number", Toast.LENGTH_LONG);
			toast.show();
			return;
		}
		if(TargetLvEt.getText().toString() == "")
		{
			Toast toast = Toast.makeText(getApplicationContext(), "You must enter a target level", Toast.LENGTH_LONG);
			toast.show();
			return;
		}
		try
		{
			targetLv = Integer.parseInt(TargetLvEt.getText().toString());
		}
		catch(NumberFormatException e)
		{
			Toast toast = Toast.makeText(getApplicationContext(), "You must enter a target level", Toast.LENGTH_LONG);
			toast.show();
			return;
		}
		Module newModule = new Module(ModuleNameEt.getText().toString());
		newModule.setModuleDescription(ModuleDescriptionEt.getText().toString());
		newModule.setTier(tier);
		newModule.setOptimizedLevel(targetLv);
		newModule.setEncounters(encounters);
		Modules.addModule(getApplicationContext(), newModule);

		setResult(RESULT_OK);
		finish();
	}
}
