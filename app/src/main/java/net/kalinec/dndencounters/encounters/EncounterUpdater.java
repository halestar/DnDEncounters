package net.kalinec.dndencounters.encounters;

import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterActor;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.monster_tokens.MonsterToken;

public class EncounterUpdater
{
	private int initiative, initiativePos, currentHp;
	private boolean isAlive;
	private MonsterToken assignedToken;
	private AdventureEncounterMonster monster;

	public EncounterUpdater(AdventureEncounterMonster monster)
	{
		this.monster = monster;
		this.initiative = monster.getInitiative();
		this.initiativePos = monster.getInitiativePosition();
		this.currentHp = monster.getHp();
		this.isAlive = (monster.getStatus() == AdventureEncounterActor.ALIVE);
		this.assignedToken = monster.getToken();
	}

	public void setInitiative(int initiative)
	{
		this.initiative = initiative;
	}

	public AdventureEncounterMonster getMonster()
	{
		return monster;
	}

	public int getInitiative()
	{
		return initiative;
	}

	public int getCurrentHp()
	{
		return currentHp;
	}

	public void setCurrentHp(int currentHp)
	{
		this.currentHp = currentHp;
	}

	public boolean isAlive()
	{
		return isAlive;
	}

	public void setAlive(boolean alive)
	{
		isAlive = alive;
	}

	public MonsterToken getAssignedToken()
	{
		return assignedToken;
	}

	public void setAssignedToken(MonsterToken assignedToken)
	{
		this.assignedToken = assignedToken;
	}

	public int getInitiativePos()
	{
		return initiativePos;
	}

	public void setInitiativePos(int initiativePos)
	{
		this.initiativePos = initiativePos;
	}

	public AdventureEncounterMonster getUpdatedMonster()
	{
		monster.setInitiative(initiative);
		monster.setInitiativePosition(initiativePos);
		monster.setHp(currentHp);
		monster.setToken(assignedToken);
		if(isAlive)
			monster.setStatus(AdventureEncounterActor.ALIVE);
		else
			monster.setStatus(AdventureEncounterActor.DEAD);
		return monster;
	}

}
