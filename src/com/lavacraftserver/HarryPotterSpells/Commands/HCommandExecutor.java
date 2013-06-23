package com.lavacraftserver.HarryPotterSpells.Commands;

import org.bukkit.command.CommandExecutor;

import com.lavacraftserver.HarryPotterSpells.HPS;

/**
 * A custom {@link CommandExecutor} for use with commands in this plugin
 */
public abstract class HCommandExecutor implements CommandExecutor {
    public HPS HPS;

    public HCommandExecutor(HPS instance) {
        this.HPS = instance;
    }

}
