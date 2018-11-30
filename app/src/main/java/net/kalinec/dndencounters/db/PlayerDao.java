package net.kalinec.dndencounters.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import net.kalinec.dndencounters.players.Player;

import java.util.List;

@Dao
public interface PlayerDao
{
	@Query("SELECT * FROM players WHERE uid = :uid")
	Player getPlayerById(int uid);
	
	@Insert
	long insert(Player player);
	
	@Update
	void update(Player player);
	
	@Delete
	void delete(Player player);
	
	@Query("SELECT * FROM players ORDER BY name ASC")
	LiveData<List<Player>> getAllPlayers();
	
	@Query("SELECT COUNT('characters.pid') AS numPcs FROM characters WHERE characters.playerId = :uid")
	int numPcs(int uid);
}
