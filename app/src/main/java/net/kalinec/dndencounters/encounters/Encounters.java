package net.kalinec.dndencounters.encounters;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
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
	private static Context context;
	private static final String fname = "encounters.srl";
	
	private static final Comparator<Encounter> ALPHABETICAL_COMPARATOR = new Comparator<Encounter>() {
		@Override
		public int compare(Encounter a, Encounter b) {
			return a.getEncounterName().compareTo(b.getEncounterName());
		}
	};
	
	
	private static void verifyDb(Context context)
	{
		Encounters.context = context;
		if(encountersDB == null)
		{
			try
			{
				encountersDB = new ArrayList<>();
				File fin = new File(context.getFilesDir(), fname);
				Log.d("Encounters", "fin length is: " + fin.length());
				if(fin.exists() && fin.length() > 0)
				{
					ObjectInputStream in = new ObjectInputStream(context.openFileInput(fname));
					Integer numEncounters = (Integer)in.readObject();
					for(int i = 0; i < numEncounters; i++)
						encountersDB.add((Encounter) in.readObject());
					in.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
				encountersDB = new ArrayList<>();
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
				encountersDB = new ArrayList<>();
			}
		}
	}
	
	private static void writeEncounters()
	{
		
		Log.d("Encounters", "Wirintg obj file.  encountersDB: " + encountersDB);
		FileOutputStream fos = null;
		try
		{
			fos = context.openFileOutput(fname, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(new Integer(encountersDB.size()));
			for(Encounter e: encountersDB)
				oos.writeObject(e);
			oos.flush();
			oos.close();
			Log.d("Encounters", "Objects written correctly. ");
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
	
	public static List<Encounter> getAllEncounters(Context context)
	{
		verifyDb(context);
		return encountersDB;
	}
	
	public static void addEncounter(Context context, Encounter e)
	{
		verifyDb(context);
		encountersDB.add(e);
		encountersDB.sort(ALPHABETICAL_COMPARATOR);
		writeEncounters();
	}
	
	public static void updateEncounter(Context context, Encounter oldEncounter, Encounter newEncounter)
	{
		verifyDb(context);
		int pos = encountersDB.indexOf(oldEncounter);
		Log.d("Encounters", "encountersDb=" + encountersDB);
		Log.d("Encounters", "searching for=" + oldEncounter);
		Log.d("Encounters", "to replace with=" + newEncounter);
		Log.d("Encounters", "in update encounter, pos=" + pos);
		if(pos >= 0)
			encountersDB.set(pos, newEncounter);
		else
			encountersDB.add(newEncounter);
		encountersDB.sort(ALPHABETICAL_COMPARATOR);
		writeEncounters();
	}
	
	public static void removeEncounter(Context context, Encounter e)
	{
		verifyDb(context);
		encountersDB.remove(e);
		encountersDB.sort(ALPHABETICAL_COMPARATOR);
		writeEncounters();
	}
}
