package net.minequests.gloriousmeme.rpglives;

import com.cryptomorin.xseries.XSound;
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
        return Utils.lives.computeIfAbsent(player.getUniqueId(), id -> Utils.getConfigLives(player));
    }

    /*
    Used for getting an online player's current max number of lives
     */
    public int getMaxLives(Player player) {
        return Utils.maxlives.computeIfAbsent(player.getUniqueId(), id -> Utils.getConfigMaxLives(player));
    }

    /*
    Used for getting an online player's current regen time
     */
    public int getRegenTime(Player player) {
        return Utils.regentime.computeIfAbsent(player.getUniqueId(), id -> Utils.getConfigRegenTime(player));
    }

    /*
    Used for getting an offline player's current lives
     */
    public int getConfigLives(Player player) {
        return RPGLives.get().getLivesConfig().getInt(player.getUniqueId() + ".lives");
    }

    /*
    Used for getting an offline player's current max number of lives
     */
    public int getConfigMaxLives(Player player) {
        return RPGLives.get().getLivesConfig().getInt(player.getUniqueId() + ".maxlives");
    }

    /*
    Used for getting an offline player's current regen time
     */
    public int getConfigRegenTime(Player player) {
        return RPGLives.get().getLivesConfig().getInt(player.getUniqueId() + ".regentime");
    }

    /*
    Used for setting a player's lives (Can not be larger than their max number of lives)
     */
    public void setLives(Player player, int i) {
        XSound.ENTITY_PLAYER_LEVELUP.play(player);

        Utils.lives.put(player.getUniqueId(), i);
    }

    /*
    Used for setting a player's max lives
     */
    public void setMaxLives(Player player, int i) {
        XSound.ENTITY_PLAYER_LEVELUP.play(player);

        Utils.maxlives.put(player.getUniqueId(), i);
    }

    /*
    Used for setting a player's regen time
     */
    public void setRegenTime(Player player, int i) {
        Utils.regentime.put(player.getUniqueId(), i);
        RPGLives.get().endTask(player);
        RPGLives.get().scheduleRepeatingTask(player, i);
    }
}
