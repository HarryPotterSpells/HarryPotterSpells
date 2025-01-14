package com.hpspells.core.spell;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.api.SpellBookRecipe;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.spell.interfaces.Craftable;

@SpellInfo(
        name = "Time",
        description = "descTime",
        range = 0,
        goThroughWalls = false,
        cooldown = 600
)
public class TimeSpell extends Spell implements Craftable {
    private SpellBookRecipe recipe = null;

    public TimeSpell(HarryPotterSpells instance) {
        super(instance);
    }

    //Dawn == 0L
    //Morning == 2500L
    //Noon == 6000L
    //Evening == 11000L
    //Night == 15000L
    public boolean cast(Player p) {
        World w = p.getWorld();
        Material m = p.getTargetBlock((Set<Material>) null, 50).getType();
        if (m == Material.GLOWSTONE)
            w.setTime(0L);
        else if (m == Material.OBSIDIAN)
            w.setTime(15000L);
        else {
            long time = w.getTime();
            if (time < 12000)
                time = time + 12000L;
            else
                time = time - 12000L;
            w.setTime(time);
        }

        if ((Boolean) getConfig("lightning", true))
            awesomeLightning(p.getLocation(), w);
        return true;
    }

    public void awesomeLightning(Location l, World w) {
        double x = l.getX(), y = l.getY(), z = l.getZ();
        //X Y Z
        w.strikeLightningEffect(new Location(w, x, y, z - 2));
        w.strikeLightningEffect(new Location(w, x, y, z + 2));
        w.strikeLightningEffect(new Location(w, x - 2, y, z));
        w.strikeLightningEffect(new Location(w, x + 2, y, z));
    }

    @Override
    public SpellBookRecipe getCraftingRecipe() {
        if (recipe == null) {
            recipe = new SpellBookRecipe(HPS, this);
            recipe.shape("dGd", "dCd", "DBD");
            recipe.setIngredient('d', Material.DIAMOND);
            recipe.setIngredient('G', Material.GHAST_TEAR);
            recipe.setIngredient('C', Material.CLOCK);
            recipe.setIngredient('D', Material.DIAMOND_BLOCK);
            recipe.setIngredient('B', Material.BOOK);
        }
        return recipe;
    }

}
