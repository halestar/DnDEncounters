package net.kalinec.dndencounters.custom_monsters;

import android.content.Context;
import android.util.Log;

import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.players.Player;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CustomMonsters
{

	private static final String fname = "custom_monsters.srl";
	private static List<CustomMonster> customMonsters = null;

	private static final Comparator<CustomMonster> ALPHABETICAL_COMPARATOR = new Comparator<CustomMonster>() {
		@Override
		public int compare(CustomMonster a, CustomMonster b) {
			return a.getUuid().compareTo(b.getUuid());
		}
	};
	
	private static List<CustomMonster> readCustomMonsters(Context context)
	{
		try
		{
			customMonsters = new ArrayList<>();
			File fin = new File(context.getFilesDir(), fname);
			if(fin.exists() && fin.length() > 0)
			{
				ObjectInputStream in = new ObjectInputStream(context.openFileInput(fname));
				Integer numEncounters = (Integer)in.readObject();
				for(int i = 0; i < numEncounters; i++)
					customMonsters.add((CustomMonster) in.readObject());
				in.close();
			}
			return customMonsters;
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	private static void writeCustomMonsters(Context context)
	{
		if(customMonsters == null)
			readCustomMonsters(context);
		else
		{
			FileOutputStream fos;
			try
			{
				fos = context.openFileOutput(fname, Context.MODE_PRIVATE);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(customMonsters.size());
				for (CustomMonster e : customMonsters)
					oos.writeObject(e);
				oos.flush();
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<CustomMonster> getCustomMonsters(Context context)
	{
		if(customMonsters == null)
			readCustomMonsters(context);
		return customMonsters;
	}

	public static void addCustomMonster(Context context, CustomMonster e)
	{
		if(customMonsters == null)
			readCustomMonsters(context);
		customMonsters.add(e);
		customMonsters.sort(ALPHABETICAL_COMPARATOR);
		writeCustomMonsters(context);
	}

	public static void updateCustomMonster(Context context, CustomMonster m)
	{
		if(customMonsters == null)
			readCustomMonsters(context);
		int pos = customMonsters.indexOf(m);
		if(pos >= 0)
			customMonsters.set(pos, m);
		else
			customMonsters.add(m);
		customMonsters.sort(ALPHABETICAL_COMPARATOR);
		writeCustomMonsters(context);
	}

	public static void removeCustomMonster(Context context, CustomMonster e)
	{
		if(customMonsters == null)
			readCustomMonsters(context);
		customMonsters.remove(e);
		customMonsters.sort(ALPHABETICAL_COMPARATOR);
		writeCustomMonsters(context);
	}

	public static CustomMonster getMonsterByUuid(Context context, String uuid)
	{
		if(customMonsters == null)
			readCustomMonsters(context);
		for(CustomMonster p: customMonsters)
		{
			if(p.getUuid().toString().equals(uuid))
				return p;
		}
		return null;
	}
}
