package net.kalinec.dndencounters.encounters;

import net.kalinec.dndencounters.monsters.Monster;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Encounter implements Serializable
{
	public final static String PASSED_ENCOUNTER = "PASSED_ENCOUNTER";
	private String encounterName;
	private List<Monster> monsters;
	private int cr;
	
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
	}
	
	public int getCr()
	{
		return cr;
	}
	
	public void setCr(int cr)
	{
		this.cr = cr;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Encounter encounter = (Encounter) o;
		return cr == encounter.cr &&
		       Objects.equals(encounterName, encounter.encounterName) &&
		       Objects.equals(monsters, encounter.monsters);
	}
	
	@Override
	public int hashCode()
	{
		
		return Objects.hash(encounterName, monsters, cr);
	}
}
