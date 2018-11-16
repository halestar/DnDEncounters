package net.kalinec.dndencounters.encounter;

import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.monsters.MonsterToken;

public class EncounterMonster implements  EncounterActor{
    private Monster monster;
    private MonsterToken token;
    private int initiative, status;
    private boolean hasActed;

    public EncounterMonster(Monster monster, MonsterToken token) {
        this.monster = monster;
        this.token = token;
    }

    public int getInitiative() {

        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int getActorType() {
        return EncounterActor.MONSTER_ACTOR;
    }

    @Override
    public boolean hasActed() {
        return hasActed;
    }

    @Override
    public void setHasActed(boolean hasActed) {
        this.hasActed = hasActed;
    }

    public Monster getMonster() {
        return monster;
    }

    public MonsterToken getToken() {
        return token;
    }
}
