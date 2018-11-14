package net.kalinec.dndencounters;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.encounters.EncounterMonsterListAdapter;
import net.kalinec.dndencounters.encounters.Encounters;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.players.Player;

import org.apache.commons.math3.fraction.Fraction;

import java.util.ArrayList;
import java.util.List;

public class EditEncounter extends AppCompatActivity
{
	public static final int REQUEST_UPDATED_ENCOUNTER = 16;
	private List<Monster> monsters;
	private EditText encounterName;
	private TextView encounterCr;
	private RecyclerView encounterMonstersRv;
	private EncounterMonsterListAdapter encounterMonsterListAdapter;
	private Encounter selectedEncounter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		selectedEncounter = (Encounter) bundle.getSerializable(Encounter.PASSED_ENCOUNTER);
		Log.d("EditEncounter", "selected encounter=" + selectedEncounter);
		setContentView(R.layout.activity_edit_encounter);
		
		encounterName = findViewById(R.id.editEncounterNameEt);
		encounterCr = findViewById(R.id.encounterCrDisplayTv);
		encounterMonstersRv = findViewById(R.id.encounterMonstersRv);
		
		encounterName.setText(selectedEncounter.getEncounterName());
		encounterCr.setText(Integer.toString(selectedEncounter.getCr()));
		if(monsters == null)
			monsters = new ArrayList<>(selectedEncounter.getMonsters());
		encounterMonsterListAdapter = new EncounterMonsterListAdapter(getApplicationContext(), new RvClickListener()
		{
			@Override
			public void onClick(View view, int position)
			{
				Monster selectedMonster = encounterMonsterListAdapter.get(position);
				if(view.getId() == R.id.removeMonsterBtn)
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
			if(crStr == Monster.CR_ERROR)
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
		encounterCr.setText(Double.toString(cr));
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
		if(requestCode == SelectMonster.REQUEST_SELECT_MONSTER)
		{
			if(resultCode == RESULT_OK)
			{
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
