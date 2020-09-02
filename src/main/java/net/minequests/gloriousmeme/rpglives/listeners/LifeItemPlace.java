package net.minequests.gloriousmeme.rpglives.listeners;

import net.minequests.gloriousmeme.rpglives.RPGLives;
import net.minequests.gloriousmeme.rpglives.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class LifeItemPlace implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();

        if (!item.hasItemMeta())
            return;
        if (item.getItemMeta().getDisplayName() == null)
            return;
        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.replaceColors(RPGLives.get().getConfig().getString("LifeItemName"))))
            event.setCancelled(true);
    }
}