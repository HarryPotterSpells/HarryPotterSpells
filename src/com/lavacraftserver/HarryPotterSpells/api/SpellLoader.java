/**
 * 
 */
package com.lavacraftserver.HarryPotterSpells.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.Validate;
import org.yaml.snakeyaml.error.YAMLException;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

public class SpellLoader {
	HarryPotterSpells p;
	public SpellLoader(HarryPotterSpells p){
		this.p = p;
		loadSpells();
	}
	public void loadSpells(){
		File file = new File(p.getDataFolder()+File.separator+"Spells"+File.separator);
		if (!file.exists())
			file.mkdirs();
		for (File f : file.listFiles()){
			if (!f.isDirectory() && f.getAbsolutePath().endsWith(".jar")){
				loadSpell(f, getDescriptionFile(f));
			}
		}
		
	}
	public Spell newInstance(Class<? extends Spell> clazz){
		try {
			Constructor<? extends Spell> constructor = clazz.getConstructor(HarryPotterSpells.class);
			Spell t = constructor.newInstance(p);
			return t;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch(InvocationTargetException e){
			e.printStackTrace();
		} catch(IllegalAccessException e){
			e.printStackTrace();
		} catch(InstantiationException e){
			e.printStackTrace();
		}
		return null;
	}
	public void loadSpell(File file, SpellDescriptionFile desc){
		Validate.notNull(file, "File Cannot be null."); 
		if (!file.exists() || !file.getAbsolutePath().endsWith(".jar") || file.isDirectory()){
			throw new InvalidSpellException("Spell must not be a directoy, and must be a .jar file.");
		}
        try {
            URL[] urls = new URL[1];
            urls[0] = file.toURI().toURL();
            Class<?> jarClass = Class.forName(desc.getMain(), true, new URLClassLoader(urls, this.getClass().getClassLoader()));
            Class<? extends Spell> type = jarClass.asSubclass(Spell.class);
            p.spellManager.addSpell(newInstance(type));
        } catch (Throwable e) {
        	throw new InvalidSpellException(e);
        } 
		
	}
	
	public static SpellDescriptionFile getDescriptionFile(File file){
		Validate.notNull(file, "File cannot be null.");
		JarFile jar = null;
		InputStream stream = null;
		try {
			jar = new JarFile(file);
			JarEntry entry = jar.getJarEntry("plugin.yml");
			if (entry == null){
				throw new InvalidSpellException(file.getName()+ " is missing a plugin.yml");
			}
			stream = jar.getInputStream(entry);
			return new SpellDescriptionFile(stream);
		}
		catch (IOException ex) {
			throw new InvalidSpellException("Invalid plguin.yml");
		} 
		catch (YAMLException ex) {
			throw new InvalidSpellException("Invalid plugin.yml");
		} 
		finally {
            if (jar != null) {
                try {
                    jar.close();
                } 
                catch (IOException e) {
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } 
                catch (IOException e) {
                }
            }
        }
		
	}

}
