package net.kalinec.dndencounters.monsters;

import android.content.Context;

import net.kalinec.dndencounters.lib.SerializableJsonObj;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Monsters
{
	private static JSONArray monsterDb;
	private static Context context;
	private static Monsters instance;
	
	private static String loadJSONFromAsset()
	{
		String json = null;
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
		if(Monsters.context == null)
			Monsters.context = context;
		if(monsterDb == null)
		{
			try
			{
				monsterDb = new JSONArray(loadJSONFromAsset());
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				monsterDb = null;
			}
		}
		instance = new Monsters();
		return (monsterDb != null);
	}
	
	public static Monsters allMonsters(Context context)
	{
		if(verifyDb(context))
			return Monsters.instance;
		return null;
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
					allMonsters.add(new Monster(i, new SerializableJsonObj(monsterDb.getJSONObject(i).toString())));
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
	
	
	public Monster getMonster(int index)
	{
		try
		{
			return new Monster(index, new SerializableJsonObj(monsterDb.getJSONObject(index).toString()));
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
