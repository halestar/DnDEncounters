package net.kalinec.dndencounters.spells;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;

public class Spell implements Serializable
{
    public final static String PASSED_SPELL = "PASSED_SPELL";
    private static final String BLANK = "Unk.";
	private String name, description, page, range, components, material, duration, casting_time,
			level, spellClass, higher_level, school;
	private boolean ritual, concentration;
    public Spell(JSONObject spell)
    {
        try{name = spell.getString("name");}catch (JSONException e){name = BLANK;}
	    try
	    {
		    description = spell.getString("desc");
	    }
	    catch (JSONException e)
	    {
		    description = BLANK;
	    }
        try{page = spell.getString("page");}catch (JSONException e){page = BLANK;}
        try{range = spell.getString("range");}catch (JSONException e){range = BLANK;}
        try{components = spell.getString("components");}catch (JSONException e){components = BLANK;}
        try{material = spell.getString("material");}catch (JSONException e){material = BLANK;}
	    try
	    {
		    ritual = (spell.getString("ritual").equals("yes"));
	    }
	    catch (JSONException e)
	    {
		    ritual = false;
	    }
        try{duration = spell.getString("duration");}catch (JSONException e){duration = BLANK;}
	    try
	    {
		    concentration = (spell.getString("concentration").equals("yes"));
	    }
	    catch (JSONException e)
	    {
		    concentration = false;
	    }
        try{casting_time = spell.getString("casting_time");}catch (JSONException e){casting_time = BLANK;}
        try{level = spell.getString("level");}catch (JSONException e){level = BLANK;}
	    try
	    {
		    spellClass = spell.getString("class");
	    }
	    catch (JSONException e)
	    {
		    spellClass = BLANK;
	    }
	    try
	    {
		    higher_level = spell.getString("higher_level");
	    }
	    catch (JSONException e)
	    {
		    higher_level = null;
	    }
	    try
	    {
		    school = spell.getString("school");
	    }
	    catch (JSONException e)
	    {
		    school = BLANK;
	    }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
	
	public String getSchool()
	{
		return school;
	}
    
    public String getPage() {
        return page;
    }

    public String getRange() {
        return range;
    }

    public String getComponents() {
        return components;
    }

    public String getMaterial() {
        return material;
    }
	
	public boolean isRitual()
	{
        return ritual;
    }
	
	public String getDuration() {
        return duration;
    }
	
	public boolean isConcentration()
	{
        return concentration;
    }
	
	public String getCasting_time() {
        return casting_time;
    }

    public String getLevel() {
        return level;
    }

    public String getSpellClass() {
        return spellClass;
    }

    public String getHigher_level() {
        return higher_level;
    }
	
	@NonNull
    @Override
    public String toString() {
        return "Spell{" +
               "name='" + name + '\'' +
               ", level='" + level + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spell spell = (Spell) o;
        return Objects.equals(name, spell.name) &&
                Objects.equals(description, spell.description) &&
                Objects.equals(page, spell.page) &&
                Objects.equals(range, spell.range) &&
                Objects.equals(components, spell.components) &&
                Objects.equals(material, spell.material) &&
                Objects.equals(ritual, spell.ritual) &&
                Objects.equals(duration, spell.duration) &&
                Objects.equals(concentration, spell.concentration) &&
                Objects.equals(casting_time, spell.casting_time) &&
                Objects.equals(level, spell.level) &&
                Objects.equals(spellClass, spell.spellClass) &&
                Objects.equals(higher_level, spell.higher_level);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, description, page, range, components, material, ritual, duration, concentration, casting_time, level, spellClass, higher_level);
    }
}
