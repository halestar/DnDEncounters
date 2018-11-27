package net.kalinec.dndencounters.adventure_encounters;

import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.monster_tokens.MonsterToken;

import java.io.Serializable;

public class AdventureEncounterMonster implements AdventureEncounterActor, Serializable
{
    public static final String PASSED_ENCOUNTER_MONSTER = "PASSED_ENCOUNTER_MONSTER";
    public static final String PASSED_ENCOUNTER_MONSTERS = "PASSED_ENCOUNTER_MONSTERS";
    
    private Monster monster;
    private MonsterToken token;
    private int initiative, status;
    private boolean hasActed;
    private int hp;

    public AdventureEncounterMonster(Monster monster, MonsterToken token) {
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
        return AdventureEncounterActor.MONSTER_ACTOR;
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    @Override
    public String toString() {
        return "AdventureEncounterMonster{" +
                "monster=" + monster +
                ", token=" + token +
                ", initiative=" + initiative +
                ", status=" + status +
                ", hasActed=" + hasActed +
                ", hp=" + hp +
                '}';
    }
}
