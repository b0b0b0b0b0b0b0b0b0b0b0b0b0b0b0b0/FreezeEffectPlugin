package org.b0b0bo.freezeeffectplugin.h;

import org.b0b0bo.freezeeffectplugin.FreezeEffectPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class cmd implements CommandExecutor {

    private final Plugin plugin;

    public cmd(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("fep.admin")) {
                plugin.reloadConfig();
                ((FreezeEffectPlugin) plugin).loadConfig();
                sender.sendMessage("§aКонфигурация FreezeEffectPlugin перезагружена.");
                sender.sendMessage("§aЗагруженные блоки: " + String.join(", ", ((FreezeEffectPlugin) plugin).getFreezingBlocks()));
                return true;
            } else {
                sender.sendMessage("§cУ вас нет прав для выполнения этой команды.");
                return true;
            }
        }
        return false;
    }
}
