package com.lavacraftserver.HarryPotterSpells.Commands;

import org.bukkit.command.CommandExecutor;

import com.lavacraftserver.HarryPotterSpells.HPS;

/**
 * A custom {@link CommandExecutor} for use with commands in this plugin
 */
public abstract class HCommandExecutor implements CommandExecutor {
    HPS HPS;

    /**
     * Constructs a {@link HCommandExecutor}
     * @param plugin an instance of {@link HPS}
     */
    public HCommandExecutor(HPS plugin) {
        HPS = plugin;
    }

}
