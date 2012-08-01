package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WingardiumLeviosa {
	
	public static void cast(Player p) {
		if(p.isFlying()) {
			p.setFlying(false);
		} else {
			Location l = p.getLocation();
			p.teleport(new Location(p.getWorld(), l.getX(), l.getY() + 1, l.getZ()));
			p.setFlying(true);
			myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(myPlugin, new Runnable() {

				   public void run() {
				       getServer().broadcastMessage("This message is broadcast by the main thread");
				   }
				}, 300L);
		}
	}

}
