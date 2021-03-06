package net.kalinec.dndencounters.modules;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Modules
{
	private static List<Module> moduleDb;
	private static final String fname = "modules.srl";
	
	private static final Comparator<Module> ALPHABETICAL_COMPARATOR = new Comparator<Module>() {
		@Override
		public int compare(Module a, Module b) {
			return a.getUuid().compareTo(b.getUuid());
		}
	};
	
	
	private static void verifyDb(Context context)
	{
		if(moduleDb == null)
		{
			try
			{
				moduleDb = new ArrayList<>();
				File fin = new File(context.getFilesDir(), fname);
				if(fin.exists() && fin.length() > 0)
				{
					ObjectInputStream in = new ObjectInputStream(context.openFileInput(fname));
					Integer numModules = (Integer)in.readObject();
					for(int i = 0; i < numModules; i++)
						moduleDb.add((Module) in.readObject());
					in.close();
				}
			}
			catch (IOException | ClassNotFoundException e)
			{
				e.printStackTrace();
				moduleDb = new ArrayList<>();
			}
		}
	}
	
	private static void writeModules(Context context)
	{
		
		FileOutputStream fos;
		try
		{
			fos = context.openFileOutput(fname, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(moduleDb.size());
			for(Module e: moduleDb)
				oos.writeObject(e);
			oos.flush();
			oos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static List<Module> getAllModules(Context context)
	{
		verifyDb(context);
		return moduleDb;
	}
	
	public static void addModule(Context context, Module e)
	{
		verifyDb(context);
		moduleDb.add(e);
		moduleDb.sort(ALPHABETICAL_COMPARATOR);
		writeModules(context);
	}
	
	public static void updateModule(Context context, Module m)
	{
		verifyDb(context);
		int pos = moduleDb.indexOf(m);
		if(pos >= 0)
			moduleDb.set(pos, m);
		else
			moduleDb.add(m);
		moduleDb.sort(ALPHABETICAL_COMPARATOR);
		writeModules(context);
	}
	
	public static void removeModule(Context context, Module e)
	{
		verifyDb(context);
		moduleDb.remove(e);
		moduleDb.sort(ALPHABETICAL_COMPARATOR);
		writeModules(context);
	}
	
	public static Module getModuleByUuid(Context context, String uuid)
	{
		verifyDb(context);
		for(Module p: moduleDb)
		{
			if(p.getUuid().toString().equals(uuid))
				return p;
		}
		return null;
	}
}
