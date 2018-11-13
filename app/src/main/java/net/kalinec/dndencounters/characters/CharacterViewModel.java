package net.kalinec.dndencounters.characters;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import net.kalinec.dndencounters.db.AppDatabase;
import net.kalinec.dndencounters.db.CharacterDao;
import net.kalinec.dndencounters.db.PlayerDao;
import net.kalinec.dndencounters.players.Player;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CharacterViewModel extends AndroidViewModel
{
	private CharacterDao characterDao;
	private LiveData<List<Character>> characterLiveData;
	
	public CharacterViewModel(@NotNull Application application)
	{
		super(application);
		characterDao = AppDatabase.getDatabase(application).characterDao();
		characterLiveData = characterDao.getAllCharacters();
	}
	
	public LiveData<List<Character>> getCharacterList()
	{
		return characterLiveData;
	}
	
	public void insert(Character... characters)
	{
		characterDao.insert(characters);
	}
	
	public void insert(Character character)
	{
		characterDao.insert(character);
	}
	
	public void update(Character character)
	{
		characterDao.update(character);
	}
	
	public void delete(Character character)
	{
		characterDao.delete(character);
	}
	
	public void deleteAll()
	{
		characterDao.deleteAll();
	}
	
	
}
