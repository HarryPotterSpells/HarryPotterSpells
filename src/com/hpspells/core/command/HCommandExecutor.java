package com.hpspells.core.command;

import com.hpspells.core.HPS;
import org.bukkit.command.CommandExecutor;

/**
 * A custom {@link CommandExecutor} for use with commands in this plugin
 */
public abstract class HCommandExecutor implements CommandExecutor {
    public HPS HPS;

    public HCommandExecutor(HPS instance) {
        this.HPS = instance;
    }

}
