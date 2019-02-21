package net.kalinec.dndencounters.activities.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import net.kalinec.dndencounters.R;

public class EncountersPreferenceFragment extends PreferenceFragment
{
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
