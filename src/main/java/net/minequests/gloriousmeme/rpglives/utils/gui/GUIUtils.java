package net.minequests.gloriousmeme.rpglives.utils.gui;

import com.cryptomorin.xseries.XMaterial;
import net.minequests.gloriousmeme.rpglives.RPGLives;
import net.minequests.gloriousmeme.rpglives.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.Optional;

/**
 * Created by GloriousMeme on 11/16/2016.
 */
public class GUIUtils {
    private final RPGLives plugin;
    private Inventory livesShop;

    public static Optional<Material> parseMaterial(String input) {
        return XMaterial.matchXMaterial(input)
                .map(XMaterial::parseMaterial);
    }

    public GUIUtils(RPGLives plugin) {
        this.plugin = plugin;

        ConfigurationSection config = plugin.getConfig();

        livesShop = Bukkit.getServer().createInventory(
                null,
                27,
                Utils.replaceColors(config.getString("ShopName"))
        );

        ItemStack buyLife = new ItemStack(
                parseMaterial(config.getString("ShopBuyItem"))
                        .orElse(Material.STONE),
                1,
                Short.parseShort(config.getString("ShopBuyItemData"))
        );
        ItemMeta buyLifeMeta = buyLife.getItemMeta();
        buyLifeMeta.setDisplayName(Utils.replaceColors(config.getString("BuyItemName")));
        buyLifeMeta.setLore(Collections.singletonList(
                config.getString("LifePrice")
        ));
        buyLife.setItemMeta(buyLifeMeta);

        ItemStack closeGUI = new ItemStack(
                parseMaterial(config.getString("ShopCloseItem"))
                        .orElse(Material.STONE),
                1,
                Short.parseShort(config.getString("ShopCloseItemData"))
        );
        ItemMeta closeGUIMeta = closeGUI.getItemMeta();
        closeGUIMeta.setDisplayName(Utils.replaceColors(config.getString("CloseItemName")));
        closeGUI.setItemMeta(closeGUIMeta);

        ItemStack border = new ItemStack(
                parseMaterial(config.getString("ShopBorderItem"))
                        .orElse(Material.STONE),
                1,
                Short.parseShort(config.getString("ShopBorderItemData"))
        );
        ItemMeta borderMeta = border.getItemMeta();
        borderMeta.setDisplayName(Utils.replaceColors(config.getString("BorderName")));
        border.setItemMeta(borderMeta);

        livesShop.setItem(11, buyLife);
        livesShop.setItem(15, closeGUI);

        for (int i = 0; i < 27; i++) {
            if (livesShop.getItem(i) == null) {
                livesShop.setItem(i, border);
            }
        }
    }

    public double getLifePrice() {
        return plugin.getConfig().getDouble("LifePrice");
    }

    public Inventory getLivesShop() {
        return livesShop;
    }
}
