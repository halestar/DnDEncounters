package net.kalinec.dndencounters.monsters;

import android.content.Context;
import android.util.Log;

import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.lib.SelectableItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MonsterTokens
{
	private static List<MonsterToken> tokensDb;
	private static Context context;
	private static final String fname = "monster_tokens.srl";
	
	private static final Comparator<MonsterToken> ALPHABETICAL_COMPARATOR = new Comparator<MonsterToken>() {
		@Override
		public int compare(MonsterToken a, MonsterToken b) {
			return a.getTokenName().compareTo(b.getTokenName());
		}
	};
	
	
	private static void verifyDb(Context context)
	{
		MonsterTokens.context = context;
		if(tokensDb == null)
		{
			try
			{
				tokensDb = new ArrayList<>();
				File fin = new File(context.getFilesDir(), fname);
				if(fin.exists() && fin.length() > 0)
				{
					ObjectInputStream in = new ObjectInputStream(context.openFileInput(fname));
					Integer numTokens = (Integer)in.readObject();
					for(int i = 0; i < numTokens; i++)
						tokensDb.add((MonsterToken) in.readObject());
					in.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
				tokensDb = new ArrayList<>();
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
				tokensDb = new ArrayList<>();
			}
		}
	}
	
	private static void writeEncounters()
	{
		
		FileOutputStream fos = null;
		try
		{
			fos = context.openFileOutput(fname, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(new Integer(tokensDb.size()));
			for(MonsterToken e: tokensDb)
				oos.writeObject(e);
			oos.flush();
			oos.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static List<MonsterToken> getAllMonsterTokens(Context context)
	{
		verifyDb(context);
		return tokensDb;
	}

	public static List<SelectableItem> getAllAsSelectibles(Context context)
	{
		verifyDb(context);
		List<SelectableItem> selectableMonsterTokens = new ArrayList<>();
		for(MonsterToken e: tokensDb)
		{
			selectableMonsterTokens.add((SelectableItem) e);
		}
		return selectableMonsterTokens;
	}
	
	public static void addMonsterToken(Context context, MonsterToken e)
	{
		verifyDb(context);
		tokensDb.add(e);
		tokensDb.sort(ALPHABETICAL_COMPARATOR);
		writeEncounters();
	}
	
	public static void updateMonsterToken(Context context, MonsterToken oldMt, MonsterToken newMt)
	{
		verifyDb(context);
		int pos = tokensDb.indexOf(oldMt);
		if(pos >= 0)
			tokensDb.set(pos, newMt);
		else
			tokensDb.add(newMt);
		tokensDb.sort(ALPHABETICAL_COMPARATOR);
		writeEncounters();
	}
	
	public static void removeMonsterToken(Context context, MonsterToken e)
	{
		verifyDb(context);
		tokensDb.remove(e);
		tokensDb.sort(ALPHABETICAL_COMPARATOR);
		writeEncounters();
	}
}
