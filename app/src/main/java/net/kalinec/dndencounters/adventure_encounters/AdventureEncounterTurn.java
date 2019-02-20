package net.kalinec.dndencounters.adventure_encounters;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class AdventureEncounterTurn implements Serializable
{
    private List<AdventureEncounterActor> actors;
    private int turnNumber;
    private int currentInitiative;
    private AdventureEncounterActor currentActor;
    private boolean completed;

	public static final Comparator<AdventureEncounterActor> INITIATIVE_COMPARATOR = new Comparator<AdventureEncounterActor>() {
		@Override
		public int compare(AdventureEncounterActor a, AdventureEncounterActor b)
		{
			if(a.getInitiative() < b.getInitiative())
				return 1;
			else if(a.getInitiative() > b.getInitiative())
				return -1;
			else if(a.getInitiativePosition() < b.getInitiativePosition())
				return -1;
			else if(a.getInitiativePosition() > b.getInitiativePosition())
				return 1;
			return 0;
		}
	};

    private int highestInitiative()
    {
	    actors.sort(INITIATIVE_COMPARATOR);
        int highest = 0;
        for(AdventureEncounterActor p: actors)
        {
            if(highest <= p.getInitiative() &&
                    !p.hasActed() &&
                    (p.getActorType() == AdventureEncounterActor.PLAYER_ACTOR ||
                            (p.getActorType() == AdventureEncounterActor.MONSTER_ACTOR && p.getStatus() == AdventureEncounterActor.ALIVE)))
                highest = p.getInitiative();
        }
        return highest;
    }

    private AdventureEncounterActor getActiveActor()
    {
        actors.sort(INITIATIVE_COMPARATOR);
        for(AdventureEncounterActor a: actors)
        {
            if(a.getInitiative() == currentInitiative &&
                    !a.hasActed()&&
                    (a.getActorType() == AdventureEncounterActor.PLAYER_ACTOR ||
                            (a.getActorType() == AdventureEncounterActor.MONSTER_ACTOR && a.getStatus() == AdventureEncounterActor.ALIVE)))
                return a;
        }
        return null;
    }
	
	AdventureEncounterTurn(List<AdventureEncounterActor> actors, int turnNumber)
	{
        this.actors = actors;
        this.turnNumber = turnNumber;

        this.currentInitiative = this.highestInitiative();
        this.currentActor = this.getActiveActor();
		assert this.currentActor != null;
        this.completed = this.currentActor.hasActed();
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
	
	@NonNull
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

    public void recalculateInitiative()
    {
	    this.currentInitiative = this.highestInitiative();
	    this.currentActor = this.getActiveActor();
	    assert this.currentActor != null;
	    this.completed = this.currentActor.hasActed();
    }
}
