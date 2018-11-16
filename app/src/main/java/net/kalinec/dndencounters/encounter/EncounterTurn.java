package net.kalinec.dndencounters.encounter;

import java.util.List;

public class EncounterTurn {
    private List<EncounterActor> actors;
    private int turnNumber;
    private int currentInitiative;
    private EncounterActor currentActor;
    private boolean completed;

    private int highestInitiative()
    {
        int highest = 0;
        for(EncounterActor p: actors)
        {
            if(highest < p.getInitiative() && !p.hasActed())
                highest = p.getInitiative();
        }
        return highest;
    }

    private EncounterActor getActiveActor()
    {
        for(EncounterActor a: actors)
        {
            if(a.getInitiative() == currentInitiative && !a.hasActed())
                return a;
        }
        return null;
    }

    public EncounterTurn(List<EncounterActor> actors, int turnNumber) {
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

    public EncounterActor getCurrentActor() {
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
}
