package net.kalinec.dndencounters.monsters;


import android.support.annotation.NonNull;

import net.kalinec.dndencounters.dice.DiceParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;


public class Monster implements Serializable
{
	public final static String PASSED_MONSTER = "PASSED_MONSTER";
	public static final String CR_ERROR = "Unk.";
	private static final String NAME_ERROR = "Error Monster";
	private int mid;
	private String name, cr, monsterType, monsterSize;
	private int str, dex, con, intel, wis, cha;
	private int str_mod, dex_mod, con_mod, intel_mod, wis_mod, cha_mod;
	private int hp;
	private ArrayList<MonsterAbility> specialAbilities, actions;

	public int getAc() {
		return ac;
	}

	private int ac;
	private DiceParser hitDice;
	
	public String getName()
	{
		return name;
	}
	
	public String getCr()
	{
		return cr;
	}

	private void determineMods()
	{
		str_mod = (int)Math.floor((str - 10) / 2);
		dex_mod = (int)Math.floor((dex - 10) / 2);
		con_mod = (int)Math.floor((con - 10) / 2);
		intel_mod = (int)Math.floor((intel - 10) / 2);
		wis_mod = (int)Math.floor((wis - 10) / 2);
		cha_mod = (int)Math.floor((cha - 10) / 2);
	}

	public String getMonsterType() {
		return monsterType;
	}

	public String getMonsterSize() {
		return monsterSize;
	}

	public ArrayList<MonsterAbility> getSpecialAbilities() {
		return specialAbilities;
	}

	public ArrayList<MonsterAbility> getActions() {
		return actions;
	}

	public Monster(int mid, JSONObject stats)
	{
		this.mid = mid;
		try{name = stats.getString("name");}catch (JSONException e){name = NAME_ERROR;}
		try{cr = stats.getString("challenge_rating");}catch (JSONException e){cr = CR_ERROR;}
		try{ str = stats.getInt("strength"); } catch (JSONException e) { str = 0; }
		try{ dex = stats.getInt("dexterity"); } catch (JSONException e) { dex = 0; }
		try{ con = stats.getInt("constitution"); } catch (JSONException e) { con = 0; }
		try{ intel = stats.getInt("intelligence"); } catch (JSONException e) { intel = 0; }
		try{ wis = stats.getInt("wisdom"); } catch (JSONException e) { wis = 0; }
		try{ cha = stats.getInt("charisma"); } catch (JSONException e) { cha = 0; }
		try{ hp = stats.getInt("hit_points"); } catch (JSONException e) { hp = 0; }
		try{ ac = stats.getInt("armor_class"); } catch (JSONException e) { ac = 0; }
		try{monsterType = stats.getString("type");}catch (JSONException e){monsterType = CR_ERROR;}
		try{monsterSize = stats.getString("size");}catch (JSONException e){monsterSize = CR_ERROR;}
		try
		{
			String hit_dice = stats.getString("hit_dice");
			hitDice = new DiceParser(hit_dice);
		}
		catch (JSONException e)
		{
			hitDice = null;
		}
		determineMods();

		//special abilities.
		try
		{
			JSONArray sAbilities = stats.getJSONArray("special_abilities");
			specialAbilities = new ArrayList<>();
			for(int i = 0; i < sAbilities.length(); i++)
				specialAbilities.add(new MonsterAbility(sAbilities.getJSONObject(i).getString("name"), sAbilities.getJSONObject(i).getString("desc")));
		}
		catch (JSONException e){
			specialAbilities = new ArrayList<>();
		}
		//actions
		try
		{
			JSONArray sActions = stats.getJSONArray("actions");
			actions = new ArrayList<>();
			for(int i = 0; i < sActions.length(); i++)
				actions.add(new MonsterAbility(sActions.getJSONObject(i).getString("name"), sActions.getJSONObject(i).getString("desc")));
		}
		catch (JSONException e){
			actions = new ArrayList<>();
		}

	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Monster monster = (Monster) o;
		return mid == monster.mid;
	}
	
	@Override
	public int hashCode()
	{
		
		return Objects.hash(mid);
	}
	
	@NonNull
	@Override
	public String toString() {
		return "Monster{" +
		       "name='" + name + '\'' +
		       ", dex_mod=" + dex_mod +
		       ", hp=" + hp +
		       '}';
	}

	private String formatMod(int mod)
	{
		if(mod < 0)
			return Integer.toString(mod);
		if(mod == 0)
			return "0";
		return "+" + Integer.toString(mod);
	}

	public String getStrMod() {
		return formatMod(str_mod);
	}

	public String getDexMod() {
		return formatMod(dex_mod);
	}

	public String getConMod() {
		return formatMod(con_mod);
	}

	public String getIntelMod() {
		return formatMod(intel_mod);
	}

	public String getWisMod() {
		return formatMod(wis_mod);
	}


	public int getDex_mod() {
		return dex_mod;
	}
	
	
	public String getChaMod() {
		return formatMod(cha_mod);
	}

	public int getHp() {
		return hp;
	}
	
	public int rollHp()
	{
		return hitDice.result();
	}
}
