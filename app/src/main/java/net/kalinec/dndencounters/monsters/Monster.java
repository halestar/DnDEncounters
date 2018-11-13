package net.kalinec.dndencounters.monsters;



import android.annotation.TargetApi;
import android.os.Build;

import net.kalinec.dndencounters.lib.SerializableJsonObj;

import org.json.JSONException;

import java.io.Serializable;
import java.util.Objects;


public class Monster implements Serializable
{
	public final static String PASSED_MONSTER = "PASSED_MONSTER";
	public static final String CR_ERROR = "Unk.";
	public static final String NAME_ERROR = "Error Monster";
	private int mid;
	private String name, cr;
	
	public int getMid()
	{
		return mid;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getCr()
	{
		return cr;
	}
	
	public Monster(int mid, SerializableJsonObj stats)
	{
		this.mid = mid;
		try
		{
			name = stats.getString("name");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			name = NAME_ERROR;
			
		}
		try
		{
			cr = stats.getString("challenge_rating");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			cr = CR_ERROR;
		}
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Monster monster = (Monster) o;
		return mid == monster.mid;
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	public int hashCode()
	{
		
		return Objects.hash(mid);
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}
