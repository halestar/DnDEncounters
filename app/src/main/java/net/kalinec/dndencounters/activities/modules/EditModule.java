package net.kalinec.dndencounters.activities.modules;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.custom_monsters.EditCustomMonster;
import net.kalinec.dndencounters.activities.encounters.AddEncounter;
import net.kalinec.dndencounters.activities.encounters.EditEncounter;
import net.kalinec.dndencounters.custom_monsters.CustomMonster;
import net.kalinec.dndencounters.custom_monsters.CustomMonsters;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.encounters.EncounterManagerListAdapter;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.modules.Module;
import net.kalinec.dndencounters.modules.Modules;

import java.util.ArrayList;

public class EditModule extends DnDEncountersActivity
{
	public static final int REQUEST_UPDATED_MODULE = 122;
	private EditText ModuleNameEt, ModuleDescriptionEt, TierEt, TargetLvEt;
	private RecyclerView EncounterListRv;
	private ArrayList<Encounter> encounters;
	private EncounterManagerListAdapter encounterAdapter;
	private int encounterPos = -1;
	private Module module;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		module = (Module)bundle.getSerializable(Module.PASSED_MODULE);
		setContentView(R.layout.activity_edit_module);

		ModuleNameEt = findViewById(R.id.ModuleNameEt);
		ModuleNameEt.setText(module.getModuleName());
		ModuleDescriptionEt = findViewById(R.id.ModuleDescriptionEt);
		ModuleDescriptionEt.setText(module.getModuleDescription());
		TierEt = findViewById(R.id.TierEt);
		TierEt.setText(Integer.toString(module.getTier()));
		TargetLvEt = findViewById(R.id.TargetLvEt);
		TargetLvEt.setText(Integer.toString(module.getOptimizedLevel()));
		EncounterListRv = findViewById(R.id.EncounterListRv);

		encounters = module.getEncounters();
		encounterAdapter = new EncounterManagerListAdapter(getApplicationContext(),
                   new RvClickListener()
                   {
                       @Override
                       public void onClick(View view,
                                           int position)
                       {
                           encounterPos = position;
                           Intent myIntent = new Intent(EditModule.this, EditEncounter.class);
                           Bundle bundle = new Bundle();
                           bundle.putSerializable(Encounter.PASSED_ENCOUNTER, encounters.get(position));
                           bundle.putBoolean(EditEncounter.WRITE_ENCOUNTER, false);
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
				Intent myIntent = new Intent(EditModule.this, AddEncounter.class);
				Bundle bundle = new Bundle();
				bundle.putBoolean(AddEncounter.WRITE_ENCOUNTER, false);
				myIntent.putExtras(bundle);
				startActivityForResult(myIntent, AddEncounter.REQUEST_NEW_ENCOUNTER);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		if(requestCode == AddEncounter.REQUEST_NEW_ENCOUNTER && resultCode == RESULT_OK)
		{
			assert data != null;
			Encounter newEncounter = (Encounter)data.getSerializableExtra(Encounter.PASSED_ENCOUNTER);
			encounters.add(newEncounter);
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

	public void updateModule(View v)
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
		module.setModuleName(ModuleNameEt.getText().toString());
		module.setModuleDescription(ModuleDescriptionEt.getText().toString());
		module.setTier(tier);
		module.setOptimizedLevel(targetLv);
		module.setEncounters(encounters);
		Modules.updateModule(getApplicationContext(), module);

		Intent data = new Intent();
		data.putExtra(Module.PASSED_MODULE, module);
		setResult(RESULT_OK, data);
		finish();
	}

	public void confirmDeleteModule(View target)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(EditModule.this);
		builder.setMessage(R.string.ConfirmModuleDelete)
		       .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener()
		       {
			       @Override
			       public void onClick(DialogInterface dialog, int which)
			       {
				       deleteModule();
			       }
		       })
		       .setNegativeButton(R.string.No, new DialogInterface.OnClickListener()
		       {
			       @Override
			       public void onClick(DialogInterface dialog, int which)
			       {
				       dialog.cancel();
			       }
		       })
		       .show();
	}

	public void deleteModule()
	{
		Modules.removeModule(getApplicationContext(), module);
		setResult(RESULT_OK);
		finish();
	}
}
