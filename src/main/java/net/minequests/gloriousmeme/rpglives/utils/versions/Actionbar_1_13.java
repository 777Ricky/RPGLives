package net.minequests.gloriousmeme.rpglives.utils.versions;

import net.minecraft.server.v1_13_R2.ChatComponentText;
import net.minecraft.server.v1_13_R2.ChatMessageType;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import net.minequests.gloriousmeme.rpglives.utils.Utils;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by GloriousMeme on 7/16/2018.
 */
public class Actionbar_1_13 implements Actionbar {

    @Override
    public void sendActionbar(Player player, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(Utils.replaceColors(message)), ChatMessageType.CHAT);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
