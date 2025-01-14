package com.hpspells.core.command;

import com.hpspells.core.HarryPotterSpells;
import org.bukkit.command.CommandExecutor;

/**
 * A custom {@link CommandExecutor} for use with commands in this plugin
 */
public abstract class HCommandExecutor implements CommandExecutor {
    public HarryPotterSpells HPS;

    public HCommandExecutor(HarryPotterSpells instance) {
        this.HPS = instance;
    }

}
