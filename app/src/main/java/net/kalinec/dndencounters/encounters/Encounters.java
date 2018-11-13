package net.kalinec.dndencounters.encounters;

import android.content.Context;

import net.kalinec.dndencounters.monsters.Monster;

import java.io.File;
import java.io.FileInputStream;
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
		if(Encounters.context == null)
			Encounters.context = context;
		if(encountersDB == null)
		{
			try
			{
				encountersDB = new ArrayList<>();
				File fin = new File(context.getFilesDir(), fname);
				if(fin.exists()){
					
					ObjectInputStream in = new ObjectInputStream(new FileInputStream(fin));
					while (true)
					{
						Encounter encounter = (Encounter) in.readObject();
						if (encounter == null)
							break;
						encountersDB.add(encounter);
					}
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
	
	private void writeEncounters()
	{
		FileOutputStream fos = null;
		try
		{
			fos = context.openFileOutput(fname, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			for(Encounter e: encountersDB)
				oos.writeObject(e);
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
	}
}
