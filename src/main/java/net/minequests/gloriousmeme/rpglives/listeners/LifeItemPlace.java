package net.minequests.gloriousmeme.rpglives.listeners;

import net.minequests.gloriousmeme.rpglives.RPGLives;
import net.minequests.gloriousmeme.rpglives.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class LifeItemPlace implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        if (!event.getItemInHand().hasItemMeta())
            return;
        if (event.getItemInHand().getItemMeta().getDisplayName() == null)
            return;
        if (event.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.replaceColors(RPGLives.get().getConfig().getString("LifeItemName"))))
            event.setCancelled(true);
    }
}