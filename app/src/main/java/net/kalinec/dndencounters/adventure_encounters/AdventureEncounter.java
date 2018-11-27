package net.kalinec.dndencounters.adventure_encounters;

import android.util.Log;

import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.parties.Party;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdventureEncounter implements Serializable {

    public void setActors(List<AdventureEncounterActor> actors) {
        this.actors = actors;
    }

    public final static String PASSED_ADVENTURE_ENCOUNTER = "PASSED_ADVENTURE_ENCOUNTER";
    private Party pcs;
    private Encounter encounter;
    private List<AdventureEncounterActor> actors;
    private List<AdventureEncounterTurn> completedTurns;
    private AdventureEncounterTurn currentTurn;
    private boolean completed, setup;

    public Encounter getEncounter() {
        return encounter;
    }

    private int turnNumber;

    public AdventureEncounterTurn getCurrentTurn() {
        return currentTurn;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isSetup() {
        return setup;
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

    public void removeActor(AdventureEncounterActor a)
    {
        actors.remove(a);
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
        if(currentTurn.isCompleted() && !completed)
        {
            completedTurns.add(currentTurn);
            turnNumber++;
            currentTurn = new AdventureEncounterTurn(actors, turnNumber);
        }
        return currentTurn;
    }

    public void complete()
    {
        completedTurns.add(currentTurn);
        currentTurn = null;
        completed = true;
    }

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
}
