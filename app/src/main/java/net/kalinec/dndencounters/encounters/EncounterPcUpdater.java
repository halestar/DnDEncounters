package net.kalinec.dndencounters.encounters;

import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterActor;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterPlayer;
import net.kalinec.dndencounters.monster_tokens.MonsterToken;
import net.kalinec.dndencounters.players.Players;

public class EncounterPcUpdater
{
	private int initiative, initiativePos, ac, hp, pp, spellDc;
	private AdventureEncounterPlayer pc;

	public EncounterPcUpdater(AdventureEncounterPlayer pc)
	{
		this.pc = pc;
		this.initiative = pc.getInitiative();
		this.initiativePos = pc.getInitiativePosition();
		this.ac = pc.getPc().getAc();
		this.hp = pc.getPc().getHp();
		this.pp = pc.getPc().getPp();
		this.spellDc = pc.getPc().getSpellDc();
	}


	public AdventureEncounterPlayer getPc()
	{
		return pc;
	}

	public int getInitiative()
	{
		return initiative;
	}

	public void setInitiative(int initiative)
	{
		this.initiative = initiative;
	}

	public int getInitiativePos()
	{
		return initiativePos;
	}

	public void setInitiativePos(int initiativePos)
	{
		this.initiativePos = initiativePos;
	}

	public int getAc()
	{
		return ac;
	}

	public void setAc(int ac)
	{
		this.ac = ac;
	}

	public int getHp()
	{
		return hp;
	}

	public void setHp(int hp)
	{
		this.hp = hp;
	}

	public int getPp()
	{
		return pp;
	}

	public void setPp(int pp)
	{
		this.pp = pp;
	}

	public int getSpellDc()
	{
		return spellDc;
	}

	public void setSpellDc(int spellDc)
	{
		this.spellDc = spellDc;
	}

	public AdventureEncounterPlayer getUpdatedPc()
	{
		pc.setInitiative(initiative);
		pc.setInitiativePosition(initiativePos);
		pc.getPc().setAc(ac);
		pc.getPc().setHp(hp);
		pc.getPc().setPp(pp);
		pc.getPc().setSpellDc(spellDc);
		return pc;
	}

}
