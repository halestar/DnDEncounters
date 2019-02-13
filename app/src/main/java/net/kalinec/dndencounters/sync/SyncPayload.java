package net.kalinec.dndencounters.sync;

public class SyncPayload
{
	public String uuid;
	public int dbId;

	public SyncPayload(String uuid, int dbId)
	{
		this.uuid = uuid;
		this.dbId = dbId;
	}
}
