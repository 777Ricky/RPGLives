package net.minequests.gloriousmeme.rpglives.events;

import net.minequests.gloriousmeme.rpglives.RPGLives;
import net.minequests.gloriousmeme.rpglives.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 * Created by GloriousMeme on 11/16/2016.
 */
public class GUIClick implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        int clickedSlot = event.getSlot();

        if (event.getCurrentItem() == null)
            return;

        if (inventory.getName().equalsIgnoreCase(Utils.replaceColors(RPGLives.get().getConfig().getString("ShopName")))) {
            if (clickedSlot == 11) {
                if (Utils.getLives(player) >= Utils.getMaxLives(player)) {
                    player.sendMessage(Utils.replaceColors("&4You already have your maximum amount of lives."));
                    event.setCancelled(true);
                    return;
                }
                if (RPGLives.get().getEconomy().getBalance(player) >= RPGLives.get().getGuiUtils().getLifePrice()) {
                    Utils.setLives(player, Utils.getLives(player) + 1);
                    RPGLives.get().getEconomy().withdrawPlayer(player, RPGLives.get().getGuiUtils().getLifePrice());
                    player.sendMessage(Utils.replaceColors(RPGLives.get().getConfig().getString("PurchaseLifeMessage")
                            .replace("%lives%", String.valueOf(Utils.getLives(player)))).replace("%maxlives%", String.valueOf(Utils.getMaxLives(player))));
                    event.setCancelled(true);
                } else
                    player.sendMessage(Utils.replaceColors(RPGLives.get().getConfig().getString("NotEnoughMoneyMessage")));
            }
            if (clickedSlot == 15) {
                player.closeInventory();
                player.sendMessage(Utils.replaceColors(RPGLives.get().getConfig().getString("CloseShopMessage")));
                event.setCancelled(true);
            }
            event.setCancelled(true);
        }
    }
}
