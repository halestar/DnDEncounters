package net.kalinec.dndencounters.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.players.Player;

import java.util.List;

@Dao
public interface CharacterDao
{
	@Query("SELECT * FROM characters WHERE pid = :pid ORDER BY NAME")
	Character getCharacterById(int pid);

	@Query("SELECT * FROM characters WHERE playerId = :playerId ORDER BY name")
	LiveData<List<Character>> getCharactersByPlayerId(int playerId);
	
	@Insert
	long insert(Character character);
	
	@Insert
	void insert(Character... characters);
	
	@Update
	void update(Character character);
	
	@Delete
	void delete(Character character);
	
	@Query("DELETE FROM characters")
	void deleteAll();
	
	@Query("SELECT * FROM characters ORDER BY name ASC")
	LiveData<List<Character>> getAllCharacters();
}
