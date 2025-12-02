package me.rimuru.rankShop;

import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

public class InventoryListener implements Listener {

    private final RankShop plugin;

    public InventoryListener(RankShop plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getView().getTitle() == null) return;

        String title = Utils.color(plugin.getConfig().getString("gui.title"));
        if (!e.getView().getTitle().equals(title)) return;

        e.setCancelled(true);

        var player = (org.bukkit.entity.Player) e.getWhoClicked();
        int slot = e.getRawSlot();

        // Identify clicked item
        for (Object o : plugin.getConfig().getList("gui.items")) {
            if (!(o instanceof Map map)) continue;

            if ((int) map.get("slot") != slot) continue;

            String key = (String) map.get("key");
            if (key.equalsIgnoreCase("info")) {
                return;
            }

            String basePath = "ranks." + key;

            double price = plugin.getConfig().getDouble(basePath + ".price");
            String group = plugin.getConfig().getString(basePath + ".luckperms-group");

            double bal = plugin.getEconomy().getBalance(player.getUniqueId());
            String currency = plugin.getConfig().getString("currency.name");

            if (bal < price) {
                player.sendMessage(Utils.color(plugin.getConfig()
                                .getString("messages.not-enough"))
                        .replace("%price%", "" + price)
                        .replace("%currency%", currency));
                return;
            }

            // Check if rank is already owned
            if (plugin.getLuckPerms() != null && group != null && !group.isEmpty()) {
                var api = plugin.getLuckPerms();
                var user = api.getUserManager().getUser(player.getUniqueId());
                if (user != null && user.getPrimaryGroup().equalsIgnoreCase(group)) {
                    player.sendMessage(Utils.color(plugin.getConfig().getString("messages.already")));
                    return;
                }

                // Withdraw
                plugin.getEconomy().withdraw(player.getUniqueId(), price);

                // Add rank
                api.getUserManager().modifyUser(player.getUniqueId(), u ->
                        u.data().add(InheritanceNode.builder(group).build()));
            }

            player.sendMessage(Utils.color(plugin.getConfig().getString("messages.bought"))
                    .replace("%rank%", key)
                    .replace("%price%", "" + price)
                    .replace("%currency%", currency));

            player.closeInventory();
            return;
        }
    }
}