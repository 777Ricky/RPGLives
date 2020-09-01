package net.minequests.gloriousmeme.rpglives.listeners;

import net.minequests.gloriousmeme.rpglives.RPGLives;
import net.minequests.gloriousmeme.rpglives.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (RPGLives.get().getConfig().getBoolean("UseLifePermission") && !player.hasPermission(RPGLives.get().getConfig().getString("LifePermission"))) {
            Utils.lives.put(player.getUniqueId(), 0);
            Utils.maxlives.put(player.getUniqueId(), 0);
            Utils.regentime.put(player.getUniqueId(), Utils.getConfigRegenTime(player));
            return;
        }
        if (Utils.lives.containsKey(player.getUniqueId()) || Utils.maxlives.containsKey(player.getUniqueId()) ||
                Utils.regentime.containsKey(player.getUniqueId())) {
            RPGLives.get().scheduleRepeatingTask(player, Utils.getRegenTime(player));
            return;
        }
        if (!RPGLives.get().getLivesl().contains(player.getUniqueId().toString())) {
            Utils.lives.put(player.getUniqueId(), RPGLives.get().getConfig().getInt("AmountOfLives"));
            Utils.maxlives.put(player.getUniqueId(), RPGLives.get().getConfig().getInt("AmountOfLives"));
            Utils.regentime.put(player.getUniqueId(), RPGLives.get().getConfig().getInt("RegenTime"));
            RPGLives.get().scheduleRepeatingTask(player, RPGLives.get().getConfig().getInt("RegenTime"));
        }
        else {
            Utils.lives.put(player.getUniqueId(), Utils.getConfigLives(player));
            Utils.maxlives.put(player.getUniqueId(), Utils.getConfigMaxLives(player));
            Utils.regentime.put(player.getUniqueId(), Utils.getConfigRegenTime(player));
            RPGLives.get().scheduleRepeatingTask(player, Utils.getConfigRegenTime(player));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        RPGLives.get().endTask(player);
    }
}