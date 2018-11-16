package net.kalinec.dndencounters.encounter;

import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.players.Player;

public class EncounterPlayer implements EncounterActor{
    private Character pc;
    private Player player;
    private int initiative, status;
    private boolean hasActed;

    public EncounterPlayer(Character pc, Player player) {
        this.pc = pc;
        this.player = player;
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
        return EncounterActor.PLAYER_ACTOR;
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
}
