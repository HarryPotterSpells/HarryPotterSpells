package com.lavacraftserver.HarryPotterSpells.Spells;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;

/**
 * An abstract class representing a Spell
 */
public abstract class Spell {
	
	@config private String name;
	@config private String description;
	@config private int range;
	@config private boolean goThroughWalls;
	@config private int cooldown;
	
	HPS HPS;
	
	/**
	 * Constructs a {@link Spell}
	 * @param plugin an instance of {@link HPS}s
	 */
	public Spell(HPS plugin) {
	    HPS = plugin;
	}

	/**
	 * Called when a spell is cast
	 * @param p the player who cast the spell
	 * @return {@code true} if the spell was a success
	 */
	public abstract boolean cast(Player p);

	/**
	 * Teaches the spell to a target
	 * @param sender the person who wants to teach
	 * @param target the person who will be taught
	 */
	public void teach(Player sender, Player target) {
		if(target != null) {
			if(playerKnows(target))
				HPS.PM.warn(sender, target.getName() + " already knows that spell!");
			else {
				teach(target);
				HPS.PM.tell(sender, "You have taught " + target.getName() + " the spell " + getName() + ".");
			}
		} else
			HPS.PM.warn(sender, "The player was not found.");
	}

	/**
	 * Teaches the spell to a player
	 * @param p the player
	 */
	public void teach(Player p) {
		List<String> list = HPS.PlayerSpellConfig.getStringListOrEmpty(p.getName());
		list.add(getName());
		HPS.PlayerSpellConfig.getPSC().set(p.getName(), list);
		HPS.PlayerSpellConfig.savePSC();
	}	

	/**
	 * Gets whether a player knows this spell
	 * @param p the player
	 * @return {@code true} if the player knows this spell
	 */
	public boolean playerKnows(Player p){
		List<String> list = HPS.PlayerSpellConfig.getStringListOrEmpty(p.getName());
		return list.contains(getName());
	}

	/**
	 * Makes a player forget the spell
	 * @param p the player
	 */
	public void unTeach(Player p) {
		List<String> list = HPS.PlayerSpellConfig.getStringListOrEmpty(p.getName());
		list.remove(getName());
		HPS.PlayerSpellConfig.getPSC().set(p.getName(), list);
		HPS.PlayerSpellConfig.savePSC();
	}

	/**
	 * Gets the name of this spell
	 * @return the spell's name
	 */
	public String getName(){
		for(Annotation a : this.getClass().getAnnotations()) {
			if(a instanceof spell){
				spell s = (spell) a;
				if(!s.name().equals(""))
					return s.name();
			}
		}
		return getClass().getSimpleName();
	}

	/**
	 * Gets the description of this spell
	 * @return the description
	 */
	public String getDescription(){
		for(Annotation a : this.getClass().getAnnotations()) {
			if(a instanceof spell) {
				spell s = (spell) a;
				return s.description();
			}
		}
		return null;
	}

	/**
	 * The annotation required for a spell to be used. <br>
	 * This annotation contains all the infomation needed for using a spell
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface spell {
		String name() default ""; //"" defaults to class name
		String description() default "A mysterious spell";
		int range() default 25;
		boolean goThroughWalls() default false;
		int cooldown() default 60;
		Material icon() default Material.STICK;
	}
	
	/**
	 * Gets the icon for this spell
	 * @return the icon as a {@link Material}
	 */
	public Material getIcon() {
	    for(Annotation a : this.getClass().getAnnotations()) {
            if(a instanceof spell) {
                spell s = (spell) a;
                return s.icon();
            }
        }
        return Material.STICK;
	}
	
	/**
	 * Gets whether the spell can be cast through walls
	 * @return {@code true} if the spel can be cast through walls.
	 */
	public boolean canBeCastThroughWalls() {
		for(Annotation a : this.getClass().getAnnotations()) {
			if(a instanceof spell) {
				spell s = (spell) a;
				return s.goThroughWalls();
			}
		}
		return false;
	}
	
	/**
	 * Gets the range of the spell
	 * @return the range
	 */
	public int getRange() {
		for(Annotation a : this.getClass().getAnnotations()) {
			if(a instanceof spell){
				spell s = (spell) a;
				return s.range();
			}
		}
		return 25;
	}
	
