package net.kalinec.dndencounters.encounters;

import android.content.Context;

import net.kalinec.dndencounters.lib.SelectableItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Encounters
{
	private static List<Encounter> encountersDB;
	private static final String fname = "encounters.srl";
	
	private static final Comparator<Encounter> ALPHABETICAL_COMPARATOR = new Comparator<Encounter>() {
		@Override
		public int compare(Encounter a, Encounter b) {
			return a.getEncounterName().compareTo(b.getEncounterName());
		}
	};
	
	
	private static void verifyDb(Context context)
	{
		if(encountersDB == null)
		{
			try
			{
				encountersDB = new ArrayList<>();
				File fin = new File(context.getFilesDir(), fname);
				if(fin.exists() && fin.length() > 0)
				{
					ObjectInputStream in = new ObjectInputStream(context.openFileInput(fname));
					Integer numEncounters = (Integer)in.readObject();
					for(int i = 0; i < numEncounters; i++)
						encountersDB.add((Encounter) in.readObject());
					in.close();
				}
			}
			catch (IOException | ClassNotFoundException e)
			{
				e.printStackTrace();
				encountersDB = new ArrayList<>();
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
			oos.writeObject(encountersDB.size());
			for(Encounter e: encountersDB)
				oos.writeObject(e);
			oos.flush();
			oos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static List<Encounter> getAllEncounters(Context context)
	{
		verifyDb(context);
		return encountersDB;
	}

	public static List<SelectableItem> getAllAsSelectibles(Context context)
	{
		verifyDb(context);
		return new ArrayList<SelectableItem>(encountersDB);
	}
	
	public static void addEncounter(Context context, Encounter e)
	{
		verifyDb(context);
		encountersDB.add(e);
		encountersDB.sort(ALPHABETICAL_COMPARATOR);
		writeEncounters(context);
	}
	
	public static void updateEncounter(Context context, Encounter e)
	{
		verifyDb(context);
		int pos = encountersDB.indexOf(e);
		if(pos >= 0)
			encountersDB.set(pos, e);
		else
			encountersDB.add(e);
		encountersDB.sort(ALPHABETICAL_COMPARATOR);
		writeEncounters(context);
	}
	
	public static void removeEncounter(Context context, Encounter e)
	{
		verifyDb(context);
		encountersDB.remove(e);
		encountersDB.sort(ALPHABETICAL_COMPARATOR);
		writeEncounters(context);
	}
	
	public static Encounter getEncounterByUuid(Context context, String uuid)
	{
		verifyDb(context);
		for(Encounter p: encountersDB)
		{
			if(p.getUuid().toString().equals(uuid))
				return p;
		}
		return null;
	}
	
	public static Encounter getMonsterByDbId(Context context, long dbId)
	{
		verifyDb(context);
		for(Encounter p: encountersDB)
		{
			if(p.getDbId() == dbId)
				return p;
		}
		return null;
	}
}
