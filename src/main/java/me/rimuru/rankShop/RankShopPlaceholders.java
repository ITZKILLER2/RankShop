package me.rimuru.rankShop;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class RankShopPlaceholders extends PlaceholderExpansion {

    private final RankShop plugin;

    public RankShopPlaceholders(RankShop plugin) {
        this.plugin = plugin;
    }

    @Override public String getIdentifier() { return "rankshop"; }
    @Override public String getAuthor() { return "Rimuru"; }
    @Override public String getVersion() { return plugin.getDescription().getVersion(); }
    @Override public boolean persist() { return true; }

    @Override
    public String onPlaceholderRequest(Player p, String id) {
        if (p == null) return "";

        return switch (id) {
            case "balance" -> "" + (long) plugin.getEconomy().getBalance(p.getUniqueId());
            case "currency" -> plugin.getConfig().getString("currency.name");
            default -> null;
        };
    }
}