	/**
	 * Gets the cool down of the spell for a player
	 * @return the cool down
	 */
	public int getCoolDown(Player p) {
		for(Annotation a : this.getClass().getAnnotations()) {
			if(a instanceof spell) {
				if(p.hasPermission(HPS.SpellManager.NO_COOLDOWN_ALL_1) || p.hasPermission(HPS.SpellManager.NO_COOLDOWN_ALL_2) || p.hasPermission("HarryPotterSpells.nocooldown." + getName()))
					return 0;
				spell s = (spell) a;
				int cooldown;
				if(HPS.Plugin.getConfig().contains("cooldowns." + s.name().toLowerCase()))
					cooldown = HPS.Plugin.getConfig().getInt("cooldowns." + s.name().toLowerCase());
				else
					cooldown = s.cooldown();
				
				return cooldown == -1 ? s.cooldown() : cooldown;
			}
		}
		return 60;
	}
	
	/**
	 * A utility method used to shorten the retrival of something from the spells confuration section
	 * @param key the key to the value relative to {@code spells.[spell name].}
	 * @param defaultt the nullable value to return if nothing was found
	 * @return the object found at that location
	 */
	public Object getConfig(String key, @Nullable Object defaultt) {
	    return defaultt == null ? HPS.Plugin.getConfig().get("spells." + getName() + "." + key) : HPS.Plugin.getConfig().get("spells." + getName() + "." + key, defaultt);
	}
	
	/**
	 * Gets a time from the spells configuration as formatted by the following table: <br>
	 * Default: seconds <br>
	 * {@code endsWith("t")}: ticks
	 * @param key the key to the value relative to {@code spells.[spell name ].}
	 * @param defaultt the nullable value to return if nothing was found
	 * @return a {code long} with the amount of ticks the time specified
	 */
	public long getTime(String key, @Nullable long defaultt) {
	    String durationString = (String) getConfig(key, "");
	    
	    if(durationString.equals(""))
	        return defaultt;
	    
        int duration = 0;

        if (durationString.endsWith("t")) {
            String ticks = durationString.substring(0, durationString.length() - 1);
            duration = Integer.parseInt(ticks);
        } else
            duration = Integer.parseInt(durationString) * 20;
        
        return duration;
	}
	
	/**
	 * @deprecated This no longer returns the name of a spell. <br>
	 * Use: {@link Spell#getName()}
	 * @return {@code null} always
	 */
	@Override
	@Deprecated
	public String toString() {
	    return null;
	}
	
	
////////Used for saving any config options attached to this command
	
public ConfigurationSection save(ConfigurationSection c){
	for(Field f:this.getClass().getFields()){
		if(f.isAnnotationPresent(config.class)){
			try{
				
			if(f.get(this) instanceof Location){
c.set(f.getName() + ".world", ((Location)f.get(this)).getWorld().getName());				
c.set(f.getName() + ".x", ((Location)f.get(this)).getX());
c.set(f.getName() + ".y", ((Location)f.get(this)).getY());
c.set(f.getName() + ".z", ((Location)f.get(this)).getZ());
			}else{
c.set(f.getName(), f.get(this));
			}
			
			}catch(Exception e){
				
			}
		}
		}
	return c;
}

public void load(ConfigurationSection c){
	for(Field f:this.getClass().getFields()){
		if(f.isAnnotationPresent(config.class)&&c.isSet(f.getName())){
			try{
				
				if(f.get(this) instanceof String){
					f.set(this, c.getString(f.getName()));
				}else if(f.get(this) instanceof Long){
					f.set(this, c.getLong(f.getName()));
				}else if(f.get(this) instanceof Integer){
					f.set(this, c.getInt(f.getName()));
				}else if(f.get(this) instanceof Double){
					f.set(this, c.getDouble(f.getName()));
				}else if(f.get(this) instanceof Boolean){
					f.set(this, c.getBoolean(f.getName()));
				}else if(f.getType()==Location.class){
					f.set(this, new Location(
							Bukkit.getServer().getWorld(c.getString(f.getName() + ".world")),
							c.getDouble(f.getName() + ".x"),
							c.getDouble(f.getName() + ".y"),
							c.getDouble(f.getName() + ".z")
							));
				}
			}catch(Exception e){
				
			}
		}
		}

}

@Retention(RetentionPolicy.RUNTIME) public @interface config{
}
	
}
