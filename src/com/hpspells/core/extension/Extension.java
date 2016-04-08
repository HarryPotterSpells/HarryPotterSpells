package com.hpspells.core.extension;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import com.hpspells.core.HPS;
import com.hpspells.core.api.APIHandler;
import com.hpspells.core.spell.SpellManager;
import com.hpspells.core.util.ReflectionsReplacement;

/**
 * Represents an Extension to HarryPotterSpells
 */
public class Extension implements Listener {
    private HPS HPS;
    private String name, description, authors, version;
    private Logger logger;
    private APIHandler API;
    private SpellManager spellManager;
    
    protected void load() {
        logger = new ExtensionLogger(this);
        logger.setParent(HPS.getLogger());
        API = APIHandler.getInstance();
        spellManager = API.getSpellManager();
        onEnable();
    }

    // Functions to be overridden by developers
    public void onEnable() {
    	logger.info("Has been enabled");
    }
    
    public void onDisable() {
    	logger.info("Has been disabled");
    }
    
    /**
     * Used to register listener class if the main class is not the listener.
     * 
     * @param listener Class to register
     */
    public void registerListener(Listener listener) {
    	HPS.getServer().getPluginManager().registerEvents(listener, HPS);
    }

    /**
     * Creates a new {@link Extension} instance.
     * 
     * @param instance an instance of {@link HPS}
     * @param extensionFile the jar file of the extension
     * @param extensionDescriptionFile the extension's description file
     * @return the newly created Extension
     */
    public static Extension create(HPS instance, File extensionFile, YamlConfiguration extensionDescriptionFile) throws Exception {
        ReflectionsReplacement.addFileToClasspath(instance.getHPSClassLoader(), extensionFile);

//        Class<? extends Extension> extensionClass;
//        try {
//        	extensionClass = jarClass.asSubclass(Extension.class);
//        } catch (ClassCastException ex) {
//            throw new ClassCastException("main class `" + extensionDescriptionFile.getString("main") + "' does not extend Extension");
//        }
//        
//        Extension extension = extensionClass.newInstance();
        
        Class<?> clazz = Class.forName(extensionDescriptionFile.getString("main"));
        if(!Extension.class.isAssignableFrom(clazz)) {
            throw new ClassNotFoundException("Could not find main class that extends Extension");
        }

        Extension extension = clazz.asSubclass(Extension.class).newInstance();
       // Extension extension = (Extension) clazz.getConstructor(com.hpspells.core.HPS.class).newInstance(instance);
//        Extension extension = (Extension) Class.forName(extensionDescriptionFile.getString("main")).newInstance();
        
        extension.HPS = instance;

        extension.name = extensionDescriptionFile.getString("name");
        extension.description = extensionDescriptionFile.getString("description");
        extension.authors = extensionDescriptionFile.getString("authors");
        extension.version = extensionDescriptionFile.getString("version");

        Validate.notNull(extension.name, "Extension name cannot be null");
        Validate.notNull(extension.version, "Extension name cannot be null");
        
        return extension;
    }

    /**
     * Gets the name of this {@link Extension}
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of this {@link Extension}
     * @return the description
     */
    public String getDescription() {
        return description == null ? "" : description;
    }

    /**
     * Gets the authors of this {@link Extension}
     * @return the authors
     */
    public String getAuthors() {
        return authors == null ? "" : authors;
    }

    /**
     * Gets the version string of this {@link Extension}
     * @return the version string
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the data folder for this {@link Extension}, creating it if it does not exist
     * @return the data folder
     */
    public File getDataFolder() {
        File file = new File(HPS.ExtensionManager.getExtensionFolder(), getName());

        if(!file.exists()) {
            file.mkdir();
        }

        return file;
    }

    /**
     * Gets the default {@link FileConfiguration} for this {@link Extension}, creating it if it does not exist
     * @return the config
     */
    public FileConfiguration getConfig() {
        File file = new File(getDataFolder(), "config.yml");

        if(!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("extCouldNotCreateConfig", getName()));
//            }
            saveResource(this, "config.yml", false); //Generates wrong one cause of wrong class loader
        }

        return YamlConfiguration.loadConfiguration(file);
    }
    
    public void saveResource(Extension extension, String resourcePath, boolean replace) {
    	if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(extension, resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + getName());
        }

        File outFile = new File(getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(getDataFolder(), resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                logger.log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
        }
	}

    public InputStream getResource(Extension extension, String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        try {
        	URL url = extension.getClass().getClassLoader().getResource(filename);
        	
            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }
    
    /**
     * Gets the {@link State} of this {@link Extension}
     * @return the state
     */
    public State getState() {
        return HPS.ExtensionManager.getState(this);
    }
    
    /**
     * Gets the {@link Logger} relevant to the class name.
     * 
     * @return logger for the extension
     */
    public Logger getLogger() {
		return logger;
	}
    
    /**
     * Gets the {@link APIHandler API} for access to API methods.
     * 
     * @return API instance
     */
    public APIHandler getAPI() {
        return API;
    }
    
    /**
     * Convenience method to get the SpellManager. 
     * 
     * @return the spell manager
     */
    public SpellManager getSpellManager() {
        return spellManager;
    }
    
    /**
     * An enum that represents the states an {@link Extension} can be in
     */
    public enum State {
        LOADED,
        ENABLED,
        DISABLED
    }


}
