package net.kalinec.dndencounters.characters;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import net.kalinec.dndencounters.players.Player;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "characters",
		foreignKeys = @ForeignKey(entity = Player.class,
				parentColumns = "uid",
				childColumns = "playerId",
				onDelete = ForeignKey.CASCADE),
		indices = {@Index("name"), @Index("playerId")})
public class Character implements Serializable
{
	
	public final static String PASSED_CHARACTER = "PASSED_CHARACTER";
	
	@PrimaryKey(autoGenerate = true)
	public int pid;
	public int playerId;
	public String name;
	public String characterClass;
	public String characterRace;

	public int ac;
	public int hp;
	public int pp;
	public int level;
	public int spellDc;
	public Character(
			@NonNull String name, int playerId, int ac, int hp,
			int pp,
			@NonNull String characterClass,
			int level
	                )
	{
		this.name = name;
		this.playerId = playerId;
		this.ac = ac;
		this.hp = hp;
		this.pp = pp;
		this.characterClass = characterClass;
		this.level = level;
	}
	
	public Character(Player owner)
	{
		this.playerId = owner.getUid();
		
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

	public int getPlayerId() {
		return playerId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Character character = (Character) o;
		return pid == character.pid &&
				playerId == character.playerId;
	}

	@Override
	public int hashCode() {

		return Objects.hash(pid, playerId);
	}
}
