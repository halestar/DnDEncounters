package net.kalinec.dndencounters.players;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import net.kalinec.dndencounters.db.AppDatabase;
import net.kalinec.dndencounters.db.PlayerDao;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel
{
	private PlayerDao playerDao;
	private LiveData<List<Player>> playerLiveData;
	
	public PlayerViewModel(@NotNull Application application)
	{
		super(application);
		playerDao = AppDatabase.getDatabase(application).playerDao();
		playerLiveData = playerDao.getAllPlayers();
	}
	
	public LiveData<List<Player>> getPlayerList()
	{
		return playerLiveData;
	}
	
	public void insert(Player... players)
	{
		playerDao.insert(players);
	}
	
	public void insert(Player player)
	{
		playerDao.insert(player);
	}
	
	public void update(Player player)
	{
		playerDao.update(player);
	}
	
	public void delete(Player player)
	{
		playerDao.delete(player);
	}
	
	public void deleteAll()
	{
		playerDao.deleteAll();
	}
	
	
}
