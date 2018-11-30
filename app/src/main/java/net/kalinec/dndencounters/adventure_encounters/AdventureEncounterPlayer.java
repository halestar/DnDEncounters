package net.kalinec.dndencounters.adventure_encounters;

import android.support.annotation.NonNull;

import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.players.Player;

import java.io.Serializable;
import java.util.UUID;

public class AdventureEncounterPlayer implements AdventureEncounterActor, Serializable
{

    public static final String PASSED_ENCOUNTER_PLAYERS = "PASSED_ENCOUNTER_PLAYERS";
    private Character pc;
    private Player player;
    private int initiative, status;
    private boolean hasActed;
    private UUID uuid;

    public AdventureEncounterPlayer(Character pc, Player player) {
        this.pc = pc;
        this.player = player;
        this.initiative = 0;
        this.uuid = UUID.randomUUID();
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

    public Player getPlayer() {
        return player;
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
