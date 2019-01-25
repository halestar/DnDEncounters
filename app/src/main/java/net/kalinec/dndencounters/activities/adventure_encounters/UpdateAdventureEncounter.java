package net.kalinec.dndencounters.activities.adventure_encounters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;

public class UpdateAdventureEncounter extends DnDEncountersActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_adventure_encounter);
	}
}
