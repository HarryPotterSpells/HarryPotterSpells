package com.lavacraftserver.HarryPotterSpells.Spells;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

public abstract class Spell {
	protected HarryPotterSpells plugin;
	
	public Spell(HarryPotterSpells instance){
		plugin=instance;
	}
	
	@config private String name;
	@config private String description;
	@config private int range;
	@config private boolean goThroughWalls;

	public abstract void cast(Player p);

	public void teach(Player sender, Player target){
		if(target != null) {
			if(playerKnows(target)) {
				plugin.PM.warn(sender, target.getName() + " already knows that spell!");
			} else {
				teach(target);
				plugin.PM.tell(sender, "You have taught " + target.getName() + " the spell " + toString() + ".");
			}
		} else {
			plugin.PM.warn(sender, "The player was not found.");
		}
	}

	public void teach(Player p){
		List<String> list = plugin.PlayerSpellConfig.getPSC().getStringList(p.getName());
		list.add(toString());
		plugin.PlayerSpellConfig.getPSC().set(p.getName(), list);
		plugin.PlayerSpellConfig.savePSC();
		plugin.PM.tell(p, "You have been taught " + toString());
	}	

	public boolean playerKnows(Player p){
		List<String> list = plugin.PlayerSpellConfig.getPSC().getStringList(p.getName());
		if(list.contains(toString())) {
			return true;
		} else {
			return false;
		}
	}

	public void unTeach(Player p){
		List<String> list = plugin.PlayerSpellConfig.getPSC().getStringList(p.getName());
		list.remove(toString());
		plugin.PlayerSpellConfig.getPSC().set(p.getName(), list);
		plugin.PlayerSpellConfig.savePSC();
		plugin.PM.tell(p, "You have forgotten " + toString());
	}
	public String toString(){
		return this.getClass().getSimpleName();
	}

	public String getName(){
		for(Annotation a:this.getClass().getAnnotations()){
			if(a instanceof spell){
				spell s=(spell)a;
				if(s.name()!=""){
					return s.name();
				}
			}
		}
		return toString();
	}
	
	public String getInternalName(){
		return toString();
	}

	public String getDescription(){
		for(Annotation a:this.getClass().getAnnotations()){
			if(a instanceof spell){
				spell s=(spell)a;
				return s.description();
			}
		}
		return null;
	}


	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface spell {
		String name() default ""; //"" defaults to class name
		String description() default "A mysterious spell";
		int range() default 25;
		boolean goThroughWalls() default false;
	}
	
	public boolean canBeCastThroughWalls() {
		for(Annotation a:this.getClass().getAnnotations()){
			if(a instanceof spell){
				spell s=(spell)a;
				return s.goThroughWalls();
			}
		}
		return false;
	}
	
	public int getRange() {
		for(Annotation a:this.getClass().getAnnotations()){
			if(a instanceof spell){
				spell s=(spell)a;
				return s.range();
			}
		}
		return 25;
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
							plugin.getServer().getWorld(c.getString(f.getName() + ".world")),
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
