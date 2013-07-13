package com.hpspells.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * PM stands for PluginMessenger. <br>
 * This class manages logs and other ways of sending messages to players/console.
 */
public class PM {
	private Logger log;
	private HPS HPS;
	private String tag;
	private ChatColor info, warning;

	/**
	 * Constructs an instance of {@link PM}
	 * @param instance an instance of {@link HPS}
	 */
	public PM(HPS instance) {
	    this.HPS = instance;
	    this.log = HPS.getLogger();
	    this.tag = ChatColor.translateAlternateColorCodes('&', HPS.getConfig().getString("messaging.tag", "&f[&6HarryPotterSpells&f] "));
	    this.info = ChatColor.valueOf(HPS.getConfig().getString("messaging.info", "YELLOW"));
	    this.warning = ChatColor.valueOf(HPS.getConfig().getString("messaging.warning", "RED"));
	}

	/**
	 * Logs any amount of messages to the console
	 * @param level the level to log the message at
	 * @param message the message(s) to log
	 */
	public void log(Level level, String... message) {
		for(String str : message)
			log.log(level, str);
	}

	/**
	 * Sends a player any amount of messages with an informative aim
	 * @param player the player to send the message to
	 * @param message the message(s) to be sent
	 */
	public void tell(Player player, String... message) {
		for(String str : message)
			player.sendMessage(tag + info + str);
	}

	/**
	 * Sends a player any amount of messages with a warning aim
	 * @param player the player to send the message to
	 * @param message the message(s) to be sent
	 */
	public void warn(Player player, String... message) {
		for(String str : message)
			player.sendMessage(tag + warning + str);
	}

	/**
	 * Sends a player a new spell notification
	 * @param player the player to send the notification to
	 * @param spell the name of the spell the user has selected
	 */
	public void newSpell(Player player, String spell) {
		player.sendMessage(ChatColor.GOLD + HPS.Localisation.getTranslation("pmSpellSelected", ChatColor.AQUA + spell));
	}

	/**
	 * Notifies a player when a spell has been casting if enabled in the config
	 * @param player the player to notify
	 * @param spell the name of the spell they have cast
	 */
	public void notify(Player player, String spell) {
		if (HPS.getConfig().getBoolean("notify-on-spell-use", true))
			player.sendMessage(ChatColor.GOLD + HPS.Localisation.getTranslation("pmSpellCast", ChatColor.AQUA + spell));
	}

	/**
	 * Makes a player shout the name of a spell if enabled in the config
	 * @param player the player to shout
	 * @param spell the spell they have cast
	 */
	public void shout(Player player, String spell) {
		if (HPS.getConfig().getBoolean("shout-on-spell-use", false))
			player.chat(spell + "!");
	}

	/**
	 * Logs a debug message to the console if enabled in the config
	 * @param message the message(s) to log
	 */
	public void debug(String... message) {
		if(HPS.getConfig().getBoolean("debug-mode", false))
			for(String str : message)
				log.log(Level.INFO, "Debug: " + str);
	}

	/**
	 * Sends a informational message to the Console or a Player depending on the sender
	 * @param sender the sender
	 * @param message the message(s)
	 */
	public void dependantMessagingTell(CommandSender sender, String... message) {
		if(sender instanceof Player)
			tell((Player) sender, message);
		else if(sender instanceof ConsoleCommandSender)
			log(Level.INFO, message);
    }

	/**
	 * Sends a warning message to the Console or a Player depending on the sender
	 * @param sender the sender
	 * @param message the message(s)
	 */
	public void dependantMessagingWarn(CommandSender sender, String... message) {
		if(sender instanceof Player)
			warn((Player) sender, message);
		else if(sender instanceof ConsoleCommandSender)
			log(Level.WARNING, message);
	}

	/**
	 * Prints the stack trace of an error to the console if debug mode is enabled
	 * @param e the throwable
	 */
	public void debug(Throwable e) {
		if(HPS.getConfig().getBoolean("debug-mode", false))
			e.printStackTrace();
	}

}
