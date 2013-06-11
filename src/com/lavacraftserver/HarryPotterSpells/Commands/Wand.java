package com.lavacraftserver.HarryPotterSpells.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;

@HCommand(name="wand", description="cmdWanDescription")
public class Wand extends HCommandExecutor {

    public Wand(HPS plugin) {
        super(plugin);
    }
    
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			player.getInventory().setItem(player.getInventory().firstEmpty(), HPS.Wand.getWand());
			HPS.PM.tell(player, HPS.Localisation.getTranslation("cmdWanGiven"));
			if(HPS.getConfig().getBoolean("wand-give.explosion-effect")) {
				player.getWorld().createExplosion(player.getLocation(), 0, false);
			}
		} else
			HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdPlayerOnly"));
		
		return true;
	}

}
