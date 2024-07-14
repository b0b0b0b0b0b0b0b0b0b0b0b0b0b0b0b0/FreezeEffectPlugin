package org.b0b0bo.freezeeffectplugin;

import org.b0b0bo.freezeeffectplugin.h.cmd;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class FreezeEffectPlugin extends JavaPlugin implements Listener {

    private final HashMap<UUID, Long> playerTimes = new HashMap<>();
    private final Set<Material> freezingBlocks = new HashSet<>();
    private int freezeDuration;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        logActiveBlocks();
        Objects.requireNonNull(getCommand("fep")).setExecutor(new cmd(this));
    }

    public void loadConfig() {
        FileConfiguration config = getConfig();
        List<String> blockList = config.getStringList("freezing-blocks");
        freezingBlocks.clear();
        for (String blockName : blockList) {
            freezingBlocks.add(Material.getMaterial(blockName));
        }
        freezeDuration = config.getInt("freeze-duration", 140);
    }

    public Set<String> getFreezingBlocks() {
        return freezingBlocks.stream().map(Material::toString).collect(Collectors.toSet());
    }

    private void logActiveBlocks() {
        getLogger().info("\u001B[36m===================================\u001B[0m");
        getLogger().info("\u001B[34m  FreezeEffectPlugin \u001B[32msuccessfully enabled\u001B[0m");
        getLogger().info("\u001B[36m  Active freezing blocks: \u001B[0m");
        for (Material block : freezingBlocks) {
            getLogger().info("\u001B[33m   - " + block.toString() + "\u001B[0m");
        }
        getLogger().info("\u001B[36m  Freeze duration: \u001B[35m" + freezeDuration + " ticks\u001B[0m");
        getLogger().info("\u001B[36m===================================\u001B[0m");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block blockUnderPlayer = player.getLocation().subtract(0, 1, 0).getBlock();
        Material blockType = blockUnderPlayer.getType();
        UUID playerUUID = player.getUniqueId();

        if (freezingBlocks.contains(blockType)) {
            if (!playerTimes.containsKey(playerUUID)) {
                playerTimes.put(playerUUID, System.currentTimeMillis());
            } else {
                long timeOnBlock = System.currentTimeMillis() - playerTimes.get(playerUUID);
                if (timeOnBlock >= 1000) {
                    player.setFreezeTicks(freezeDuration);
                    playerTimes.put(playerUUID, System.currentTimeMillis());
                }
            }
        } else {
            playerTimes.remove(playerUUID);
        }
    }
}
