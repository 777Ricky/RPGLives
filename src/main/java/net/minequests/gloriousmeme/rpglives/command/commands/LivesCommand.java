package net.minequests.gloriousmeme.rpglives.command.commands;

import net.minequests.gloriousmeme.rpglives.RPGLives;
import net.minequests.gloriousmeme.rpglives.command.AbstractCommand;
import net.minequests.gloriousmeme.rpglives.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LivesCommand extends AbstractCommand {

    public LivesCommand() {
        super("lives", "rpglives.command.use", false);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            if (RPGLives.get().getConfig().getStringList("UnlimitedLivesWorlds").contains(player.getWorld().getName())) {
                player.sendMessage(Utils.replaceColors(RPGLives.get().getConfig().getString("LivesCommandMessage").replaceAll("%lives%",
                        String.valueOf(Utils.getLives(player))).replaceAll("%maxlives%", "unlimited")));
            } else {
                player.sendMessage(Utils.replaceColors(RPGLives.get().getConfig().getString("LivesCommandMessage").replaceAll("%lives%",
                        String.valueOf(Utils.getLives(player))).replaceAll("%maxlives%", String.valueOf(Utils.getMaxLives(player)))));
                return;
            }
            player.sendMessage(Utils.replaceColors(RPGLives.get().getConfig().getString("LivesCommandMessage").replaceAll("%lives%",
                    String.valueOf(Utils.getLives(player))).replaceAll("%maxlives%", String.valueOf(Utils.getMaxLives(player)))));
        } else if (args.length == 1) {
            Player target = Bukkit.getServer().getPlayerExact(args[0]);
            if (!player.hasPermission("rpglives.lives.others")) {
                player.sendMessage(Utils.replaceColors("&4You do not have permission to use this command."));
                return;
            }
            if (target == null) {
                player.sendMessage(Utils.replaceColors("&4Please specify a valid player."));
                return;
            }
            if (RPGLives.get().getConfig().getStringList("UnlimitedLivesWorlds").contains(target.getWorld().getName())) {
                player.sendMessage(Utils.replaceColors(RPGLives.get().getConfig().getString("LivesCommandMessage").replaceAll("%lives%",
                        String.valueOf(Utils.getLives(target))).replaceAll("%maxlives%", "unlimited")));
            } else {
                player.sendMessage(Utils.replaceColors(RPGLives.get().getConfig().getString("LivesCommandMessage").replaceAll("%lives%",
                        String.valueOf(Utils.getLives(target))).replaceAll("%maxlives%", String.valueOf(Utils.getMaxLives(target)))));
            }
        }
    }
}
