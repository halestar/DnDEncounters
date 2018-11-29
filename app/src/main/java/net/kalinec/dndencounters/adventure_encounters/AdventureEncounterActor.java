package net.kalinec.dndencounters.adventure_encounters;

import java.io.Serializable;
import java.util.UUID;

public interface AdventureEncounterActor extends Serializable
{

    public static final int ALIVE = 1;
    public static final int DEAD = 2;

    public static final int PLAYER_ACTOR = 3;
    public static final int MONSTER_ACTOR = 4;
    public void setInitiative(int initiative);
    public int getInitiative();
    public int getStatus();
    public void setStatus(int status);
    public int getActorType();
    public boolean hasActed();
    public void setHasActed(boolean hasActed);
    public UUID getUuid();
    public String getName();
    public int getHp();
}
