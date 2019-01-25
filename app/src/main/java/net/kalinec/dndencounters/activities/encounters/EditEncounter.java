package net.kalinec.dndencounters.activities.encounters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.monsters.SelectMonster;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.encounters.EncounterMonsterListAdapter;
import net.kalinec.dndencounters.encounters.Encounters;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.monsters.Monster;

import org.apache.commons.math3.fraction.Fraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditEncounter extends DnDEncountersActivity
{
	private List<Monster> monsters;
	private EditText encounterName;
	private TextView encounterCr;
	private EncounterMonsterListAdapter encounterMonsterListAdapter;
	private Encounter selectedEncounter;
	public final static String WRITE_ENCOUNTER = "WRITE_ENCOUNTER";
	private boolean writeEncounter = true;
	public static final int REQUEST_UPDATED_ENCOUNTER = 515;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		selectedEncounter = (Encounter) bundle.getSerializable(Encounter.PASSED_ENCOUNTER);
		writeEncounter = bundle.getBoolean(WRITE_ENCOUNTER, true);
		Log.d("EditEncounter", "selected encounter=" + selectedEncounter);
		setContentView(R.layout.activity_edit_encounter);
		
		encounterName = findViewById(R.id.editEncounterNameEt);
		encounterCr = findViewById(R.id.encounterCrDisplayTv);
		RecyclerView encounterMonstersRv = findViewById(R.id.encounterMonstersRv);
		
		encounterName.setText(selectedEncounter.getEncounterName());
		encounterCr.setText(String.format(Locale.getDefault(), "%d", selectedEncounter.getCr()));
		if(monsters == null)
			monsters = new ArrayList<>(selectedEncounter.getMonsters());
		encounterMonsterListAdapter = new EncounterMonsterListAdapter(getApplicationContext(), new RvClickListener()
		{
			@Override
			public void onClick(View view, int position)
			{
				Monster selectedMonster = encounterMonsterListAdapter.get(position);
				if(view.getId() == R.id.RemoveEncounterBt)
					removeMonsterFromEncounter(selectedMonster);
				else if(view.getId() == R.id.dupeMonsterBtn)
					addMonsterToEncounter(selectedMonster);
			}
		});
		encounterMonsterListAdapter.setMonsterList(monsters);
		encounterMonstersRv.setAdapter(encounterMonsterListAdapter);
		encounterMonstersRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
	}
	
	public void addMonster(View v)
	{
		Intent myIntent = new Intent(EditEncounter.this, SelectMonster.class);
		startActivityForResult(myIntent, SelectMonster.REQUEST_SELECT_MONSTER);
	}
	
	private void updateCr()
	{
		double cr = 0;
		for(Monster m: monsters)
		{
			String crStr = m.getCr();
			if (crStr.equals(Monster.CR_ERROR))
				continue;
			String[] fractionCr = crStr.split("/");
			if(fractionCr.length == 1)
				cr += Integer.parseInt(crStr);
			else if(fractionCr.length == 2)
			{
				//fraction.
				Fraction
						f = new Fraction(Integer.parseInt(fractionCr[0]), Integer.parseInt(fractionCr[1]));
				cr += f.doubleValue();
			}
		}
		cr = Math.ceil(cr);
		encounterCr.setText(String.format(Locale.getDefault(), "%d", (int) cr));
	}
	
	private void addMonsterToEncounter(Monster m)
	{
		monsters.add(m);
		encounterMonsterListAdapter.setMonsterList(monsters);
		updateCr();
	}
	
	private void removeMonsterFromEncounter(Monster m)
	{
		monsters.remove(m);
		encounterMonsterListAdapter.setMonsterList(monsters);
		updateCr();
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == SelectMonster.REQUEST_SELECT_MONSTER)
		{
			if(resultCode == RESULT_OK)
			{
				assert data != null;
				Monster selectedMonster = (Monster)data.getSerializableExtra(Monster.PASSED_MONSTER);
				Log.d("EditEncounter", "after selected_monster got: " + selectedMonster);
				addMonsterToEncounter(selectedMonster);
			}
		}
	}
	
	public void updateEncounter(View v)
	{
		Encounter newEncounter = new Encounter();
		newEncounter.setEncounterName(encounterName.getText().toString());
		newEncounter.setMonsters(monsters);
		if(writeEncounter)
			Encounters.updateEncounter(getApplicationContext(), selectedEncounter, newEncounter);
		Intent data = new Intent();
		data.putExtra(Encounter.PASSED_ENCOUNTER, newEncounter);
		setResult(RESULT_OK, data);
		finish();
	}
	
	public void removeEncounter(View v)
	{
		Encounters.removeEncounter(getApplicationContext(), selectedEncounter);
		finish();
	}
}
