package net.kalinec.dndencounters.activities.adventure_encounters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import net.kalinec.dndencounters.R;

public class AssignPlayerInitiative extends AppCompatActivity
{
	private RecyclerView PlayerInitiativeRv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assign_player_initiative);
	}
}
