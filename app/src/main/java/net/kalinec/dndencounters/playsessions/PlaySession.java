package net.kalinec.dndencounters.playsessions;

import android.content.Context;

import net.kalinec.dndencounters.encounter.AdventureEncounter;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.parties.Party;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlaySession implements Serializable
{
	public final static String PASSED_SESSION = "PASSED_SESSION";
	private final static String fname = "current_session.srl";
	private Party players;
	private ArrayList<Encounter> encounters = new ArrayList<>();
	private Encounter currentEncounter = null;
	private AdventureEncounter adventureEncounter;
	private Date started, completed;

	public Encounter getCurrentEncounter() {
		return currentEncounter;
	}

	public void setCurrentEncounter(Encounter currentEncounter) {
		this.currentEncounter = currentEncounter;
		if(currentEncounter != null)
			adventureEncounter = new AdventureEncounter(players, currentEncounter);
	}

	public AdventureEncounter getAdventureEncounter() {
		return adventureEncounter;
	}

	public PlaySession()
	{
		players = new Party();
	}

	public Party getPlayers() {
		return players;
	}

	public void setPlayers(Party players) {
		this.players = players;
	}

	public void addEncounter(Encounter e)
	{
		encounters.add(e);
	}

	public void addEncounters(List<Encounter> eList)
	{
		encounters.addAll(eList);
	}

	public void removeEncounter(Encounter e)
	{
		encounters.remove(e);
	}

	public List<Encounter> getEncounters() {
		return encounters;
	}

	public Date getStarted() {
		return started;
	}

	public Date getCompleted() {
		return completed;
	}

	private void writeSession(Context context)
	{
		File fout = new File(context.getFilesDir(), fname);
		try {
			if (!fout.exists())
				fout.createNewFile();
			ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(fname, Context.MODE_PRIVATE));
			oos.writeObject(this);
			oos.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void beginSession(Context context)
	{
		started = new Date();
		writeSession(context);
	}

	public void saveSession(Context context)
	{
		writeSession(context);
	}

	public static PlaySession existingSession(Context context)
	{
		PlaySession activeSession = null;
		try
		{
			File fout = new File(context.getFilesDir(), fname);
			if(!fout.exists())
				return null;
			ObjectInputStream ois = new ObjectInputStream(context.openFileInput(fname));
			activeSession = (PlaySession)ois.readObject();
		}
		catch(IOException e)
		{
			return null;
		}
		catch(ClassNotFoundException e)
		{
			return null;
		}
		return activeSession;
	}
}
