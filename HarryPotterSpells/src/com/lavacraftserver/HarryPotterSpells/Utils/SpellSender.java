package com.lavacraftserver.HarryPotterSpells.Utils;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lavacraftserver.HarryPotterSpells.Spells.Confringo;
import com.lavacraftserver.HarryPotterSpells.Spells.Confundo;
//import com.lavacraftserver.HarryPotterSpells.Spells.Nox;
import com.lavacraftserver.HarryPotterSpells.Spells.Aguamenti;
import com.lavacraftserver.HarryPotterSpells.Spells.AlarteAscendare;
import com.lavacraftserver.HarryPotterSpells.Spells.Evanesco;
import com.lavacraftserver.HarryPotterSpells.Spells.Reducto;
import com.lavacraftserver.HarryPotterSpells.Spells.Sonorus;
import com.lavacraftserver.HarryPotterSpells.Spells.Spongify;
import com.lavacraftserver.HarryPotterSpells.Spells.TimeSpell;
import com.lavacraftserver.HarryPotterSpells.Spells.TreeSpell;
import com.lavacraftserver.HarryPotterSpells.Spells.WingardiumLeviosa;

public class SpellSender {
	
	public static void go(String spell, Player p, PlayerInteractEvent e) {
		if(spell.equalsIgnoreCase("confringo")) {Confringo.cast(p, e);}
		if(spell.equalsIgnoreCase("time")) {TimeSpell.cast(p, e);}
		if(spell.equalsIgnoreCase("tree")) {TreeSpell.cast(p);}
		if(spell.equalsIgnoreCase("confundo")) {Confundo.cast(p);}
		if(spell.equalsIgnoreCase("wingardiumleviosa")) {WingardiumLeviosa.cast(p);}
//		if(spell.equalsIgnoreCase("nox")) {Nox.cast(p);}
		if(spell.equalsIgnoreCase("evanesco")) {Evanesco.cast(p);}
		if(spell.equalsIgnoreCase("reducto")) {Reducto.cast(p);}
		if(spell.equalsIgnoreCase("sonorus")) {Sonorus.cast(p);}
		if(spell.equalsIgnoreCase("spongify")) {Spongify.cast(p);}
		if(spell.equalsIgnoreCase("aguamenti")) {Aguamenti.cast(p);}
		if(spell.equalsIgnoreCase("alarteascendare")) {AlarteAscendare.cast(p);}
	}
}
