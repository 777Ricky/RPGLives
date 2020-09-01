package net.minequests.gloriousmeme.rpglives;

import net.minequests.gloriousmeme.rpglives.utils.Utils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Created by Zack on 4/6/2017.
 */
public class RPGLivesAPI {

    /*
    Used for getting an online player's current lives
     */
    public int getLives(Player player) {
        if (!Utils.lives.containsKey(player.getUniqueId()))
            Utils.lives.put(player.getUniqueId(), Utils.getConfigLives(player));
        return Utils.lives.get(player.getUniqueId());
    }

    /*
    Used for getting an online player's current max number of lives
     */
    public int getMaxLives(Player player) {
        if (!Utils.maxlives.containsKey(player.getUniqueId()))
            Utils.maxlives.put(player.getUniqueId(), Utils.getConfigMaxLives(player));
        return Utils.maxlives.get(player.getUniqueId());
    }

    /*
    Used for getting an online player's current regen time
     */
    public int getRegenTime(Player player) {
        if (!Utils.regentime.containsKey(player.getUniqueId()))
            Utils.regentime.put(player.getUniqueId(), Utils.getConfigRegenTime(player));
        return Utils.regentime.get(player.getUniqueId());
    }

    /*
    Used for getting an offline player's current lives
     */
    public int getConfigLives(Player player) {
        return RPGLives.get().getLivesl().getInt(player.getUniqueId() + ".lives");
    }

    /*
    Used for getting an offline player's current max number of lives
     */
    public int getConfigMaxLives(Player player) {
        return RPGLives.get().getLivesl().getInt(player.getUniqueId() + ".maxlives");
    }

    /*
    Used for getting an offline player's current regen time
     */
    public int getConfigRegenTime(Player player) {
        return RPGLives.get().getLivesl().getInt(player.getUniqueId() + ".regentime");
    }

    /*
    Used for setting a player's lives (Can not be larger than their max number of lives)
     */
    public void setLives(Player player, int i) {
        try {
            player.playSound(player.getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 1, 1);
        } catch (Exception ignored) { }

        Utils.lives.put(player.getUniqueId(), i);
    }

    /*
    Used for setting a player's max lives
     */
    public void setMaxLives(Player player, int i) {
        try {
            player.playSound(player.getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 1, 1);
        } catch (Exception ignored) { }

        Utils.maxlives.put(player.getUniqueId(), i);
    }

    /*
    Used for setting a player's regen time
     */
    public void setRegenTime(Player player, int i) {
        if (Utils.regentime.containsKey(player.getUniqueId()))
            Utils.regentime.remove(player.getUniqueId());
        Utils.regentime.put(player.getUniqueId(), i);
        RPGLives.get().endTask(player);
        RPGLives.get().scheduleRepeatingTask(player, i);
    }
}
