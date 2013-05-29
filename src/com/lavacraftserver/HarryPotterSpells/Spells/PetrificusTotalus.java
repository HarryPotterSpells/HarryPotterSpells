package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Petrificus Totalus",
		description="Stuns the target player",
		range=50,
		goThroughWalls=false
)
public class PetrificusTotalus extends Spell implements Listener {
	public static List<String> players = new ArrayList<>();

	@Override
	public void cast(Player p) {
		if (Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof Player) {
			PetrificusTotalus.players.add(((Player) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls())).getName());
		} else {
			HPS.PM.warn(p, "This may only be used on a player or a mob.");
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (PetrificusTotalus.players.contains(e.getPlayer().getName()))
			e.setTo(e.getFrom());
	}

}
