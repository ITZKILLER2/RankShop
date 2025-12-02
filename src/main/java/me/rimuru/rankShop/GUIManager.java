package me.rimuru.rankShop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GUIManager {

    private final RankShop plugin;

    public GUIManager(RankShop plugin) {
        this.plugin = plugin;
    }

    public void openShop(Player player) {
        String title = Utils.color(plugin.getConfig().getString("gui.title"));
        int rows = plugin.getConfig().getInt("gui.rows", 3);

        Inventory inv = Bukkit.createInventory(null, rows * 9, title);

        boolean filler = plugin.getConfig().getBoolean("gui.filler", true);
        if (filler) {
            ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta meta = glass.getItemMeta();
            meta.setDisplayName(" ");
            glass.setItemMeta(meta);

            for (int i = 0; i < inv.getSize(); i++)
                inv.setItem(i, glass);
        }

        List<?> items = plugin.getConfig().getList("gui.items");
        for (Object o : items) {
            if (!(o instanceof java.util.Map map)) continue;

            int slot = (int) map.get("slot");
            String key = (String) map.get("key");
            Material mat = Material.valueOf((String) map.get("material"));

            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();

            String display = plugin.getConfig().getString("ranks." + key + ".display");
            meta.setDisplayName(Utils.color(display));

            List<String> lore = plugin.getConfig().getStringList("ranks." + key + ".lore");
            lore = lore.stream().map(Utils::color).toList();
            meta.setLore(lore);

            item.setItemMeta(meta);
            inv.setItem(slot, item);
        }

        player.openInventory(inv);
    }
}