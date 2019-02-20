package net.kalinec.dndencounters.adventure_encounters;

import android.support.annotation.NonNull;

import net.kalinec.dndencounters.characters.Character;

import java.io.Serializable;
import java.util.UUID;

public class AdventureEncounterPlayer implements AdventureEncounterActor, Serializable
{

    public static final String PASSED_ENCOUNTER_PLAYERS = "PASSED_ENCOUNTER_PLAYERS";
    private Character pc;
    private int initiative, status;
    private boolean hasActed;
    private UUID uuid;
    private int initiativePosition;

    public AdventureEncounterPlayer(Character pc) {
        this.pc = pc;
        this.initiative = 0;
        this.uuid = UUID.randomUUID();
        this.initiativePosition = 1;
    }
    @Override
    public int getInitiative() {
        return initiative;
    }
    @Override
    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }
    @Override
    public int getStatus() {
        return status;
    }
    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int getActorType() {
        return AdventureEncounterActor.PLAYER_ACTOR;
    }

    @Override
    public boolean hasActed() {
        return hasActed;
    }

    public Character getPc() {
        return pc;
    }
    
    @Override
    public void setHasActed(boolean hasActed) {
        this.hasActed = hasActed;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String getName() {
        return pc.getName();
    }

    @Override
    public int getHp() {
        return pc.getHp();
    }

    @Override
    public void setInitiativePosition(int position)
    {
        this.initiativePosition = position;
    }

    @Override
    public int getInitiativePosition()
    {
        return initiativePosition;
    }

    @NonNull
	@Override
	public String toString()
	{
		return "AdventureEncounterPlayer{" +
		       "pc=" + pc +
		       ", initiative=" + initiative +
		       ", status=" + status +
		       ", hasActed=" + hasActed +
		       '}';
	}
	
}
