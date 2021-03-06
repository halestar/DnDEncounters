package net.kalinec.dndencounters.characters;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Character implements Serializable
{
	
	public final static String PASSED_CHARACTER = "PASSED_CHARACTER";
	
	public UUID uuid;
	public int dbId;
	public String name;
	public String characterClass;
	public String characterRace;

	public int ac;
	public int hp;
	public int pp;
	public int level;
	public int spellDc;
	public Character(
			@NonNull String name, int ac, int hp,
			int pp,
			@NonNull String characterClass,
			int level
	                )
	{
		this.uuid = UUID.randomUUID();
		this.name = name;
		this.ac = ac;
		this.hp = hp;
		this.pp = pp;
		this.characterClass = characterClass;
		this.level = level;
	}
	
	public Character()
	{
		this.uuid = UUID.randomUUID();
	}
	
	@NonNull
	@Override
	public String toString()
	{
		return "Character{" +
		       "name='" + name + '\'' +
		       ", characterClass='" + characterClass + '\'' +
		       ", characterRace='" + characterRace + '\'' +
		       '}';
	}
	
	@NonNull
	public String getName()
	{
		return name;
	}
	
	public void setName(@NonNull String name)
	{
		this.name = name;
	}
	
	
	public int getLevel()
	{
		return level;
	}
	
	public void setLevel(int level)
	{
		this.level = level;
	}
	
	public int getAc()
	{
		return ac;
	}
	
	public void setAc(int ac)
	{
		this.ac = ac;
	}
	
	public int getHp()
	{
		return hp;
	}
	
	public void setHp(int hp)
	{
		this.hp = hp;
	}
	
	public int getPp()
	{
		return pp;
	}
	
	public void setPp(int pp)
	{
		this.pp = pp;
	}
	
	public int getSpellDc()
	{
		return spellDc;
	}
	
	public void setSpellDc(int spellDc)
	{
		this.spellDc = spellDc;
	}
	
	@NonNull
	public String getCharacterClass()
	{
		return characterClass;
	}
	
	public void setCharacterClass(@NonNull String characterClass)
	{
		this.characterClass = characterClass;
	}
	
	public String getCharacterRace()
	{
		return characterRace;
	}
	
	public void setCharacterRace(String characterRace)
	{
		this.characterRace = characterRace;
	}
	
	String nameAndDescription()
	{
		return name + "(" +
		       (!characterRace.equals("") ? characterRace + " " : "") +
		       characterClass + ")";
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Character character = (Character) o;
		return Objects.equals(uuid, character.uuid);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(uuid);
	}
	
	public UUID getUuid()
	{
		return uuid;
	}
	
	public int getDbId()
	{
		return dbId;
	}
	
	public void setDbId(int dbId)
	{
		this.dbId = dbId;
	}
}
