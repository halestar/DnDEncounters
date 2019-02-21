package net.kalinec.dndencounters.activities.preferences;

import android.preference.PreferenceActivity;

import net.kalinec.dndencounters.R;

import java.util.List;

public class EncountersPreferences extends PreferenceActivity
{
	@Override
	public void onBuildHeaders(List<Header> target)
	{
		loadHeadersFromResource(R.xml.prefs_headers, target);
	}

	@Override
	protected boolean isValidFragment(String fragmentName)
	{
		return EncountersPreferenceFragment.class.getName().equals(fragmentName);
	}
}
