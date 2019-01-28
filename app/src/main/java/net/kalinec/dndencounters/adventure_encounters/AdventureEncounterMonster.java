package net.kalinec.dndencounters.adventure_encounters;

import android.support.annotation.NonNull;

import net.kalinec.dndencounters.monster_tokens.MonsterToken;
import net.kalinec.dndencounters.monsters.Monster;

import java.io.Serializable;
import java.util.UUID;

public class AdventureEncounterMonster implements AdventureEncounterActor, Serializable
{
    public static final String PASSED_ENCOUNTER_MONSTERS = "PASSED_ENCOUNTER_MONSTERS";
    
    private Monster monster;
    private MonsterToken token;
    private int initiative, status;
    private boolean hasActed;
    private int hp;
    private UUID uuid;
    
    public void setToken(MonsterToken token)
    {
        this.token = token;
    }
    
    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    private int maxHp;

    public AdventureEncounterMonster(Monster monster, MonsterToken token) {
        this.monster = monster;
        this.token = token;
        uuid = UUID.randomUUID();
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

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String getName() {
        return monster.getName();
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
	
	@NonNull
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
