package net.kalinec.dndencounters.sync;

import java.util.ArrayList;

public class SyncPlayerPayload extends SyncPayload
{
	public ArrayList<SyncPayload> pcs;
	
	public SyncPlayerPayload(String uuid, long dbId)
	{
		super(uuid, dbId);
		pcs = new ArrayList<>();
	}
	
}
