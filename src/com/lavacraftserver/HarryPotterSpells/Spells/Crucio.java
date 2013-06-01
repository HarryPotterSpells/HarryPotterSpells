package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell(
		name="Crucio",
		description="Torture your victim.",
		range=50,
		goThroughWalls=false
)

public class Crucio extends Spell implements Listener {

	private HashMap<String, Integer> crucioTimer = new HashMap<String, Integer>();
	
	int tid = Bukkit.getScheduler().scheduleSyncRepeatingTask(HPS.Plugin, new Runnable() {
		@Override
		public void run() {
			for(String s : crucioTimer.keySet()){
				if(crucioTimer.containsKey(s)){
					while(crucioTimer.get(s)>=0){
						int r = crucioTimer.get(s);
						crucioTimer.put(s, r--);
					}
					crucioTimer.remove(s);
					denyFlight(s);
				}
			}
		}
	}, 0, 20);
	
	public boolean cast(Player p){
		if (Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof Player) {
			Player entity = (Player)Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			crucioTimer.put(entity.getDisplayName(), 10);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.HARM, crucioTimer.get(entity.getDisplayName())*20, 0));
			entity.setFlying(true);
			entity.teleport(new Location(entity.getWorld(), entity.getLocation().getX(), entity.getLocation().getBlockY()+2, entity.getLocation().getZ()));
			return true;
		} else {
			HPS.PM.warn(p, "This can only be used on a player.");
			return false;
		}
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		if(crucioTimer.containsKey(e.getPlayer().getDisplayName())){
			e.setTo(e.getFrom());
		}
	}
	
	public void denyFlight(String s) {
		Bukkit.getServer().getPlayer(s).setFlying(false);
	}
	
}