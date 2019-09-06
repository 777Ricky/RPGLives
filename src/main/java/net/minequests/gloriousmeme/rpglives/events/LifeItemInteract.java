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
                int i = Utils.getLives(player);
                i++;
                hand.setAmount(amount - 1);
                if (RPGLives.get().getVersion().contains("1_8")) {
                    player.getInventory().setItemInHand(hand);
                }
                else if (RPGLives.get().getVersion().contains("1_9") || RPGLives.get().getVersion().contains("1_10") || RPGLives.get().getVersion().contains("1_11")
                        || RPGLives.get().getVersion().contains("1_12") || RPGLives.get().getVersion().contains("1_13")) {
                    player.getInventory().setItemInMainHand(hand);
                }
                Utils.setLives(player, i);
                player.sendMessage(Utils.replaceColors(RPGLives.get().getConfig().getString("GainLifeMessage").replaceAll("<lives>", String.valueOf(Utils.getLives(player)))).replaceAll("<maxlives>", String.valueOf(Utils.getMaxLives(player))));
            } else {
                int i = Utils.getLives(player);
                i++;
                if (RPGLives.get().getVersion().contains("1_8")) {
                    player.getInventory().setItemInHand(new ItemStack(Material.AIR));
                }
                else if (RPGLives.get().getVersion().contains("1_9") || RPGLives.get().getVersion().contains("1_10") || RPGLives.get().getVersion().contains("1_11")
                        || RPGLives.get().getVersion().contains("1_12") || RPGLives.get().getVersion().contains("1_13")) {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }
                Utils.setLives(player, i);
                player.sendMessage(Utils.replaceColors(RPGLives.get().getConfig().getString("GainLifeMessage").replaceAll("<lives>", String.valueOf(Utils.getLives(player)))).replaceAll("<maxlives>", String.valueOf(Utils.getMaxLives(player))));
            }
        }
    }
}