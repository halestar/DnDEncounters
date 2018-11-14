package net.kalinec.dndencounters.playsessions;

import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.parties.Party;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlaySession implements Serializable
{
	private Party players = null;
	private List<Encounter> encounters = new ArrayList<>();
	private Encounter currentEncounter = null;
	private Date started, completed;
	
	public PlaySession()
	{
		started = new Date();
	}
	
}
