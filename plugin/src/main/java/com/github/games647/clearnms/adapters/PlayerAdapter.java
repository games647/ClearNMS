package com.github.games647.clearnms.adapters;

import org.bukkit.entity.Player;

/**
 * Provides accessibility to player related internals.
 */
public interface PlayerAdapter {

    /**
     * @param player Bukkit player
     * @return the ping in ms
     */
    int getPing(Player player);

    /**
     * @param player Bukkit player
     * @return true if the credits screen is showing to the client
     */
    boolean isViewingCredits(Player player);
}
