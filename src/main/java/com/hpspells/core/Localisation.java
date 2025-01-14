package com.hpspells.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

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
    private HarryPotterSpells HPS;

    /**
     * Constructs the {@link Localisation} class
     *
     * @param instance an instance of {@link HarryPotterSpells}
     */
    public Localisation(HarryPotterSpells instance) {
        this.HPS = instance;
        load();
    }
    
    /**
     * Generates and loads the language files.
     */
    public void load() {
    	langFolder = new File(HPS.getDataFolder(), "Locale");
    	if (!langFolder.exists()) {
    		langFolder.mkdir();
    	}
    	this.registerLang(Language.ENGLISH, new File(langFolder, "us-english.properties"));
        this.registerLang(Language.DUTCH, new File(langFolder, "nl-dutch.properties"));
        this.registerLang(Language.GERMAN, new File(langFolder, "de-german.properties"));
        this.registerLang(Language.SPANISH, new File(langFolder, "es-spanish.properties"));
        this.registerLang(Language.ITALIAN, new File(langFolder, "it-italian.properties"));
        this.registerLang(Language.CHINESE, new File(langFolder, "zh-chinese.properties"));
		this.registerLang(Language.PORTUGUESE, new File(langFolder, "br-portuguese.properties"));
        if (loadDefaultLang())
        	this.loadLang(Language.getLanguage(HPS.getConfig().getString("language")));
    }
    
    /**
     * Registers a language that HPS can support
     * @param language The language to register
     * @param file The language file
     */
    public void registerLang(Language language, File file) {
    	languages.put(language, file);
    	loadedProperties.put(language, new Properties());
    	for (Language lang : languages.keySet())
    		if (!languages.get(lang).exists()) 
    			this.generateFile(file, file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(File.separator) + 1));
    	HPS.PM.debug(file.getAbsolutePath());
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
			activeLang.load(new InputStreamReader(new FileInputStream(languages.get(language)), Charset.forName("UTF-8")));
		} catch (FileNotFoundException e) {
			HPS.PM.log(Level.WARNING, "Could not find the language file for language " + HPS.getConfig().getString("language") + ". Reverting to default language...");
            HPS.PM.debug(e);
		} catch (IOException e) {
			HPS.PM.log(Level.SEVERE, "Could not load any language file. Plugin will not function.", "Disabling plugin...");
			HPS.PM.debug(e);
			HPS.localeState = false;
            Bukkit.getServer().getPluginManager().disablePlugin(HPS);
            return;
		}
    }
    
    /**
     * Loads the default language of english and returns a boolean
     * depending on whether the default language was loaded or not.
     * 
     * @return true if default language was loaded
     */
	private boolean loadDefaultLang() {
    	try {
    		if (!new File(this.langFolder, "us-english.properties").exists()) {
    			HPS.PM.log(Level.INFO, "Attempting to download us-english.properties ...");
    			URL url = new URI("https://raw.githubusercontent.com/leothawne/HarryPotterSpells/master/src/main/resources/us-english.properties").toURL();
    			URLConnection urlc = url.openConnection();
    			ReadableByteChannel rbc = Channels.newChannel(urlc.getInputStream());
    			FileOutputStream fos = new FileOutputStream(this.langFolder + File.separator + "us-english.properties");
    			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    			fos.close();
    			HPS.PM.log(Level.INFO, "us-english.properties successfully downloaded");
    		}
    		defaultLang = loadedProperties.get(Language.ENGLISH);
    		defaultLang.load(new FileInputStream(languages.get(Language.ENGLISH)));
    		return true;
		} catch (Exception e) {
			HPS.PM.log(Level.SEVERE, "Download failed!!!");
			HPS.PM.log(Level.SEVERE, "Could not load default language. Plugin will not function.", "Disabling plugin...");
            HPS.PM.debug(e);
            HPS.localeState = false;
            Bukkit.getServer().getPluginManager().disablePlugin(HPS);
            return false;
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
    	if (defaultLang.getProperty(key) == null) {
    		HPS.PM.log(Level.SEVERE, "Translation error occurred. Please contact the developer with the following error:");
    		HPS.PM.log(Level.SEVERE, "Unable to find property: '" + key + "' for language type: '" + HPS.getConfig().getString("language") + "'");
    		return ChatColor.RED + "Translation error occurred. Please contact server administrator!";
    	}
        return activeLang.getProperty(key) == null ? String.format(defaultLang.getProperty(key), args) : String.format(activeLang.getProperty(key), args);
    }

    
    private void generateFile(File file, final String lang) {
    	InputStream stream = HarryPotterSpells.class.getClassLoader().getResourceAsStream(lang);
//    	HPS.PM.log(Level.INFO, "URL: " + HPS.class.getClassLoader().getResource(lang) == null ? "null" : HPS.class.getClassLoader().getResource(lang).toString());
//    	HPS.PM.log(Level.INFO, "URL: " + HPS.class.getClassLoader().getResource("/" + lang) == null ? "null" : HPS.class.getClassLoader().getResource("/" + lang).toString()); //null on windows
//    	HPS.PM.log(Level.INFO, "URL: " + HPS.class.getResource(lang) == null ? "null" : HPS.class.getResource(lang).toString()); //null on windows
//    	HPS.PM.log(Level.INFO, "URL: " + HPS.class.getResource("/" + lang) == null ? "null" : HPS.class.getResource("/" + lang).toString());
        if (stream == null) {
        	HPS.PM.log(Level.SEVERE, "Could not generate language file " + lang + " Ignore this message if you arent using this language");
        	return;
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
            HPS.PM.log(Level.INFO, "Language file " + lang + " has been sucessfully created.");
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
    	ENGLISH,
    	GERMAN,
    	SPANISH,
    	ITALIAN,
    	CHINESE,
		PORTUGUESE;
    	
    	/**
    	 * Gets the language from its configuration name
    	 * @param name The configuration language name
    	 * @return Enum of language. ENGLISH if no matches found
    	 */
    	public static Language getLanguage(String name) {
    		if (name.equalsIgnoreCase("nl-dutch")) {
    			return DUTCH;
    		} else if (name.equalsIgnoreCase("de-german")) {
    		    return GERMAN;
    		} else if (name.equalsIgnoreCase("es-spanish")) {
                return SPANISH;
            } else if (name.equalsIgnoreCase("it-italian")) {
            	return ITALIAN;
            } else if (name.equalsIgnoreCase("zh-chinese")) {
                return CHINESE;
            } else if (name.equalsIgnoreCase("br-portuguese")) {
				return PORTUGUESE;
			}
    		return ENGLISH;
    	}
    }
}
