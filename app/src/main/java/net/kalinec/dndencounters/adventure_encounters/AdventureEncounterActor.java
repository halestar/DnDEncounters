package net.kalinec.dndencounters.adventure_encounters;

import java.io.Serializable;
import java.util.UUID;

public interface AdventureEncounterActor extends Serializable
{
	
	int ALIVE = 1;
	int DEAD = 2;
	
	int PLAYER_ACTOR = 3;
	int MONSTER_ACTOR = 4;
	
	int getInitiative();
	
	void setInitiative(int initiative);
	
	int getStatus();
	
	void setStatus(int status);
	
	int getActorType();
	
	boolean hasActed();
	
	void setHasActed(boolean hasActed);
	
	UUID getUuid();
	
	String getName();
	
	int getHp();
}
