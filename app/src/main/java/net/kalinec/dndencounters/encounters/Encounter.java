package net.kalinec.dndencounters.encounters;

import android.support.annotation.NonNull;

import net.kalinec.dndencounters.lib.SelectableItem;
import net.kalinec.dndencounters.monsters.Monster;

import org.apache.commons.math3.fraction.Fraction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Encounter implements Serializable, SelectableItem
{
	public final static String PASSED_ENCOUNTER = "PASSED_ENCOUNTER";
	public final static String PASSED_ENCOUNTERS = "PASSED_ENCOUNTERS";
	private UUID uuid;
	private long dbId;
	private String encounterName;
	private List<Monster> monsters;
	private int cr;
	private boolean isSelected = false;

	public Encounter(String name, List<Monster> monsters)
	{
		this.uuid = UUID.randomUUID();
		this.encounterName = name;
		this.monsters = monsters;
		updateCr();
	}

	public Encounter()
	{
		this.uuid = UUID.randomUUID();
		this.encounterName = "";
		this.monsters = new ArrayList<>();
	}
	
	private void updateCr()
	{
		double newCr = 0;
		for(Monster m: monsters)
		{
			String crStr = m.getCr();
			if (crStr.equals(Monster.CR_ERROR))
				continue;
			String[] fractionCr = crStr.split("/");
			if(fractionCr.length == 1)
				newCr += Integer.parseInt(crStr);
			else if(fractionCr.length == 2)
			{
				//fraction.
				Fraction f = new Fraction(Integer.parseInt(fractionCr[0]), Integer.parseInt(fractionCr[1]));
				newCr += f.doubleValue();
			}
		}
		cr = (int)Math.ceil(newCr);
	}
	
	public String getEncounterName()
	{
		return encounterName;
	}
	
	public void setEncounterName(String encounterName)
	{
		this.encounterName = encounterName;
	}
	
	public List<Monster> getMonsters()
	{
		return monsters;
	}
	
	public void setMonsters(List<Monster> monsters)
	{
		this.monsters = monsters;
		updateCr();
	}
	
	public int getCr()
	{
		return cr;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Encounter encounter = (Encounter) o;
		return Objects.equals(uuid, encounter.uuid);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(uuid);
	}

	@NonNull
	@Override
	public String toString()
	{
		return "Encounter{" +
		       "encounterName='" + encounterName + '\'' +
		       ", monsters=" + monsters +
		       ", cr=" + cr +
		       '}';
	}

	@Override
	public boolean isSelected() {
		return isSelected;
	}

	@Override
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public String getSelectableText() {
		return this.encounterName + "(CR: " + this.cr + ")";
	}

	public long getDbId()
	{
		return dbId;
	}

	public void setDbId(long dbId)
	{
		this.dbId = dbId;
	}

	public UUID getUuid()
	{
		return uuid;
	}
}
