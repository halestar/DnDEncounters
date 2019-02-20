package net.kalinec.dndencounters.adventure_encounters;

import android.support.annotation.NonNull;
import android.util.Log;

import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.parties.Party;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdventureEncounter implements Serializable {
	
	public final static String PASSED_ADVENTURE_ENCOUNTER = "PASSED_ADVENTURE_ENCOUNTER";
    private Party pcs;
    private Encounter encounter;
    private ArrayList<AdventureEncounterActor> actors;
    private ArrayList<AdventureEncounterTurn> completedTurns;
    private AdventureEncounterTurn currentTurn;
    private boolean completed, setup, monsterInitiative, monsterHp;
    private int turnNumber;

    public boolean isMonsterInitiative()
    {
        return monsterInitiative;
    }

    public void setMonsterInitiative(boolean monsterInitiative)
    {
        this.monsterInitiative = monsterInitiative;
    }

    public boolean isMonsterHp()
    {
        return monsterHp;
    }

    public void setMonsterHp(boolean monsterHp)
    {
        this.monsterHp = monsterHp;
    }

    public Encounter getEncounter() {
        return encounter;
    }


    public AdventureEncounterTurn getCurrentTurn() {
        return currentTurn;
    }

    public boolean isCompleted()
    {
        //easy, check that there is at least one alive monster
        completed = true;
        for(AdventureEncounterActor actor: actors)
        {
            if(actor.getActorType() == AdventureEncounterActor.MONSTER_ACTOR && actor.getStatus() == AdventureEncounterActor.ALIVE)
            {
                completed = false;
                break;
            }
        }
        return completed;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public Party getParty() {
        return pcs;
    }

    public AdventureEncounter(Party p, Encounter e)
    {
        this.pcs = p;
        this.encounter = e;
        actors = new ArrayList<>();
        completedTurns = new ArrayList<>();
        currentTurn = null;
        completed = setup = false;
        turnNumber = 1;

    }

    public boolean isSetup() {
        return setup;
    }

    private boolean isSetUp()
    {
        //easy check we must have the same number of aactor as players+monsters
        if(actors.size() != (pcs.getMembers().size() + encounter.getMonsters().size()))
            return false;
        Log.d("AdventureEncounter", "sizes match");
        //else we must make sure that each actor matches a member of the party and a monster
        for(Character c: pcs.getMembers())
        {
            boolean found = false;
            for(AdventureEncounterActor a: actors)
            {
                if(a.getActorType() == AdventureEncounterActor.PLAYER_ACTOR && ((AdventureEncounterPlayer)a).getPc().equals(c))
                {
                    found = true;
                    break;
                }
            }
            if(!found)
                return false;
        }
        Log.d("AdventureEncounter", "pcs match");
        //next, check the monsters
        for(Monster m: encounter.getMonsters())
        {
            boolean found = false;
            for(AdventureEncounterActor a: actors)
            {
                if(a.getActorType() == AdventureEncounterActor.MONSTER_ACTOR && ((AdventureEncounterMonster)a).getMonster().equals(m))
                {
                    found = true;
                    break;
                }
            }
            if(!found)
                return false;
        }
        Log.d("AdventureEncounter", "monsters match");
        //else, we arte done.
        return true;
    }

    public void addActor(AdventureEncounterActor a)
    {
        actors.add(a);
        setup = isSetUp();
    }

    public void clearActors()
    {
        actors.clear();
    }

    public void beginEncounter()
    {
        if(setup && !completed)
        {
            currentTurn = new AdventureEncounterTurn(actors, turnNumber);
        }
    }

    public AdventureEncounterTurn nextTurn()
    {
        return currentTurn;
    }
	
	
	@NonNull
    @Override
    public String toString() {
        return "AdventureEncounter{" +
               "pcs=" + pcs +
               ", encounter=" + encounter +
               ", actors=" + actors +
               ", currentTurn=" + currentTurn +
               ", completed=" + completed +
               ", setup=" + setup +
               ", turnNumber=" + turnNumber +
               '}';
    }

    public ArrayList<AdventureEncounterMonster> getAvailableMonsters()
    {
        ArrayList<AdventureEncounterMonster> availableMonsters = new ArrayList<>();
        for(AdventureEncounterActor a: actors)
        {
            if(a.getActorType() == AdventureEncounterActor.MONSTER_ACTOR && a.getStatus() != AdventureEncounterActor.DEAD)
                availableMonsters.add((AdventureEncounterMonster)a);
        }
        return availableMonsters;
    }

    public ArrayList<AdventureEncounterMonster> getAllAvailableMonsters()
    {
        ArrayList<AdventureEncounterMonster> availableMonsters = new ArrayList<>();
        for(AdventureEncounterActor a: actors)
        {
            if(a.getActorType() == AdventureEncounterActor.MONSTER_ACTOR)
                availableMonsters.add((AdventureEncounterMonster)a);
        }
        return availableMonsters;
    }

    public ArrayList<AdventureEncounterPlayer> getAvailablePlayers()
    {
        ArrayList<AdventureEncounterPlayer> availablePlayers = new ArrayList<>();
        for(AdventureEncounterActor a: actors)
        {
            if(a.getActorType() == AdventureEncounterActor.PLAYER_ACTOR)
                availablePlayers.add((AdventureEncounterPlayer)a);
        }
        return availablePlayers;
    }

    public void updateActor(AdventureEncounterActor actor)
    {
    	boolean found = false;
        for(int i = 0; i < actors.size(); i++)
        {
            if(actors.get(i).getUuid().equals(actor.getUuid()))
            {
                actors.set(i, actor);
                found = true;
                break;
            }
        }
        if(!found)
        	actors.add(actor);
    }

    public List<AdventureEncounterActor> getActors() {
        return actors;
    }

    public void finishTurn()
    {
        if(currentTurn.isCompleted())
        {
            completedTurns.add(currentTurn);
            turnNumber++;
            //make all the actors not act.
            for(AdventureEncounterActor actor: actors)
                actor.setHasActed(false);
            currentTurn = new AdventureEncounterTurn(actors, turnNumber);
        }
    }
}
