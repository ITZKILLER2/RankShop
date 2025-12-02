package me.rimuru.rankShop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandReload implements CommandExecutor {

    private final RankShop plugin;

    public CommandReload(RankShop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("rankshop.admin")) {
            sender.sendMessage(Utils.color("&cYou don't have permission."));
            return true;
        }

        sender.sendMessage(Utils.color("&eReloading RankShop..."));

        // Reload config
        plugin.reloadConfig();

        // Reload economy file
        plugin.getEconomy().load();

        sender.sendMessage(Utils.color("&aRankShop reloaded successfully!"));
        return true;
    }
}
