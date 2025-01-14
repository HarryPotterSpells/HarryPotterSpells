package com.hpspells.core.command;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.spell.Spell;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@CommandInfo(name = "spellswitch", description = "cmdSpsDescription", usage = "<command> <spell>", permissionDefault = "true", aliases="ss")
public class SpellSwitch extends HCommandExecutor {

    public SpellSwitch(HarryPotterSpells instance) {
        super(instance);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length != 1) {
                return false;
            } else {
                String spellName = args[0].replace('_', ' ');
                if (!HPS.SpellManager.isSpell(spellName)) {
                    HPS.PM.dependantMessagingWarn(player, HPS.Localisation.getTranslation("genSpellNotRecognized"));
                    return false;
                } else {
                    Spell spell = HPS.SpellManager.getSpell(spellName);
                    if (!spell.playerKnows(player)) {
                        HPS.PM.warn(player, HPS.Localisation.getTranslation("cmdSpsPlayerDoesntKnow"));
                        return true;
                    } else {
                        HPS.SpellManager.setCurrentSpell(player, spell);
                        // Captializes the spell name before sending it to the player
                        spellName = spellName.substring(0, 1).toUpperCase() + spellName.substring(1);
                        HPS.PM.newSpell(player, spellName);
                        if (HPS.getConfig().getBoolean("wand.lore.show-current-spell")) {
                       	 //TODO: Somehow get the Wand.Lore and update it cleanly instead of this
                           ItemStack wand = HPS.WandManager.getWandFromInventory(player.getInventory());
                           ItemMeta meta = wand.getItemMeta();
                           List<String> lore = meta.getLore();
                           for (String string : lore) {
                           	if (ChatColor.stripColor(string).contains("Current Spell: ")) {
                           		int index = lore.indexOf(string);
                           		for (String line : HPS.getConfig().getStringList("wand.lore.format")) {
                           			if (ChatColor.stripColor(line).contains("%spell")) {
                           				line = ChatColor.translateAlternateColorCodes('&', line);
                           				line = line.replace("%spell", spell == null ? "None" : spell.getName());
                           				lore.set(index, line);
                           				break;
                           			}
                           		}
                           		break;
                           	}
                           }
                           meta.setLore(lore);
                           wand.setItemMeta(meta);
                       }
                        return true;
                    }
                }
            }
        } else
            HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdPlayerOnly"));
        return true;
    }

}
