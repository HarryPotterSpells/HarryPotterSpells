/**
 * 
 */
package com.lavacraftserver.HarryPotterSpells.api;

import java.io.InputStream;
import java.lang.reflect.Field;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SpellDescriptionFile {
	FileConfiguration c;
	@Load(true)
	String main;
	@Load(true)
	String name;
	public SpellDescriptionFile (InputStream s){
		c = YamlConfiguration.loadConfiguration(s);
		//Reflection.
		for (Field field : this.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Load.class)) {
				String path = field.getName();
				try {
					if (c.isSet(path)) {
						field.set(this, c.get(path));
					} else {
						if (field.get(this) != null){
							c.set(path, field.get(this));
						}
						else if (field.getAnnotation(Load.class).value()){
							throw new InvalidSpellException(c.getCurrentPath()+"."+path +" in "+ c.getName() + " for spell: "+name+" may not be null!");
						}
					}
				} catch (IllegalAccessException ex) {

				}
			}
		}
	}
	/**
	 * @return the c
	 */
	public FileConfiguration getC() {
		return c;
	}
	/**
	 * @return the main
	 */
	public String getMain() {
		return main;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	

}
