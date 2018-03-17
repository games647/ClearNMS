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
}
