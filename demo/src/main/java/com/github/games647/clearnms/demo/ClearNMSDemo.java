package com.github.games647.clearnms.demo;

import com.github.games647.clearnms.ClearNMS;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ClearNMSDemo extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(this, () -> {
            if (!player.isOnline()) {
                return;
            }

            ClearNMS clearPlugin = JavaPlugin.getPlugin(ClearNMS.class);
            int ping = clearPlugin.getPlayerAdapter().getPing(player);

            getLogger().log(Level.INFO, "Player {0} ping: {1}ms", new Object[]{player.getName(), ping});
        }, 20L);
    }
}
