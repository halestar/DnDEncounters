package net.kalinec.dndencounters.monsters;

import java.io.Serializable;

public class MonsterAbility implements Serializable {
    private String name;
    private String description;
	
	
	public MonsterAbility(String name, String description)
	{
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
