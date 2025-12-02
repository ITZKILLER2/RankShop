package me.rimuru.rankShop;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandsAdminBalance implements CommandExecutor {

    private final RankShop plugin;

    public CommandsAdminBalance(RankShop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] a) {
        if (!s.hasPermission("rankshop.admin")) {
            s.sendMessage(Utils.color(plugin.getConfig().getString("messages.no-permission")));
            return true;
        }

        if (a.length < 3) {
            s.sendMessage("/rankshopbal <set|add|remove> <player> <amount>");
            return true;
        }

        String action = a[0];
        Player target = Bukkit.getPlayer(a[1]);
        if (target == null) {
            s.sendMessage("Player not online.");
            return true;
        }

        double amount;
        try { amount = Double.parseDouble(a[2]); }
        catch (Exception e) { s.sendMessage("Invalid amount."); return true; }

        switch (action.toLowerCase()) {
            case "set" -> {
                plugin.getEconomy().setBalance(target.getUniqueId(), amount);
                s.sendMessage(Utils.color(plugin.getConfig().getString("messages.admin-set"))
                        .replace("%player%", target.getName())
                        .replace("%amount%", "" + amount));
            }
            case "add" -> {
                plugin.getEconomy().addBalance(target.getUniqueId(), amount);
                s.sendMessage(Utils.color(plugin.getConfig().getString("messages.admin-add"))
                        .replace("%player%", target.getName())
                        .replace("%amount%", "" + amount));
            }
            case "remove" -> {
                plugin.getEconomy().removeBalance(target.getUniqueId(), amount);
                s.sendMessage(Utils.color(plugin.getConfig().getString("messages.admin-remove"))
                        .replace("%player%", target.getName())
                        .replace("%amount%", "" + amount));
            }
            default -> s.sendMessage("Unknown action.");
        }

        return true;
    }
}