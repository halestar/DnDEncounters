package net.kalinec.dndencounters;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.kalinec.dndencounters.encounters.EncounterMonsterListAdapter;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.monsters.Monsters;
import net.kalinec.dndencounters.players.Player;

import org.apache.commons.math3.fraction.Fraction;

import java.util.ArrayList;
import java.util.List;

public class AddEncounter extends AppCompatActivity
{
	private List<Monster> monsters;
	private EditText encounterName;
	private TextView encounterCr;
	private RecyclerView encounterMonstersRv;
	private EncounterMonsterListAdapter encounterMonsterListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_encounter);
		
		encounterName = findViewById(R.id.addEncounterNameEt);
		encounterCr = findViewById(R.id.encounterCrDisplayTv);
		encounterCr.setText("0");
		encounterMonstersRv = findViewById(R.id.encounterMonstersRv);
		monsters = new ArrayList<>();
		
		encounterMonsterListAdapter = new EncounterMonsterListAdapter(getApplicationContext(), new RvClickListener()
		{
			@Override
			public void onClick(View view, int position)
			{
				Monster selectedMonster = monsters.get(position);
				if(view.getId() == R.id.removeMonsterBtn)
					removeMonsterFromEncounter(selectedMonster);
				else if(view.getId() == R.id.dupeMonsterBtn)
					addMonsterToEncounter(selectedMonster);
			}
		});
		
		encounterMonstersRv.setAdapter(encounterMonsterListAdapter);
		encounterMonstersRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
	}
	
	public void addMonster(View v)
	{
		Intent myIntent = new Intent(AddEncounter.this, SelectMonster.class);
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
				Fraction f = new Fraction(Integer.parseInt(fractionCr[0]), Integer.parseInt(fractionCr[1]));
				cr += f.doubleValue();
			}
		}
		cr = Math.ceil(cr);
		encounterCr.setText(Double.toString(cr));
	}
	
	private void addMonsterToEncounter(Monster m)
	{
		monsters.add(m);
		encounterMonsterListAdapter.add(m);
		updateCr();
	}
	
	private void removeMonsterFromEncounter(Monster m)
	{
		monsters.remove(m);
		encounterMonsterListAdapter.remove(m);
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
				addMonsterToEncounter(selectedMonster);
			}
		}
	}
	
	public void adddEncounter()
	{
		
	}
}
