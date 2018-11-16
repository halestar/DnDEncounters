package net.kalinec.dndencounters.encounter;

import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.parties.Party;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdventureEncounter implements Serializable {

    public void setActors(List<EncounterActor> actors) {
        this.actors = actors;
    }

    public final static String PASSED_ADVENTURE_ENCOUNTER = "PASSED_ADVENTURE_ENCOUNTER";
    private Party pcs;
    private Encounter encounter;
    private List<EncounterActor> actors;
    private List<EncounterTurn> completedTurns;
    private EncounterTurn currentTurn;
    private boolean completed, setup;

    public Encounter getEncounter() {
        return encounter;
    }

    private int turnNumber;

    public EncounterTurn getCurrentTurn() {
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
        //else we must make sure that each actor matches a member of the party and a monster
        for(Character c: pcs.getMembers())
        {
            boolean found = false;
            for(EncounterActor a: actors)
            {
                if(a.getActorType() == EncounterActor.PLAYER_ACTOR && ((EncounterPlayer)a).getPc().equals(c))
                {
                    found = true;
                    break;
                }
            }
            if(!found)
                return false;
        }
        //next, check the monsters
        for(Monster m: encounter.getMonsters())
        {
            boolean found = false;
            for(EncounterActor a: actors)
            {
                if(a.getActorType() == EncounterActor.MONSTER_ACTOR && ((EncounterMonster)a).getMonster().equals(m))
                {
                    found = true;
                    break;
                }
            }
            if(!found)
                return false;
        }
        //else, we arte done.
        return true;
    }

    public void addActor(EncounterActor a)
    {
        actors.add(a);
        setup = isSetUp();
    }

    public void removeActor(EncounterActor a)
    {
        actors.remove(a);
        setup = isSetUp();
    }

    public void beginEncounter()
    {
        if(setup && !completed)
        {
            currentTurn = new EncounterTurn(actors, turnNumber);
        }
    }

    public void nextTurn()
    {
        if(currentTurn.isCompleted() && !completed)
        {
            completedTurns.add(currentTurn);
            turnNumber++;
            currentTurn = new EncounterTurn(actors, turnNumber);
        }
    }

    public void complete()
    {
        completedTurns.add(currentTurn);
        currentTurn = null;
        completed = true;
    }
}
