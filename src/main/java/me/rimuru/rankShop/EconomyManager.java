package me.rimuru.rankShop;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public class EconomyManager {

    private final RankShop plugin;
    private File file;
    private FileConfiguration cfg;

    public EconomyManager(RankShop plugin) {
        this.plugin = plugin;
    }

    public void load() {
        file = new File(plugin.getDataFolder(), plugin.getConfig().getString("economy.file", "balances.yml"));
        if (!file.exists()) {
            plugin.getDataFolder().mkdirs();
            try { file.createNewFile(); } catch (Exception ignored) {}
        }
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try { cfg.save(file); } catch (Exception ignored) {}
    }

    public double getBalance(UUID uuid) {
        return cfg.getDouble(uuid.toString(), 0);
    }

    public void setBalance(UUID uuid, double amount) {
        cfg.set(uuid.toString(), amount);
        save();
    }

    public void addBalance(UUID uuid, double amount) {
        setBalance(uuid, getBalance(uuid) + amount);
    }

    public void removeBalance(UUID uuid, double amount) {
        setBalance(uuid, Math.max(0, getBalance(uuid) - amount));
    }

    public boolean withdraw(UUID uuid, double amount) {
        double bal = getBalance(uuid);
        if (bal < amount) return false;
        setBalance(uuid, bal - amount);
        return true;
    }
}