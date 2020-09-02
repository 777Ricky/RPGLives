package net.minequests.gloriousmeme.rpglives.listeners;

import net.minequests.gloriousmeme.rpglives.RPGLives;
import net.minequests.gloriousmeme.rpglives.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PlayerDeath implements Listener {
    private Map<Player, ItemStack[]> invsave = new HashMap<>();
    private Map<Player, ItemStack[]> armorsave = new HashMap<>();

    private Location location;

    private void checkInvsave(Player player) {
        if (invsave.containsKey(player) && armorsave.containsKey(player)) {
            player.getInventory().setContents(invsave.get(player));
            player.getInventory().setArmorContents(armorsave.get(player));
            invsave.remove(player);
            armorsave.remove(player);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("rpglives.respawn") && location != null) {
            player.teleport(location);
        }

        checkInvsave(player);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        checkInvsave(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        checkInvsave(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (RPGLives.get().getConfig().getBoolean("DeleteItems") && Utils.getLives(player) <= 0) {
            player.getInventory().clear();
            Utils.clearArmor(player);
            event.getDrops().clear();
            return;
        }

        if (Utils.getLives(player) <= 0) {
            return;
        }

        if (!RPGLives.get().getConfig().getStringList("Worlds").contains(player.getWorld().getName()) && RPGLives.get().getConfig().getBoolean("UsePerWorld")) {
            return;
        }

        if (Utils.getLives(player) == 1 && RPGLives.get().getConfig().getBoolean("ExecuteCommandsOnFinalLife")) {
            for (String s : RPGLives.get().getConfig().getStringList("Commands")) {
                String commands = s.replace("%player%", player.getName());
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), commands);
            }
        }

        if (RPGLives.get().getConfig().getStringList("UnlimitedLivesWorlds").contains(player.getWorld().getName())) {
            invsave.put(player, player.getInventory().getContents());
            armorsave.put(player, player.getInventory().getArmorContents());
            player.getInventory().clear();
            Utils.clearArmor(player);
            event.getDrops().clear();
            return;
        }

        if (player.hasPermission("rpglives.respawn") && Utils.getLives(player) <= 0)
            location = player.getLocation();

        if (player.getKiller() != null) {
            if (!RPGLives.get().getConfig().getStringList("SaveItemsOnPvPDeathWorlds").contains(player.getWorld().getName())) {
                if (RPGLives.get().getConfig().getInt("PvPDeathAmount") == 0)
                    return;
                int x = Utils.getLives(player) - RPGLives.get().getConfig().getInt("PvPDeathAmount");
                Utils.setLives(player, x);
                Utils.sendPlayerActionbar(
                        player,
                        RPGLives.get().getConfig().getString("LostLifeMessage")
                                .replaceAll("%lives%", String.valueOf(Utils.getLives(player)))
                                .replaceAll("%maxlives%", String.valueOf(Utils.getMaxLives(player)))
                );
            } else {
                int x = Utils.getLives(player) - RPGLives.get().getConfig().getInt("PvPDeathAmount");
                Utils.setLives(player, x);
                invsave.put(player, player.getInventory().getContents());
                armorsave.put(player, player.getInventory().getArmorContents());
                player.getInventory().clear();
                Utils.clearArmor(player);
                event.getDrops().clear();
                Utils.sendPlayerActionbar(
                        player,
                        RPGLives.get().getConfig().getString("LostLifeMessage")
                                .replaceAll("%lives%", String.valueOf(Utils.getLives(player)))
                                .replaceAll("%maxlives%", String.valueOf(Utils.getMaxLives(player)))
                );
            }
        } else {
            int i = Utils.getLives(player) - RPGLives.get().getConfig().getInt("NormalDeathAmount");
            Utils.setLives(player, i);
            invsave.put(player, player.getInventory().getContents());
            armorsave.put(player, player.getInventory().getArmorContents());
            player.getInventory().clear();
            Utils.clearArmor(player);
            event.getDrops().clear();
            Utils.sendPlayerActionbar(
                    player,
                    RPGLives.get().getConfig().getString("LostLifeMessage")
                            .replaceAll("%lives%", String.valueOf(Utils.getLives(player)))
                            .replaceAll("%maxlives%", String.valueOf(Utils.getMaxLives(player)))
            );
        }
    }
}