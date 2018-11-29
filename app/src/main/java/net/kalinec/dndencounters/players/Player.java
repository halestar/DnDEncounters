package net.kalinec.dndencounters.players;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "players")
public class Player implements Serializable
{
	
	public final static String PASSED_PLAYER = "PASSED_PLAYER";
	
	@PrimaryKey(autoGenerate = true)
	@NonNull
	public int uid;
	public String name;
	public String dci;
	@ColumnInfo(typeAffinity = ColumnInfo.BLOB)
	private byte[] portrait;
	
	public String getDci()
	{
		return dci;
	}
	
	public void setDci(String dci)
	{
		this.dci = dci;
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
		this.name = name;
	}
	
	@NonNull
	public int getUid()
	{
		return uid;
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
}
