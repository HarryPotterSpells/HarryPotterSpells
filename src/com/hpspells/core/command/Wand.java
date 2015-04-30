package com.hpspells.core.command;

import com.hpspells.core.HPS;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "wand", description = "cmdWanDescription")
public class Wand extends HCommandExecutor {

    public Wand(HPS instance) {
        super(instance);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.getInventory().setItem(player.getInventory().firstEmpty(), HPS.Wand.getWand(player));
            HPS.PM.tell(player, HPS.Localisation.getTranslation("cmdWanGiven"));
            if (HPS.getConfig().getBoolean("wand.explosion-effect")) {
                player.getWorld().createExplosion(player.getLocation(), 0, false);
            }
        } else
            HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdPlayerOnly"));

        return true;
    }

}
