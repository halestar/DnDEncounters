package net.kalinec.dndencounters.custom_monsters;

import net.kalinec.dndencounters.dice.DiceParser;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.monsters.MonsterAbility;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class CustomMonster extends Monster implements Serializable
{
    private UUID uuid;
    private long dbId;

    public CustomMonster(int mid, JSONObject stats)
    {
        super(mid, stats);
        uuid = UUID.randomUUID();
        this.mid = -1;
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
        this.mid = -1;
    }

    public void setHp(int hp)
    {
        this.hp = hp;
    }

    public void setCr(String cr) {
        this.cr = (cr.equals("null")? "": cr);
    }

    public void setMonsterType(String monsterType) {
        this.monsterType = (monsterType.equals("null")? "": monsterType);
    }
    public void setMonsterName(String monsterName) {
        this.name = (monsterName.equals("null")? "": monsterName);
    }

    public void setMonsterSize(String monsterSize) {
        this.monsterSize = (monsterSize.equals("null")? "": monsterSize);
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

    public void setActions(ArrayList<MonsterAbility> actions) {
        this.actions = actions;
    }

    public void setAc(int ac) {
        this.ac = ac;
    }

    public void setHitDice(DiceParser hitDice) {
        this.hitDice = hitDice;
    }
    public void setSpeed(String speed) {
        this.speed = (monsterSize.equals("null")? "": monsterSize);
    }

    public void setAlignment(String alignment)
    {
        this.alignment = (alignment.equals("null")? "": alignment);
    }

    public void setResistances(String resistances)
    {
        this.resistances = (resistances.equals("null")? "": resistances);
    }

    public void setImmunities(String immunities)
    {
        this.immunities = (immunities.equals("null")? "": immunities);
    }

    public void setVulnerabilities(String vulnerabilities)
    {
        this.vulnerabilities = (vulnerabilities.equals("null")? "": vulnerabilities);
    }

    public void setLanguages(String languages)
    {
        this.languages = (languages.equals("null")? "": languages);
    }

    public void setSenses(String senses)
    {
        this.senses = (senses.equals("null")? "": senses);
    }

    public void setLegendaryAbilities(
            ArrayList<MonsterAbility> legendaryAbilities)
    {
        this.legendaryAbilities = legendaryAbilities;
    }

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;
		CustomMonster that = (CustomMonster) o;
		return Objects.equals(uuid, that.uuid);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), uuid);
	}

	public long getDbId()
    {
        return dbId;
    }

    public void setDbId(long dbId)
    {
        this.dbId = dbId;
    }
}
