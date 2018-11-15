package net.kalinec.dndencounters.playsessions;

import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.parties.Party;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlaySession implements Serializable
{
	public final static String PASSED_SESSION = "PASSED_SESSION";
	private Party players;
	private List<Encounter> encounters = new ArrayList<>();
	private Encounter currentEncounter = null;
	private Date started, completed;
	
	public PlaySession()
	{
		started = new Date();
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
}
