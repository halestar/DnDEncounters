package net.kalinec.dndencounters.sync;

import android.content.Context;

import net.kalinec.dndencounters.custom_monsters.CustomMonster;
import net.kalinec.dndencounters.custom_monsters.CustomMonsters;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.encounters.Encounters;
import net.kalinec.dndencounters.modules.Module;
import net.kalinec.dndencounters.modules.Modules;
import net.kalinec.dndencounters.monster_tokens.MonsterToken;
import net.kalinec.dndencounters.monster_tokens.MonsterTokens;

import java.io.Serializable;
import java.util.ArrayList;

public class SyncData implements Serializable
{
	public ArrayList<CustomMonster> custom_monsters;
	public ArrayList<Encounter> encounters;
	public ArrayList<Module> modules;
	public ArrayList<MonsterToken> monster_tokens;

	public SyncData(Context context)
	{
		custom_monsters = (ArrayList<CustomMonster>)CustomMonsters.getCustomMonsters(context);
		encounters = (ArrayList<Encounter>) Encounters.getAllEncounters(context);
		modules = (ArrayList<Module>) Modules.getAllModules(context);
		monster_tokens = (ArrayList<MonsterToken>) MonsterTokens.getAllMonsterTokens(context);
	}
}
