package net.kalinec.dndencounters.modules;

import net.kalinec.dndencounters.encounters.Encounter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Module implements Serializable
{
	public final static String PASSED_MODULE = "PASSED_MODULE";
	public final static String PASSED_MODULES= "PASSED_MODULES";
	private String moduleName;
	private String moduleDescription;
	private int tier;
	private int optimizedLevel;
	private ArrayList<Encounter> encounters;
	private UUID uuid;
	private long dbId;

	public Module(String moduleName)
	{
		this.moduleName = moduleName;
		this.uuid = UUID.randomUUID();
	}

	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}

	public String getModuleName()
	{
		return moduleName;
	}

	public String getModuleDescription()
	{
		return moduleDescription;
	}

	public void setModuleDescription(String moduleDescription)
	{
		this.moduleDescription = moduleDescription;
	}

	public int getTier()
	{
		return tier;
	}

	public void setTier(int tier)
	{
		this.tier = tier;
	}

	public int getOptimizedLevel()
	{
		return optimizedLevel;
	}

	public void setOptimizedLevel(int optimizedLevel)
	{
		this.optimizedLevel = optimizedLevel;
	}

	public ArrayList<Encounter> getEncounters()
	{
		return encounters;
	}

	public void setEncounters(ArrayList<Encounter> encounters)
	{
		this.encounters = encounters;
	}

	public void addEncounter(Encounter e)
	{
		encounters.add(e);
	}

	public void removeEncounter(Encounter e)
	{
		encounters.remove(e);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Module module = (Module) o;
		return Objects.equals(uuid, module.uuid);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(uuid);
	}

	@Override
	public String toString()
	{
		return "Module{" +
		       "moduleName='" + moduleName + '\'' +
		       ", moduleDescription='" + moduleDescription + '\'' +
		       ", tier=" + tier +
		       ", optimizedLevel=" + optimizedLevel +
		       ", encounters=" + encounters +
		       ", uuid=" + uuid +
		       '}';
	}

	public UUID getUuid()
	{
		return uuid;
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
