package com.hpspells.core.command;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.hpspells.core.HarryPotterSpells;

@CommandInfo(name = "wand", description = "cmdWanDescription", usage = "<command> [player|me] [material]")
public class Wand extends HCommandExecutor {

    Permission perm = new Permission("harrypotterspells.wand.others", PermissionDefault.OP);
    
    public Wand(HarryPotterSpells instance) {
        super(instance);
        Bukkit.getServer().getPluginManager().addPermission(perm);
    }

    @SuppressWarnings("deprecation")
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                giftWand(player);
            } else {
                HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdPlayerOnly"));
            }
        } else if (args.length >= 1 && args.length <= 2) {
            if (args[0].equalsIgnoreCase("me") || args[0].equalsIgnoreCase(sender.getName())) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (args.length == 2) {
                        Material material = Material.matchMaterial(args[1]);
                        if (HPS.WandManager.getWandTypes().contains(material)) {
                            giftWand(player, material);
                        } else {
                            String materialName = material == null ? "NULL" : material.toString();
                            HPS.PM.dependantMessagingWarn(sender, HPS.Localisation.getTranslation("errWandCreationInvalidType", 
                                    ChatColor.YELLOW + materialName + HPS.PM.getWarningColor()));
                            HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errWandCreationInvalidType", material));
                        }
                    } else {
                        giftWand(player);
                    }
                } else {
                    HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdPlayerOnly"));
                }
            } else if (sender.hasPermission("harrypotterspells.wand.others")) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    if (args.length == 2) {
                        Material material = Material.matchMaterial(args[1]);
                        if (HPS.WandManager.getWandTypes().contains(material)) {
                            giftWand(target, material);
                        } else {
                            String materialName = material == null ? "NULL" : material.toString();
                            HPS.PM.dependantMessagingWarn(sender, HPS.Localisation.getTranslation("errWandCreationInvalidType", 
                                    ChatColor.YELLOW + materialName + HPS.PM.getWarningColor()));
                            HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errWandCreationInvalidType", material));
                        }
                    } else {
                        giftWand(target);
                    }
                    HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdWanGifted", target.getName()));
                } else if (Bukkit.getOfflinePlayer(args[0]) != null) {
                    HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdWanOffline", ChatColor.GREEN + args[0]));
                } else {
                    HPS.PM.dependantMessagingWarn(sender, "This player does not exist.");
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }
    
    /**
     * Gives the player a wand with a specified material or the default one.
     * 
     * @param player The player to give a wand to
     * @param material The material to use if required
     */
    public void giftWand(Player player, Material material) {
        player.getInventory().setItem(player.getInventory().firstEmpty(), HPS.WandManager.getWand(player, material));
        HPS.PM.tell(player, HPS.Localisation.getTranslation("cmdWanGiven"));
        if (HPS.getConfig().getBoolean("wand.explosion-effect")) {
            player.getWorld().createExplosion(player.getLocation(), 0, false);
        }
    }
    
    public void giftWand(Player player) {
        giftWand(player, null);
    }

}
