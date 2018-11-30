package net.kalinec.dndencounters.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.players.Player;

@Database(entities = {Player.class, Character.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase
{
	private static AppDatabase INSTANCE;
	private static final String DB_NAME = "app_db";
	
	public static AppDatabase getDatabase(final Context context)
	{
		if (INSTANCE == null)
		{
			synchronized (AppDatabase.class)
			{
				if (INSTANCE == null)
				{
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
					                                AppDatabase.class, DB_NAME)
							.allowMainThreadQueries() // SHOULD NOT BE USED IN PRODUCTION !!!
							.build();
				}
			}
		}
		return INSTANCE;
	}
	
	public abstract PlayerDao playerDao();
	public abstract CharacterDao characterDao();
	
	
	
}
