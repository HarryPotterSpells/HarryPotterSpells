package com.lavacraftserver.HarryPotterSpells;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;

import com.lavacraftserver.HarryPotterSpells.Jobs.EnableJob;

/**
 * A class that manages the {@code PlayerSpellConfig.yml} file. <br>
 * This file is used to store all the spells that players know.
 */
public class PlayerSpellConfig implements EnableJob {
	private FileConfiguration PSC = null;
	private File PSCFile = null;
	
	/**
	 * The current version specifying the format of the {@code PlayerSpellConfig.yml}
	 */
	public static final double CURRENT_VERSION = 0.5d;
	
	/**
	 * Reloads the PlayerSpellConfig
	 */
	public void reloadPSC() {
	    if (PSCFile == null)
	    	PSCFile = new File(HPS.Plugin.getDataFolder(), "PlayerSpellConfig.yml");
	    PSC = YamlConfiguration.loadConfiguration(PSCFile);
	    InputStream defConfigStream = HPS.class.getResourceAsStream("PlayerSpellConfig.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        PSC.setDefaults(defConfig);
	    }
	    savePSC();
	}
	
	/**
	 * Gets the PlayerSpellConfig file
	 * @return the file as a {@link FileConfiguration}
	 */
	public FileConfiguration getPSC() {
	    if (PSC == null)
	        reloadPSC();
	    return PSC;
	}
	
	/**
	 * Saves change made to the PlayerSpellConfig to disk
	 */
	public void savePSC() {
	    if (PSC == null || PSCFile == null) 
	        return;
	    
	    try {
	        getPSC().save(PSCFile);
	    } catch (IOException ex) {
	    	HPS.PM.log(Level.SEVERE, HPS.Localisation.getTranslation("pscCouldNotSave", PSCFile.getName()));
	    	HPS.PM.debug(ex);
	    }
	}
	
	/**
	 * A utility function that is meant to be used instead of {@link FileConfiguration#getStringList(String)}. <br>
	 * This is because the Bukkit version returns {@code null} when no String list is found.
	 * @param string the path to the String list
	 * @return the string list at that location or an empty {@link ArrayList}
	 */
	public List<String> getStringListOrEmpty(String string) {
	    return getPSC().getStringList(string) == null ? new ArrayList<String>() : getPSC().getStringList(string);
	}
	
	@Override
	public void onEnable(PluginManager pm) {
	    Double version = getPSC().getDouble("VERSION_DO_NOT_EDIT", -1d) == -1d ? null : getPSC().getDouble("VERSION_DO_NOT_EDIT", -1d);
	    if(version == null || version < CURRENT_VERSION) {
	        HPS.PM.log(Level.INFO, HPS.Localisation.getTranslation("pscOutOfDate"));
	        
	        // STORE UPDATES HERE
	        
	        if(version == null) { // Updating from unformatted version to version 0.4
	            Map<String, List<String>> newConfig = new HashMap<String, List<String>>(), lists = new HashMap<String, List<String>>();
	            Set<String> css = new HashSet<String>();
	            
	            for(String s : getPSC().getKeys(false)) // This seemingly stupid code is to avoid a ConcurrencyModificationException (google it)
	                css.add(s);
	            
	            for(String s : css)
	                lists.put(s, getPSC().getStringList(s));
	            
	            for(String cs : css) {
	                List<String> list = new ArrayList<String>();
	                for(String spellString : getPSC().getStringList(cs)) {
	                    if(spellString.equals("AlarteAscendare")) {
                            list.add("Alarte Ascendare");
	                    } else if(spellString.equals("AvadaKedavra")) {
	                        list.add("Avada Kedavra");
	                    } else if(spellString.equals("FiniteIncantatem")) {
                            list.add("Finite Incantatem");
	                    } else if(spellString.equals("MagnaTonitrus")) {
                            list.add("Magna Tonitrus");
                        } else if(spellString.equals("PetrificusTotalus")) {
                            list.add("Petrificus Totalus");
                        } else if(spellString.equals("TimeSpell")) {
	                        list.add("Time");
	                    } else if(spellString.equals("TreeSpell")) {
                            list.add("Tree");
	                    } else if(spellString.equals("WingardiumLeviosa")) {
                            list.add("Wingardium Leviosa");
	                    } else {
	                        list.add(spellString);
	                    }
	                }
	                
	                newConfig.put(cs, list);
	            }

	            for(Entry<String, List<String>> ent : newConfig.entrySet()) {
	                getPSC().set(ent.getKey(), ent.getValue());
	            }
	            
	            getPSC().set("VERSION_DO_NOT_EDIT", 0.4d);
	            
	            savePSC();
	            
	            HPS.PM.log(Level.INFO, HPS.Localisation.getTranslation("pscUpdated", "0.4"));
	        }
	        
	    }
	}
	
}
