package me.rimuru.rankShop;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUIManager {

    private final RankShop plugin;

    public GUIManager(RankShop plugin) {
        this.plugin = plugin;
    }

    public void openShop(Player player) {
        // --- GUI TITLE PARSED WITH PAPI ---
        String title = plugin.getConfig().getString("gui.title", "&cRank Shop");
        title = Utils.color(title);
        title = PlaceholderAPI.setPlaceholders(player, title);

        int rows = plugin.getConfig().getInt("gui.rows", 3);
        Inventory inv = Bukkit.createInventory(null, rows * 9, title);

        // --- FILLER ITEMS ---
        boolean filler = plugin.getConfig().getBoolean("gui.filler", true);
        if (filler) {
            ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta meta = glass.getItemMeta();
            meta.setDisplayName(" ");
            glass.setItemMeta(meta);

            for (int i = 0; i < inv.getSize(); i++) {
                inv.setItem(i, glass);
            }
        }

        // --- LOAD GUI ITEMS ---
        List<?> items = plugin.getConfig().getList("gui.items");
        if (items == null) return;

        for (Object o : items) {
            if (!(o instanceof Map<?, ?> map)) continue;

            int slot = (int) map.get("slot");
            String key = (String) map.get("key");
            Material mat = Material.valueOf((String) map.get("material"));

            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();

            // --- DISPLAY NAME WITH PAPI ---
            String display = plugin.getConfig().getString("ranks." + key + ".display", "&cMissing Name");
            display = Utils.color(display);
            display = PlaceholderAPI.setPlaceholders(player, display);
            meta.setDisplayName(display);

            // --- LORE WITH PAPI ---
            List<String> lore = plugin.getConfig().getStringList("ranks." + key + ".lore");
            List<String> parsedLore = new ArrayList<>();

            for (String line : lore) {
                line = Utils.color(line);
                line = PlaceholderAPI.setPlaceholders(player, line);
                parsedLore.add(line);
            }

            meta.setLore(parsedLore);
            item.setItemMeta(meta);

            inv.setItem(slot, item);
        }

        player.openInventory(inv);
    }
}
