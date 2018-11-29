package net.kalinec.dndencounters.spells;

import android.content.Context;

import net.kalinec.dndencounters.monsters.Monster;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Spells
{
	private static JSONArray spellDb;
	private static Context context;
	private static Spells instance;
	
	private static String loadJSONFromAsset()
	{
		String json = null;
		try
		{
			InputStream is = context.getAssets().open("spells.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return "[]";
		}
		return json;
	}
	
	private static boolean verifyDb(Context context)
	{
		if(Spells.context == null)
			Spells.context = context;
		if(spellDb == null)
		{
			try
			{
				spellDb = new JSONArray(loadJSONFromAsset());
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				spellDb = null;
			}
		}
		instance = new Spells();
		return (spellDb != null);
	}
	
	public static Spells allSpells(Context context)
	{
		if(verifyDb(context))
			return Spells.instance;
		return null;
	}
	
	public static List<Spell> spellList(Context context)
	{
		if(verifyDb(context))
		{
			ArrayList<Spell> spellList = new ArrayList<>();
			for(int i = 0; i < spellDb.length(); i++)
			{
				try
				{
					spellList.add(new Spell(spellDb.getJSONObject(i)));
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			return spellList;
			
		}
		return new ArrayList<>();
	}
	
	
	public Spell getSpell(int index)
	{
		try
		{
			return new Spell(spellDb.getJSONObject(index));
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
