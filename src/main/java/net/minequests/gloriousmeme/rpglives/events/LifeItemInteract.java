package net.minequests.gloriousmeme.rpglives.events;

import net.minequests.gloriousmeme.rpglives.RPGLives;
import net.minequests.gloriousmeme.rpglives.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class LifeItemInteract implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (event.getItem() == null)
                return;
            if (!event.getItem().hasItemMeta())
                return;
            if (event.getItem().getItemMeta().getDisplayName() == null)
                return;
            if (!event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.replaceColors(RPGLives.get().getConfig().getString("LifeItemName"))))
                return;
            if (Utils.getLives(player) >= Utils.getMaxLives(player))
                return;
            ItemStack hand = event.getItem();
            int amount = hand.getAmount();

            if (amount > 1) {
                hand.setAmount(amount - 1);
                player.getInventory().setItemInHand(hand);
            } else {
                player.getInventory().setItemInHand(new ItemStack(Material.AIR));
            }

            int i = Utils.getLives(player);
            i++;
            Utils.setLives(player, i);
            player.sendMessage(Utils.replaceColors(RPGLives.get().getConfig().getString("GainLifeMessage").replaceAll("<lives>", String.valueOf(Utils.getLives(player)))).replaceAll("<maxlives>", String.valueOf(Utils.getMaxLives(player))));

        }
    }
}