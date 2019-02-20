package net.kalinec.dndencounters.parties;


import android.support.annotation.NonNull;

import net.kalinec.dndencounters.characters.Character;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Party implements Serializable
{
	public final static String PASSED_PARTY = "PASSED_PARTY";
	private Date created;
	private List<Character> members;
	private String name;
	private int apl;
	
	public Date getCreated()
	{
		return created;
	}
	
	public List<Character> getMembers()
	{
		return members;
	}
	
	public int getApl()
	{
		return apl;
	}
	
	public void setMembers(List<Character> members)
	{
		this.members = members;
		updateApl();
		
	}

	public void addMember(Character pc)
	{
		members.add(pc);
		updateApl();
	}

	public void removeMember(Character pc)
	{
		members.remove(pc);
		updateApl();
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Party()
	{
		created = new Date();
		members = new ArrayList<>();
		name = "Unknown Party";
	}
	
	private void updateApl()
	{
		if(members.size() == 0)
			apl = 0;
		else
		{
			int level = 0;
			for (Character pc : members)
				level += pc.getLevel();
			apl = (int) Math.floor(level / members.size());
		}
	}
	
	@NonNull
	@Override
	public String toString() {
		return "Party{" +
		       "created=" + created +
		       ", members=" + members +
		       ", name='" + name + '\'' +
		       ", apl=" + apl +
		       '}';
	}
}
