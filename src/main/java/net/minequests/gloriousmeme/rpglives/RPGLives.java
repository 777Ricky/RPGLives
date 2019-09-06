package net.minequests.gloriousmeme.rpglives;

import net.milkbowl.vault.economy.Economy;
import net.minequests.gloriousmeme.rpglives.command.AbstractCommand;
import net.minequests.gloriousmeme.rpglives.command.TabCompletionHandler;
import net.minequests.gloriousmeme.rpglives.events.*;
import net.minequests.gloriousmeme.rpglives.utils.GUIUtils;
import net.minequests.gloriousmeme.rpglives.utils.PlaceHolderAPIHook;
import net.minequests.gloriousmeme.rpglives.utils.Utils;
import net.minequests.gloriousmeme.rpglives.utils.versions.*;
import org.bukkit.Bukkit;
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

    private static RPGLives plugin;
    private static RPGLivesAPI rpgLivesAPI;

    private static Economy economy = null;

    private File livesf;
    private FileConfiguration livesl;

    private String name = getServer().getClass().getPackage().getName();
    private String version = name.substring(name.lastIndexOf('.') + 1);

    public Actionbar actionbar;
    private GUIUtils guiUtils;

    private HashMap<UUID, Integer> taskID = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        rpgLivesAPI = new RPGLivesAPI();
        guiUtils = new GUIUtils();

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

        if (setupActionbar())
            getLogger().info("Actionbar setup was successful!");
        else
            getLogger().severe("Failed to setup Actionbar please use a compatible server version or disable TitleEnabled in the config!");


        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceHolderAPIHook(this).hook();
            getLogger().info("Hooked into PlaceholderAPI.");
        } else
            getLogger().info("Could not find PlaceholderAPI continuing without it.");
    }

    @Override
    public void onDisable() {
        saveHashmapData();

        plugin = null;
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
        return plugin;
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

    public String getVersion() {
        return version;
    }

    public FileConfiguration getLivesl() {
        return livesl;
    }

    private void createFiles() {
        livesf = new File(getDataFolder(), "lives.yml");

        if (!livesf.exists()) {
            livesf.getParentFile().mkdirs();
            saveResource("lives.yml", false);
        }
        livesl = new YamlConfiguration();
        try {
            livesl.load(livesf);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void saveHashmapData() {
        for (Map.Entry<UUID, Integer> entry : Utils.lives.entrySet()) {
            livesl.set(entry.getKey() + ".lives", entry.getValue());
        }
        for (Map.Entry<UUID, Integer> entry : Utils.maxlives.entrySet()) {
            livesl.set(entry.getKey() + ".maxlives", entry.getValue());
        }
        for (Map.Entry<UUID, Integer> entry : Utils.regentime.entrySet()) {
            livesl.set(entry.getKey() + ".regentime", entry.getValue());
        }
        try {
            livesl.save(livesf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean setupActionbar() {

        if (!getConfig().getBoolean("TitleEnabled"))
            return false;

        String version;

        try {
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return false;
        }

        getLogger().info("Your server is running version " + version);

        if (version.contains("v1_8_R1"))
            actionbar = new Actionbar_1_8();
        else if (version.contains("v1_8_R3"))
            actionbar = new Actionbar_1_8_8();
        else if (version.contains("v1_9_R2"))
            actionbar = new Actionbar_1_9_4();
        else if (version.contains("v1_10_R1"))
            actionbar = new Actionbar_1_10();
        else if (version.contains("v1_11_R1"))
            actionbar = new Actionbar_1_11();
        else if (version.contains("v1_12_R1"))
            actionbar = new Actionbar_1_12();
        else if (version.contains("v1_13_R2"))
            actionbar = new Actionbar_1_13();

        return actionbar != null;
    }

    public GUIUtils getGuiUtils() {
        return guiUtils;
    }

    public void scheduleRepeatingTask(final Player player, long ticks) {
        final int tid = getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (!getConfig().getBoolean("LifeRegen"))
                return;
            if (getConfig().getBoolean("UseLifePermission") && !player.hasPermission("LifePermission"))
                return;
            if (getConfig().getBoolean("UsePerWorld") && getConfig().getStringList("Worlds").contains(player.getWorld().getName()))
                return;
            if (Utils.getLives(player) > Utils.getMaxLives(player)) {
                Utils.setLives(player, Utils.getMaxLives(player));
                return;
            }
            if (Utils.getLives(player) < Utils.getMaxLives(player)) {
                int i = Utils.getLives(player);
                i++;
                Utils.setLives(player, i);
                player.sendMessage(Utils.replaceColors(getConfig().getString("GainLifeMessage").replace("<lives>",
                        String.valueOf(Utils.getLives(player)))).replace("<maxlives>", String.valueOf(Utils.getMaxLives(player))));
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