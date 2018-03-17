package com.github.games647.clearnms;

import com.github.games647.clearnms.adapters.PlayerAdapter;
import com.github.games647.clearnms.asm.PlayerFactory;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ClearNMS extends JavaPlugin {

    private PlayerAdapter playerAdapter;

    @Override
    public void onEnable() {
        ServerVersion version = new ServerVersion(Bukkit.getServer().getClass());

        try {
            playerAdapter = new PlayerFactory(version).createAdapter();
        } catch (ReflectiveOperationException reflectiveEx) {
            //disable plugin on any errors
            getLogger().log(Level.SEVERE, "Cannot generate player adapter code", reflectiveEx);
            Bukkit.getScheduler().runTask(this, () -> Bukkit.getPluginManager().disablePlugin(this));
        }
    }

    public PlayerAdapter getPlayerAdapter() {
        return playerAdapter;
    }
}
