package net.kalinec.dndencounters.custom_monsters;

import net.kalinec.dndencounters.dice.DiceParser;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.monsters.MonsterAbility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class CustomMonster extends Monster
{
    private UUID uuid;

    public CustomMonster(int mid, JSONObject stats)
    {
        super(mid, stats);
        uuid = UUID.randomUUID();
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public CustomMonster(String name)
    {
        super();
        this.name = name;
        uuid = UUID.randomUUID();
    }

    public void setHp(int hp)
    {
        this.hp = hp;
    }

    public void setCr(String cr) {
        this.cr = cr;
    }

    public void setMonsterType(String monsterType) {
        this.monsterType = monsterType;
    }
    public void setMonsterName(String monsterName) {
        this.name = monsterName;
    }

    public void setMonsterSize(String monsterSize) {
        this.monsterSize = monsterSize;
    }

    public void setStr(int str) {
        this.str = str;
        determineMods();
    }

    public void setDex(int dex) {
        this.dex = dex;
        determineMods();
    }

    public void setCon(int con) {
        this.con = con;
        determineMods();
    }

    public void setIntel(int intel) {
        this.intel = intel;
        determineMods();
    }

    public void setWis(int wis) {
        this.wis = wis;
        determineMods();
    }

    public void setCha(int cha) {
        this.cha = cha;
        determineMods();
    }

    public void setSpecialAbilities(ArrayList<MonsterAbility> specialAbilities) {
        this.specialAbilities = specialAbilities;
    }

    public void setAc(int ac) {
        this.ac = ac;
    }

    public void setHitDice(DiceParser hitDice) {
        this.hitDice = hitDice;
    }
}