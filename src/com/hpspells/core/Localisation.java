package com.hpspells.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

import org.bukkit.Bukkit;

/**
 * A class that manages localisation within the plugin
 */
public class Localisation {
	private HashMap<Language, File> languages = new HashMap<Language, File>();
	private HashMap<Language, Properties> loadedProperties = new HashMap<Language, Properties>();
    private Properties defaultLang, activeLang;
    //private Properties lang;
    //private ArrayList<File> files = new ArrayList<File>();
    private File langFolder;
    //private File properties, en;
    //private boolean once = false;
    private HPS HPS;

    /**
     * Constructs the {@link Localisation} class
     *
     * @param instance an instance of {@link HPS}
     */
    public Localisation(HPS instance) {
        this.HPS = instance;
        load();
    }
    
    /**
     * Generates and loads the language files.
     */
    public void load() {
    	langFolder = new File(HPS.getDataFolder(), "Language Files");
    	if (!langFolder.exists()) {
    		langFolder.mkdir();
    	}
    	this.registerLang(Language.ENGLISH, new File(langFolder, "us-english.properties"));
        this.registerLang(Language.DUTCH, new File(langFolder, "nl-dutch.properties"));
        this.loadDefaultLang();
        this.loadLang(Language.getLanuage(HPS.getConfig().getString("language")));
    }
    
    /**
     * Registers a language that HPS can support
     * @param language The language to register
     * @param file The language file
     */
    public void registerLang(Language language, File file) {
    	languages.put(language, file);
    	loadedProperties.put(language, new Properties());
    	this.generateFile(file, file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1));
    }
    
    /**
     * Loads the specified language from file
     * @param language The language to load
     */
    public void loadLang(Language language) {
    	if (!languages.containsKey(language) && loadedProperties.containsKey(language)) {
    		HPS.PM.log(Level.SEVERE, "An error occurred while trying to load the language file");
    		HPS.PM.log(Level.SEVERE, "The language file may not exist or has not been processed");
    	}
    	try {
			activeLang = loadedProperties.get(language);
			activeLang.load(new FileInputStream(languages.get(language)));
		} catch (FileNotFoundException e) {
			HPS.PM.log(Level.WARNING, "Could not find the language file for language " + HPS.getConfig().getString("language") + ". Reverting to default language...");
            HPS.PM.debug(e);
		} catch (IOException e) {
			HPS.PM.log(Level.SEVERE, "Could not load any language file. Plugin will not function.", "Disabling plugin...");
            Bukkit.getServer().getPluginManager().disablePlugin(HPS);
            return;
		}
    }
    
    /**
     * Loads the default language of english
     */
    private void loadDefaultLang() {
    	try {
    		defaultLang = loadedProperties.get(Language.ENGLISH);
    		defaultLang.load(new FileInputStream(languages.get(Language.ENGLISH)));
		} catch (Exception e) {
			HPS.PM.log(Level.SEVERE, "Could not load default language. Plugin will not function.", "Disabling plugin...");
            HPS.PM.debug(e);
            Bukkit.getServer().getPluginManager().disablePlugin(HPS);
            return;
		}
    }
    
    /**
     * Loads all languages available
     */
    public void loadAllLang() {
    	for (Language language : loadedProperties.keySet()) {
    		this.loadLang(language);
    	}
    }

//    /**
//     * Loads or reloads the current language
//     */
//    public void load() {
//        lang = new Properties();
//        defaultLang = new Properties();
//        langFolder = new File(HPS.getDataFolder(), "Language Files");
//        if (!langFolder.exists()) {
//        	langFolder.mkdir();
//        }
//        properties = new File(langFolder, HPS.getConfig().getString("language", "us-english") + ".properties");
//        en = new File(langFolder, "us-english.properties");
//        files.add(properties);
//        files.add(en);
//
//        for (File file : files) {
//        	this.generateFile(file, file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1));
//        }
//        
//        try {
//            lang.load(new FileInputStream(properties));
//            defaultLang.load(new FileInputStream(en));
//        } catch (FileNotFoundException e) { // Specifed lang file does not exist. Revert to default once.
//            HPS.PM.log(Level.WARNING, "Could not find the language file for language " + HPS.getConfig().getString("language") + ". Reverting to default language...");
//            HPS.PM.debug(e);
//            if (!once) {
//                once = true;
//                load();
//            }
//            return;
//        } catch (IOException e) { // Panic and tell the user stuff has gone wrong
//            HPS.PM.log(Level.SEVERE, "Could not load the language file. Plugin will not function.", "Disabling plugin...");
//            Bukkit.getServer().getPluginManager().disablePlugin(HPS);
//            return;
//        }
//
//        HPS.PM.debug(getTranslation("dbgLanguageLoaded", properties.getName()));
//    }

    /**
     * Gets a string from the currently loaded language file
     *
     * @param key  the translation to get
     * @param args objects used in {@link String#format(String, Object...)}
     * @return the translation or {@code null} if not found
     */
    public String getTranslation(String key, Object... args) {
        return activeLang.getProperty(key) == null ? String.format(defaultLang.getProperty(key), args) : String.format(activeLang.getProperty(key), args);
    }

    
    private void generateFile(File file, final String lang) {
    	InputStream stream = HPS.class.getResourceAsStream("/" + lang);
        if (stream == null) {
        	HPS.PM.log(Level.SEVERE, "Could not generate language file " + lang + " Ignore this message if you arent using this language");
        }
        OutputStream resStreamOut = null;
        int readBytes;
        byte[] buffer = new byte[4096];
        try {
            resStreamOut = new FileOutputStream(new File(langFolder, lang));
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
            	stream.close();
				resStreamOut.close();
			} catch (IOException e) {
				HPS.PM.log(Level.SEVERE, "Error occured while generating language files...", "Unable to close streams");
				e.printStackTrace();
			}
        }
        
//    	if (!file.exists()) {
//        	try {
//        		file.createNewFile();
//
//                Files.copy(new InputSupplier<InputStream>() {
//
//                    @Override
//                    public InputStream getInput() throws IOException {
//                        return HPS.class.getResourceAsStream("/" + lang);
//                    }
//
//                }, file);
//            } catch (IOException e) {
//                HPS.PM.log(Level.WARNING, "Could not copy the language file" + lang + ".");
//                HPS.PM.debug(e);
//            }
//        }
    }
    
    /**
     * Enum representing a lanugage
     */
    public enum Language {
    	DUTCH,
    	ENGLISH;
    	
    	/**
    	 * Gets the language from its configuration name
    	 * @param name The configuration language name
    	 * @return Enum of language. ENGLISH if no matches found
    	 */
    	public static Language getLanuage(String name) {
    		if (name.equalsIgnoreCase("nl-dutch")) {
    			return DUTCH;
    		}
    		return ENGLISH;
    	}
    }
}
