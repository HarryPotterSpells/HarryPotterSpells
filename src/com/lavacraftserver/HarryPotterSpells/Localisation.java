package com.lavacraftserver.HarryPotterSpells;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

/**
 * A class that manages localisation within the plugin
 */
public class Localisation {
    private Properties lang;
    private File properties, base, defaultt;
    private boolean once = false;
    
    /**
     * Constructs the {@link Localisation} class
     * @param plugin an instance of {@link HPS}
     */
    public Localisation() {
        load();
    }
    
    /**
     * Loads or reloads the current language
     */
    public void load() {
        lang = new Properties();
        base = new File(HPS.Plugin.getDataFolder(), "Language Files");
        properties = new File(base, HPS.Plugin.getConfig().getString("Language", "us-english") + ".properties");
        defaultt = new File(base, "us-english.properties");
                
        if(!defaultt.exists()) {            
            try {
                defaultt.getParentFile().mkdirs();
                defaultt.createNewFile();
                
                Files.copy(new InputSupplier<InputStream>() {

                    @Override
                    public InputStream getInput() throws IOException {
                        return HPS.class.getResourceAsStream("/us-english.properties");
                    }
                    
                }, defaultt);
            } catch (IOException e) {
                HPS.PM.log(Level.WARNING, "Could not copy the default language file.");
                HPS.PM.debug(e);
            }
        }
            
        try {
            lang.load(new FileInputStream(properties));
        } catch (FileNotFoundException e) { // Specifed lang file does not exist. Revert to default once.            
            HPS.PM.log(Level.WARNING, "Could not find the language file for language " + HPS.Plugin.getConfig().getString("Language", "us-english") + ". Reverting to default language...");
            HPS.PM.debug(e);
            if(!once) {
                once = true;
                load();
            }
            return;
        } catch (IOException e) { // Panic and tell the user stuff has gone wrong
            HPS.PM.log(Level.SEVERE, "Could not load the language file. Plugin will not function.", "Disabling plugin...");
            Bukkit.getServer().getPluginManager().disablePlugin(HPS.Plugin);
            return;
        }
        
        HPS.PM.debug(getTranslation("dbgLanguageLoaded", properties.getName()));
    }
    
    /**
     * Gets a string from the currently loaded language file
     * @param key the translation to get
     * @param args objects used in {@link String#format(String, Object...)}
     * @return the translation or {@code null} if not found
     */
    public String getTranslation(String key, Object... args) {
        return String.format(lang.getProperty(key), args);
    }

}
