package net.kalinec.dndencounters.playsessions;

import android.content.Context;

import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
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

	public void updateAdventureEncounter(AdventureEncounter adventureEncounter)
	{
		this.adventureEncounter = adventureEncounter;
	}

	public String getSessionName()
	{
		return players.getName() + " Adventure";
	}

	public UUID getUuid() {
		return uuid;
	}

	public void completeSession()
	{
		completed = new Date();
		if(encounterInProgress())
			completeCurrentEncounter();
	}

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
		uuid = UUID.randomUUID();
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

	public boolean isSessionStarted()
	{
		return (started != null);
	}
}
