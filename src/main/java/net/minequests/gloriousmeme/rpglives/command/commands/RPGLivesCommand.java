package net.minequests.gloriousmeme.rpglives.command.commands;

import net.minequests.gloriousmeme.rpglives.RPGLives;
import net.minequests.gloriousmeme.rpglives.command.AbstractCommand;
import net.minequests.gloriousmeme.rpglives.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RPGLivesCommand extends AbstractCommand {

    public RPGLivesCommand() {
        super("rpglives", "rpglives.use", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            if (!sender.hasPermission("rpglives.help")) {
                sender.sendMessage(Utils.replaceColors(RPGLives.get().getConfig().getString("NoPermsMessage")));
                return;
            }
            Utils.sendHelpMessage(sender);
            return;
        }
        if (args[0].equalsIgnoreCase("setmaxlives")) {
            if (!sender.hasPermission("rpglives.setmaxlives")) {
                sender.sendMessage(Utils.replaceColors(RPGLives.get().getConfig().getString("NoPermsMessage")));
                return;
            }
            if (args.length < 3) {
                sender.sendMessage(Utils.replaceColors("&4Usage: /rpglives setmaxlives <player> <amount>"));
                return;
            }
            Player target = Bukkit.getServer().getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(Utils.replaceColors("&4The player " + args[1] + " is not online."));
                return;
            }
            if (!Utils.isNumber(args[2])) {
                sender.sendMessage(Utils.replaceColors("&4That is not a number please use a number."));
                return;
            }
            if (Integer.valueOf(args[2]) <= 0) {
                sender.sendMessage(Utils.replaceColors("&4You can not set a player's max lives lower than 1"));
                return;
            }
            sender.sendMessage(Utils.replaceColors("&aYou set " + target.getName() + "'s max lives to " + args[2] + "."));
            Utils.setMaxLives(target, Integer.valueOf(args[2]));
            return;
        }
        if (args[0].equalsIgnoreCase("setlives")) {
            if (!sender.hasPermission("rpglives.setlives")) {
                sender.sendMessage(Utils.replaceColors(RPGLives.get().getConfig().getString("NoPermsMessage")));
                return;
            }
            if (args.length < 3) {
                sender.sendMessage(Utils.replaceColors("&4Usage: /rpglives setlives <player> <amount>"));
                return;
            }
            Player target = Bukkit.getServer().getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(Utils.replaceColors("&4That player is not online."));
                return;
            }
            if (!Utils.isNumber(args[2])) {
                sender.sendMessage(Utils.replaceColors("&4That is not a number please use a number."));
                return;
            }
            if (Integer.valueOf(args[2]) > Utils.getMaxLives(target)) {
                sender.sendMessage(Utils.replaceColors("&4You can not set a player's lives higher than their max lives in the config."));
                return;
            }
            sender.sendMessage(Utils.replaceColors("&aYou set " + target.getName() + "'s lives to " + args[2] + "."));
            Utils.setLives(target, Integer.valueOf(args[2]));
            return;
        }
        if (args[0].equalsIgnoreCase("setregentime")) {
            if (!sender.hasPermission("rpglives.setregentime")) {
                sender.sendMessage(Utils.replaceColors(RPGLives.get().getConfig().getString("NoPermsMessage")));
                return;
            }
            if (args.length < 3) {
                sender.sendMessage(Utils.replaceColors("&4Usage: /rpglives setregentime <player> <time>"));
                return;
            }
            Player target = Bukkit.getServer().getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(Utils.replaceColors("$That player is not online."));
                return;
            }
            if (!Utils.isNumber(args[2])) {
                sender.sendMessage(Utils.replaceColors("&4That is not a number please use a number."));
                return;
            }
            if (Integer.valueOf(args[2]) < 0) {
                sender.sendMessage(Utils.replaceColors("&4You can't set a player's life regen lower than 0 minute(s)."));
                return;
            }
            sender.sendMessage(Utils.replaceColors("&aYou set " + target.getName() + "'s live regen to " + args[2] + " minute(s)."));
            Utils.setRegenTime(target, Integer.valueOf(args[2]));
            return;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("rpglives.reload")) {
                sender.sendMessage(Utils.replaceColors(RPGLives.get().getConfig().getString("NoPermsMessage")));
                return;
            }
            sender.sendMessage(Utils.replaceColors("&aThe RPGLives config has been reloaded."));
            RPGLives.get().reloadConfig();
            return;
        }
        if (args[0].equalsIgnoreCase("giveitem")) {
            if (!sender.hasPermission("rpglives.giveitem")) {
                sender.sendMessage(Utils.replaceColors(RPGLives.get().getConfig().getString("NoPermsMessage")));
                return;
            }
            if (args.length < 3) {
                sender.sendMessage(Utils.replaceColors("&4Usage: /rpglives giveitem <player> <amount>"));
                return;
            }
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(Utils.replaceColors("&4Player not found."));
                return;
            }
            if (!Utils.isNumber(args[2])) {
                sender.sendMessage(Utils.replaceColors("&4The amount must be a number."));
                return;
            }
            int itemAmount = Integer.parseInt(args[2]);
            ItemStack rpgItem = new ItemStack(Material.valueOf(RPGLives.get().getConfig().getString("LifeItemType")), itemAmount);
            ItemMeta rpgItemMeta = rpgItem.getItemMeta();
            rpgItemMeta.setDisplayName(Utils.replaceColors(RPGLives.get().getConfig().getString("LifeItemName")));
            rpgItemMeta.setLore(RPGLives.get().getConfig().getStringList("LifeItemLore"));
            rpgItem.setItemMeta(rpgItemMeta);

            if (itemAmount == 1) {
                sender.sendMessage(Utils.replaceColors(Utils.replaceColors(RPGLives.get().getConfig().getString("GiveLifeItemMessage").replaceAll("%player%", target.getName())))
                        .replaceAll("%amount%", args[2]).replaceAll("%item%", RPGLives.get().getConfig().getString("LifeItemName")));
                target.getInventory().addItem(rpgItem);
                return;
            } else {
                sender.sendMessage(Utils.replaceColors(Utils.replaceColors(RPGLives.get().getConfig().getString("GiveMultipleLifeItemMessage").replaceAll("%player%", target.getName())))
                        .replaceAll("%amount%", args[2]).replaceAll("%item%", RPGLives.get().getConfig().getString("LifeItemName")));
                target.getInventory().addItem(rpgItem);
                return;
            }
        }
        if (args[0].equalsIgnoreCase("shop")) {
            if (!RPGLives.get().getConfig().getBoolean("ShopEnabled")) {
                sender.sendMessage(Utils.replaceColors("&4The shop is not enabled you must enable it in the config."));
                return;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(Utils.replaceColors("&4Only players can use this command."));
                return;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("rpglives.shop")) {
                sender.sendMessage(RPGLives.get().getConfig().getString("NoPermsMessage"));
                return;
            }
            player.openInventory(RPGLives.get().getGuiUtils().getLivesShop());
            player.sendRawMessage(Utils.replaceColors("&aShop opening."));
        }
    }
}