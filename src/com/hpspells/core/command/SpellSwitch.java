package com.hpspells.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.Spell;

@CommandInfo (name="spellswitch", description="cmdSpsDescription", usage="<command> <spell>", permissionDefault="true")
public class SpellSwitch extends HCommandExecutor{

	public SpellSwitch(HPS instance) {
		super(instance);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player){
			Player player = (Player) sender;
			if (args.length != 1){
				return false;
			} else {
				String spellName = args[0].replace('_', ' ');
				if (!HPS.SpellManager.isSpell(spellName)) {
					HPS.PM.dependantMessagingWarn(player, HPS.Localisation.getTranslation("genSpellNotRecognized"));
				} else {
					Spell spell = HPS.SpellManager.getSpell(spellName);
					if (!spell.playerKnows(player)){
						HPS.PM.warn(player, HPS.Localisation.getTranslation("cmdSpsPlayerDoesntKnow"));
						return true;
					} else {
						HPS.SpellManager.setCurrentSpell(player, spell);
						return true;
					}
				}
			}
		} else 
			HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdPlayerOnly"));	
		return true;
	}

}
