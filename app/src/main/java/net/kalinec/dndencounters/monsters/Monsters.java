package net.kalinec.dndencounters.monsters;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Monsters
{
	private static JSONArray monsterDb;
	
	private static String loadJSONFromAsset(Context context)
	{
		String json;
		try
		{
			InputStream is = context.getAssets().open("dnd_monsters.json");
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
		if(monsterDb == null)
		{
			try
			{
				monsterDb = new JSONArray(loadJSONFromAsset(context));
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				monsterDb = null;
			}
		}
		return (monsterDb != null);
	}
	
	public static List<Monster> monsterList(Context context)
	{
		if(verifyDb(context))
		{
			ArrayList<Monster> allMonsters = new ArrayList<>();
			for(int i = 0; i < monsterDb.length(); i++)
			{
				try
				{
					allMonsters.add(new Monster(i, monsterDb.getJSONObject(i)));
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			return allMonsters;
			
		}
		return new ArrayList<>();
	}
	
	
}
