package net.kalinec.dndencounters.playsessions;

import android.content.Context;

import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.modules.Module;
import net.kalinec.dndencounters.parties.Party;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PlaySession implements Serializable
{
	public final static String PASSED_SESSION = "PASSED_SESSION";
	private Party players;
	private ArrayList<Encounter> encounters = new ArrayList<>();
	private Encounter currentEncounter = null;
	private AdventureEncounter adventureEncounter;
	private ArrayList<AdventureEncounter> completedEncounters = new ArrayList<>();
	private Date started, completed;
	private UUID uuid;
	private Module module = null;

	public Module getModule()
	{
		return module;
	}

	public void setModule(Module module)
	{
		this.module = module;
	}

	public boolean hasModule()
	{
		return (this.module != null);
	}

	public void updateAdventureEncounter(AdventureEncounter adventureEncounter)
	{
		this.adventureEncounter = adventureEncounter;
	}
	
	String getSessionName()
	{
		return players.getName() + " Adventure";
	}
	
	UUID getUuid()
	{
		return uuid;
	}
	
	void completeSession()
	{
		completed = new Date();
		if(encounterInProgress())
			completeCurrentEncounter();
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
		uuid = UUID.randomUUID();
	}

	public Party getPlayers() {
		return players;
	}

	public void setPlayers(Party players) {
		this.players = players;
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
	
	Date getStarted()
	{
		return started;
	}

	public void beginSession(Context context)
	{
		if(!isSessionStarted())
		{
			started = new Date();
			PlaySessionManager.saveCurrentSession(context, this);
		}
	}

	public void saveSession(Context context)
	{
		PlaySessionManager.saveCurrentSession(context, this);
	}

	public ArrayList<AdventureEncounter> getCompletedEncounters() {
		return completedEncounters;
	}

	public void completeCurrentEncounter()
	{
		currentEncounter = null;
		completedEncounters.add(adventureEncounter);
		adventureEncounter = null;
	}

	public boolean encounterInProgress()
	{
		return (currentEncounter != null && adventureEncounter != null);
	}
	
	private boolean isSessionStarted()
	{
		return (started != null);
	}
}
