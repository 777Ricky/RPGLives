package net.minequests.gloriousmeme.rpglives.utils;

import com.cryptomorin.xseries.XMaterial;
import net.minequests.gloriousmeme.rpglives.RPGLives;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by GloriousMeme on 11/16/2016.
 */
public class GUIUtils {

    private Inventory livesShop = Bukkit.getServer().createInventory(null, 27, Utils.replaceColors(
            RPGLives.get().getConfig().getString("ShopName")));

    {
        ItemStack buyLife = new ItemStack(XMaterial.matchXMaterial(RPGLives.get().getConfig().getString("ShopBuyItem"))
                .orElse(XMaterial.STONE).parseMaterial(), 1,
                Short.valueOf(RPGLives.get().getConfig().getString("ShopBuyItemData")));
        ItemMeta buyLifeMeta = buyLife.getItemMeta();
        buyLifeMeta.setDisplayName(Utils.replaceColors(RPGLives.get().getConfig().getString("BuyItemName")));
        ArrayList<String> buyLifeLore = new ArrayList<>();
        buyLifeLore.add(String.valueOf(RPGLives.get().getConfig().getDouble("LifePrice")));
        buyLifeMeta.setLore(buyLifeLore);
        buyLife.setItemMeta(buyLifeMeta);

        ItemStack closeGUI = new ItemStack(XMaterial.matchXMaterial(RPGLives.get().getConfig().getString("ShopCloseItem"))
                .orElse(XMaterial.BARRIER).parseMaterial(), 1,
                Short.valueOf(RPGLives.get().getConfig().getString("ShopCloseItemData")));
        ItemMeta closeGUIMeta = closeGUI.getItemMeta();
        closeGUIMeta.setDisplayName(Utils.replaceColors(RPGLives.get().getConfig().getString("CloseItemName")));
        closeGUI.setItemMeta(closeGUIMeta);

        ItemStack borders = new ItemStack(XMaterial.matchXMaterial(RPGLives.get().getConfig().getString("ShopBorderItem"))
                .orElse(XMaterial.BLACK_STAINED_GLASS).parseMaterial(), 1,
                Short.valueOf(RPGLives.get().getConfig().getString("ShopBorderItemData")));
        ItemMeta borderMeta = borders.getItemMeta();
        borderMeta.setDisplayName(Utils.replaceColors(RPGLives.get().getConfig().getString("BorderName")));
        borders.setItemMeta(borderMeta);

        livesShop.setItem(11, buyLife);
        livesShop.setItem(15, closeGUI);

        for(int i = 0; i < 27; i++) {
            if(livesShop.getItem(i) == null)
                livesShop.setItem(i, borders);
        }
    }

    public double getLifePrice() {
        return RPGLives.get().getConfig().getDouble("LifePrice");
    }

    public Inventory getLivesShop() {
        return livesShop;
    }
}
