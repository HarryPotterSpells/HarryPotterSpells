package com.hpspells.core.spell;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;

@SpellInfo(
        name = "Silencio",
        description = "descSilencio",
        range = 50,
        goThroughWalls = false,
        cooldown = 90
)

public class Silencio extends Spell implements Listener {

    public Silencio(HPS instance) {
        super(instance);
    }

    public ArrayList<String> muted = new ArrayList<String>();

    public boolean cast(final Player p) {
        HPS.SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitBlock(Block block) {
                HPS.PM.warn(p, HPS.Localisation.getTranslation("spellLivingEntityOnly"));
            }

            @Override
            public void hitEntity(LivingEntity entity) {
                if (entity instanceof Player) {
                    Player tarPlayer = (Player) entity;
                    final String tarPlayerName = tarPlayer.getName();
                    muted.add(tarPlayerName);
                    HPS.PM.tell(tarPlayer, p.getName() + " Casted Silencio on you, You are now muted");
                    HPS.PM.tell(p, tarPlayerName + " has been Muted!");
                    Bukkit.getScheduler().scheduleSyncDelayedTask(HPS, new Runnable() {
                        public void run() {
                            muted.remove(tarPlayerName);
                        }
                    }, getTime("duration", 30));
                }
            }

        }, 1.0, Effect.SMOKE);
        return true;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (muted.contains(player.getName())) {
            event.setCancelled(true);
            HPS.PM.tell(player, "You are still muted");
        }
    }
}
