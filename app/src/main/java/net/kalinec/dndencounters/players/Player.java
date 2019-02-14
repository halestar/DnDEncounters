package net.kalinec.dndencounters.players;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import net.kalinec.dndencounters.characters.Character;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Player implements Serializable
{
	
	public final static String PASSED_PLAYER = "PASSED_PLAYER";
	
	private UUID uuid;
	private long dbId;
	private String name;
	public String dci;
	private byte[] portrait;
	private ArrayList<Character> pcs = new ArrayList<>();
	
	public ArrayList<Character> getPcs()
	{
		return pcs;
	}
	
	public void setPcs(ArrayList<Character> pcs)
	{
		this.pcs = pcs;
	}
	
	public String getDci()
	{
		return dci;
	}
	
	public void setDci(String dci)
	{
		this.dci = dci.equals("null")? "": dci;
	}
	
	public byte[] getPortrait()
	{
		return portrait;
	}
	
	public void setPortrait(byte[] portrait)
	{
		this.portrait = portrait;
	}
	
	public Player(String name)
	{
		this.uuid = UUID.randomUUID();
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public Bitmap getMiniPortrait() {
		if(portrait != null)
			return BitmapFactory.decodeByteArray(portrait, 0, portrait.length);
		return null;
	}
	
	public UUID getUuid()
	{
		return uuid;
	}
	
	public long getDbId()
	{
		return dbId;
	}
	
	public void setDbId(long dbId)
	{
		this.dbId = dbId;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Player player = (Player) o;
		return Objects.equals(uuid, player.uuid);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(uuid);
	}
}
