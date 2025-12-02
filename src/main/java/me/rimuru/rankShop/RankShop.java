package me.rimuru.rankShop;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class RankShop extends JavaPlugin {

    private static RankShop instance;
    private LuckPerms luckPerms;
    private EconomyManager economy;
    private GUIManager gui;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        economy = new EconomyManager(this);
        economy.load();

        try {
            luckPerms = LuckPermsProvider.get();
            getLogger().info("LuckPerms hooked.");
        } catch (Exception e) {
            getLogger().warning("LuckPerms NOT found!");
        }

        gui = new GUIManager(this);

        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);

        getCommand("rankshop").setExecutor((sender, cmd, label, args) -> {
            // /rankshop reload
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {

                if (!sender.hasPermission("rankshop.admin")) {
                    sender.sendMessage(Utils.color("&cYou do not have permission."));
                    return true;
                }

                sender.sendMessage(Utils.color("&eReloading RankShop..."));

                this.reloadConfig();
                economy.load();

                sender.sendMessage(Utils.color("&aRankShop reloaded successfully!"));
                return true;
            }

            if (!(sender instanceof org.bukkit.entity.Player p)) {
                sender.sendMessage("Players only.");
                return true;
            }
            if (!p.hasPermission("rankshop.open")) {
                p.sendMessage(Utils.color(getConfig().getString("messages.no-permission")));
                return true;
            }
            gui.openShop(p);
            return true;
        });


        getCommand("rankshopbal").setExecutor(new CommandsAdminBalance(this));

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new RankShopPlaceholders(this).register();
            getLogger().info("PlaceholderAPI expansion registered.");
        }

        getLogger().info("RankShop enabled.");
    }

    @Override
    public void onDisable() {
        economy.save();
    }

    public static RankShop get() {
        return instance;
    }

    public LuckPerms getLuckPerms() { return luckPerms; }
    public EconomyManager getEconomy() { return economy; }
    public GUIManager getGui() { return gui; }
}
