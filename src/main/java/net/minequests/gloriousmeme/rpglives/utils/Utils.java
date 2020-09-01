package net.minequests.gloriousmeme.rpglives.utils;

import me.rayzr522.jsonmessage.JSONMessage;
import net.minequests.gloriousmeme.rpglives.RPGLives;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Utils {

    public static HashMap<UUID, Integer> lives = new HashMap<>();
    public static  HashMap<UUID, Integer> maxlives = new HashMap<>();
    public static HashMap<UUID, Integer> regentime = new HashMap<>();


    public static String replaceColors(String message) {
        String string = message.replaceAll("&", "§");
        return string.replaceAll("§§", "&");
    }

    public static boolean isNumber(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    public static int getLives(Player player) {
        if (!lives.containsKey(player.getUniqueId()))
            lives.put(player.getUniqueId(), getConfigLives(player));
        return lives.get(player.getUniqueId());
    }

    public static int getMaxLives(Player player) {
        if (!maxlives.containsKey(player.getUniqueId()))
            maxlives.put(player.getUniqueId(), getConfigMaxLives(player));
        return maxlives.get(player.getUniqueId());
    }

    public static int getRegenTime(Player player) {
        if (!regentime.containsKey(player.getUniqueId()))
            regentime.put(player.getUniqueId(), getConfigRegenTime(player));
        return regentime.get(player.getUniqueId());
    }

    public static int getConfigLives(Player player) {
        return RPGLives.get().getLivesl().getInt(player.getUniqueId() + ".lives");
    }

    public static int getConfigMaxLives(Player player) {
        return RPGLives.get().getLivesl().getInt(player.getUniqueId() + ".maxlives");
    }

    public static int getConfigRegenTime(Player player) {
        return RPGLives.get().getLivesl().getInt(player.getUniqueId() + ".regentime");
    }

    public static void setLives(Player player, int i) {
        try {
            player.playSound(player.getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 1, 1);
        } catch (Exception ignore) { }

        lives.put(player.getUniqueId(), i);
    }

    public static void setMaxLives(Player player, int i) {
        try {
            player.playSound(player.getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 1, 1);
        } catch (Exception ignore) { }

        maxlives.put(player.getUniqueId(), i);
    }

    public static void setRegenTime(Player player, int i) {
        regentime.put(player.getUniqueId(), i);
        RPGLives.get().endTask(player);
        RPGLives.get().scheduleRepeatingTask(player, i);
    }

    public static void sendPlayerActionbar(Player player, String message) {
        if (RPGLives.get().getConfig().getBoolean("TitleEnabled")) {
            JSONMessage.create(replaceColors(message))
                    .color(ChatColor.WHITE)
                    .actionbar(player);
        }
        player.sendMessage(replaceColors(message));
    }

    public static void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(replaceColors("&6----------------------------------------------------"));
        sender.sendMessage(replaceColors("&6RPGLives by GloriousMeme version: " + RPGLives.get().getDescription().getVersion()));
        sender.sendMessage(replaceColors("&6Usage: /lives <player>"));
        sender.sendMessage(replaceColors("&6Usage: /rpglives help"));
        sender.sendMessage(replaceColors("&6Usage: /rpglives reload"));
        sender.sendMessage(replaceColors("&6Usage: /rpglives shop"));
        sender.sendMessage(replaceColors("&6Usage: /rpglives giveitem <player> <amount>"));
        sender.sendMessage(replaceColors("&6Usage: /rpglives setlives <player> <amount>"));
        sender.sendMessage(replaceColors("&6Usage: /rpglives setmaxlives <player> <amount>"));
        sender.sendMessage(replaceColors("&6Usage: /rpglives setregentime <player> <time>"));
        sender.sendMessage(replaceColors("&6----------------------------------------------------"));
    }

    public static void clearArmor(Player player) {
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }
}