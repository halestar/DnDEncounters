package net.kalinec.dndencounters.sync;

import java.util.ArrayList;

public class LinkData
{
	public ArrayList<SyncPlayerPayload> players;
	public ArrayList<SyncPayload> custom_monsters;
	public ArrayList<SyncPayload> encounters;
	public ArrayList<SyncPayload> monster_tokens;
	public ArrayList<SyncPayload> modules;
	
	public LinkData()
	{
		players = new ArrayList<>();
		custom_monsters = new ArrayList<>();
		encounters = new ArrayList<>();
		monster_tokens = new ArrayList<>();
		modules = new ArrayList<>();
	}
}
