package net.kalinec.dndencounters.sync;

public class SyncPayload
{
	public String uuid;
	public long dbId;

	public SyncPayload(String uuid, long dbId)
	{
		this.uuid = uuid;
		this.dbId = dbId;
	}
}
