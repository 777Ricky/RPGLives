package net.minequests.gloriousmeme.rpglives;

import net.milkbowl.vault.economy.Economy;
import net.minequests.gloriousmeme.rpglives.command.AbstractCommand;
import net.minequests.gloriousmeme.rpglives.command.TabCompletionHandler;
import net.minequests.gloriousmeme.rpglives.listeners.*;
import net.minequests.gloriousmeme.rpglives.utils.GUIUtils;
import net.minequests.gloriousmeme.rpglives.utils.PlaceHolderAPIHook;
import net.minequests.gloriousmeme.rpglives.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RPGLives extends JavaPlugin {

    /*
    TODO: implement permission so that the player revives where he died instead of normal spawn point (in progress)
    TODO: support heroes death (if it isn't already)
    TODO: implement a permission system to determine player's max lives
    TODO: Add ability to give someone else one of your lives
    TODO: Add ability to select specific slots you lose on pve / pvp death (Maybe)
     */

    private static RPGLives instance;
    private static RPGLivesAPI rpgLivesAPI;

    private static Economy economy = null;

    private File livesFile;
    private FileConfiguration livesConfig;

    private GUIUtils guiUtils;

    private Map<UUID, Integer> taskID = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        rpgLivesAPI = new RPGLivesAPI();
        guiUtils = new GUIUtils(this);

        if (setupEconomy()) {
            getLogger().info("Successfully hooked into vault.");
        } else {
            getLogger().info("Couldn't find vault continuing without it.");
        }

        saveDefaultConfig();
        registerEvents();
        AbstractCommand.registerCommands(this);
        getCommand("rpglives").setTabCompleter(new TabCompletionHandler());
        createFiles();


        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceHolderAPIHook(this).hook();
            getLogger().info("Hooked into PlaceholderAPI.");
        } else
            getLogger().info("Could not find PlaceholderAPI continuing without it.");
    }

    @Override
    public void onDisable() {
        saveHashmapData();

        instance = null;
        rpgLivesAPI = null;
        guiUtils = null;
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getServer().getPluginManager();

        pm.registerEvents(new PlayerJoin(), this);
        pm.registerEvents(new PlayerDeath(), this);
        pm.registerEvents(new LifeItemInteract(), this);
        pm.registerEvents(new LifeItemPlace(), this);
        pm.registerEvents(new GUIClick(), this);
    }

    public static RPGLives get() {
        return instance;
    }

    public static RPGLivesAPI getAPI() {
        return rpgLivesAPI;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    public Economy getEconomy() {
        return economy;
    }

    public FileConfiguration getLivesConfig() {
        return livesConfig;
    }

    private void createFiles() {
        livesFile = new File(getDataFolder(), "lives.yml");

        if (!livesFile.exists()) {
            livesFile.getParentFile().mkdirs();
            saveResource("lives.yml", false);
        }
        livesConfig = new YamlConfiguration();
        try {
            livesConfig.load(livesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void saveHashmapData() {
        for (Map.Entry<UUID, Integer> entry : Utils.lives.entrySet()) {
            livesConfig.set(entry.getKey() + ".lives", entry.getValue());
        }
        for (Map.Entry<UUID, Integer> entry : Utils.maxlives.entrySet()) {
            livesConfig.set(entry.getKey() + ".maxlives", entry.getValue());
        }
        for (Map.Entry<UUID, Integer> entry : Utils.regentime.entrySet()) {
            livesConfig.set(entry.getKey() + ".regentime", entry.getValue());
        }
        try {
            livesConfig.save(livesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GUIUtils getGuiUtils() {
        return guiUtils;
    }

    public void scheduleRepeatingTask(final Player player, long ticks) {
        final int tid = getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            FileConfiguration config = getConfig();
            World world = player.getWorld();

            if (!config.getBoolean("LifeRegen")) {
                return;
            }
            if (config.getBoolean("UseLifePermission") && !player.hasPermission("LifePermission")) {
                return;
            }
            if (config.getBoolean("UsePerWorld") && config.getStringList("Worlds").contains(world.getName())) {
                return;
            }
            if (Utils.getLives(player) > Utils.getMaxLives(player)) {
                Utils.setLives(player, Utils.getMaxLives(player));
                return;
            }
            if (Utils.getLives(player) < Utils.getMaxLives(player)) {
                int i = Utils.getLives(player);
                i++;
                Utils.setLives(player, i);
                player.sendMessage(Utils.replaceColors(
                        config.getString("GainLifeMessage")
                                .replace("%lives%", String.valueOf(Utils.getLives(player)))
                                .replace("%maxlives%", String.valueOf(Utils.getMaxLives(player)))
                ));
            }
        }, ticks * 1200, ticks * 1200);
        taskID.put(player.getUniqueId(), tid);
    }

    public void endTask(Player player) {
        if (taskID.containsKey(player.getUniqueId())) {
            int tid = taskID.get(player.getUniqueId());
            Bukkit.getServer().getScheduler().cancelTask(tid);
            taskID.remove(player.getUniqueId());
        }
    }
}