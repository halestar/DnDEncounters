package net.kalinec.dndencounters.monster_tokens;

import android.content.Context;

import java.io.File;
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
	private static final String fname = "monster_tokens.srl";
	
	private static final Comparator<MonsterToken> ALPHABETICAL_COMPARATOR = new Comparator<MonsterToken>() {
		@Override
		public int compare(MonsterToken a, MonsterToken b) {
			return a.getTokenName().compareTo(b.getTokenName());
		}
	};
	
	
	private static void verifyDb(Context context)
	{
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
			catch (IOException | ClassNotFoundException e)
			{
				e.printStackTrace();
				tokensDb = new ArrayList<>();
			}
		}
	}
	
	private static void writeEncounters(Context context)
	{
		
		FileOutputStream fos;
		try
		{
			fos = context.openFileOutput(fname, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(tokensDb.size());
			for(MonsterToken e: tokensDb)
				oos.writeObject(e);
			oos.flush();
			oos.close();
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
	
	public static void addMonsterToken(Context context, MonsterToken e)
	{
		verifyDb(context);
		tokensDb.add(e);
		tokensDb.sort(ALPHABETICAL_COMPARATOR);
		writeEncounters(context);
	}
	
	public static void updateMonsterToken(Context context, MonsterToken mt)
	{
		verifyDb(context);
		int pos = tokensDb.indexOf(mt);
		if(pos >= 0)
			tokensDb.set(pos, mt);
		else
			tokensDb.add(mt);
		tokensDb.sort(ALPHABETICAL_COMPARATOR);
		writeEncounters(context);
	}
	
	public static void removeMonsterToken(Context context, MonsterToken e)
	{
		verifyDb(context);
		tokensDb.remove(e);
		tokensDb.sort(ALPHABETICAL_COMPARATOR);
		writeEncounters(context);
	}
	
	public static MonsterToken getMonsterByUuid(Context context, String uuid)
	{
		verifyDb(context);
		for(MonsterToken p: tokensDb)
		{
			if(p.getUuid().toString().equals(uuid))
				return p;
		}
		return null;
	}
}
