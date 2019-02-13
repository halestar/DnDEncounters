package net.kalinec.dndencounters.players;

import android.content.Context;

import net.kalinec.dndencounters.characters.Character;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class Players implements Serializable
{
	private static ArrayList<Player> playersDb;
	private static final String fname = "players.srl";
	
	private static final Comparator<Player> ALPHABETICAL_COMPARATOR = new Comparator<Player>() {
		@Override
		public int compare(Player a, Player b) {
			return a.getName().compareTo(b.getName());
		}
	};
	
	
	private static void verifyDb(Context context)
	{
		if(playersDb == null)
		{
			try
			{
				playersDb = new ArrayList<>();
				File fin = new File(context.getFilesDir(), fname);
				if(fin.exists() && fin.length() > 0)
				{
					ObjectInputStream in = new ObjectInputStream(context.openFileInput(fname));
					Integer numPlayers = (Integer)in.readObject();
					for(int i = 0; i < numPlayers; i++)
						playersDb.add((Player) in.readObject());
					in.close();
				}
			}
			catch (IOException | ClassNotFoundException e)
			{
				e.printStackTrace();
				playersDb = new ArrayList<>();
			}
		}
	}
	
	private static void writePlayers(Context context)
	{
		
		FileOutputStream fos;
		try
		{
			fos = context.openFileOutput(fname, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(playersDb.size());
			for(Player e: playersDb)
				oos.writeObject(e);
			oos.flush();
			oos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static List<Player> getAllPlayers(Context context)
	{
		verifyDb(context);
		return playersDb;
	}
	
	public static void addPlayer(Context context, Player e)
	{
		verifyDb(context);
		playersDb.add(e);
		playersDb.sort(ALPHABETICAL_COMPARATOR);
		writePlayers(context);
	}
	
	public static void updatePlayer(Context context, Player player)
	{
		verifyDb(context);
		int pos = playersDb.indexOf(player);
		if(pos >= 0)
			playersDb.set(pos, player);
		else
			playersDb.add(player);
		playersDb.sort(ALPHABETICAL_COMPARATOR);
		writePlayers(context);
	}
	
	public static void removePlayer(Context context, Player e)
	{
		verifyDb(context);
		playersDb.remove(e);
		playersDb.sort(ALPHABETICAL_COMPARATOR);
		writePlayers(context);
	}
	
	public static List<Character> getAllPcs(Context context)
	{
		verifyDb(context);
		ArrayList<Character> pcs = new ArrayList<>();
		for(Player p: playersDb)
			pcs.addAll(p.getPcs());
		return pcs;
	}
	
	public static void updatePc(Context context, Character pc)
	{
		verifyDb(context);
		for(Player p: playersDb)
		{
			if(p.getPcs().contains(pc))
			{
				p.getPcs().set(p.getPcs().indexOf(pc), pc);
				writePlayers(context);
				return;
			}
		}
	}
	
	public static void removePc(Context context, Character pc)
	{
		verifyDb(context);
		for(Player p: playersDb)
		{
			if(p.getPcs().contains(pc))
			{
				p.getPcs().remove(pc);
				writePlayers(context);
				return;
			}
		}
	}
	
	public static Player findOwner(Context context, Character pc)
	{
		verifyDb(context);
		for(Player p: playersDb)
		{
			if(p.getPcs().contains(pc))
				return p;
		}
		return null;
	}

	public static Player getPlayerByUuid(Context context, String uuid)
	{
		verifyDb(context);
		for(Player p: playersDb)
		{
			if(p.getUuid().toString().equals(uuid))
				return p;
		}
		return null;
	}
}
