package net.kalinec.dndencounters.adventure_encounters;

import java.io.Serializable;
import java.util.List;

public class AdventureEncounterTurn implements Serializable
{
    private List<AdventureEncounterActor> actors;
    private int turnNumber;
    private int currentInitiative;
    private AdventureEncounterActor currentActor;
    private boolean completed;

    private int highestInitiative()
    {
        int highest = 0;
        for(AdventureEncounterActor p: actors)
        {
            if(highest < p.getInitiative() && !p.hasActed())
                highest = p.getInitiative();
        }
        return highest;
    }

    private AdventureEncounterActor getActiveActor()
    {
        for(AdventureEncounterActor a: actors)
        {
            if(a.getInitiative() == currentInitiative && !a.hasActed())
                return a;
        }
        return null;
    }

    public AdventureEncounterTurn(List<AdventureEncounterActor> actors, int turnNumber) {
        this.actors = actors;
        this.turnNumber = turnNumber;

        currentInitiative = this.highestInitiative();
        currentActor = this.getActiveActor();
        completed = this.currentActor.hasActed();
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public int getCurrentInitiative() {
        return currentInitiative;
    }

    public AdventureEncounterActor getCurrentActor() {
        return currentActor;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void completeRound()
    {
        currentActor.setHasActed(true);
        currentInitiative = highestInitiative();
        currentActor = getActiveActor();
        if(currentActor == null)
            completed = true;
    }

    @Override
    public String toString() {
        return "AdventureEncounterTurn{" +
                "actors=" + actors +
                ", turnNumber=" + turnNumber +
                ", currentInitiative=" + currentInitiative +
                ", currentActor=" + currentActor +
                ", completed=" + completed +
                '}';
    }
}
