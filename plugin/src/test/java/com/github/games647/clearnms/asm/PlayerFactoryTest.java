package com.github.games647.clearnms.asm;

import com.github.games647.clearnms.ServerVersion;
import com.github.games647.clearnms.adapters.PlayerAdapter;

import net.minecraft.server.v1_12_R1.EntityPlayer;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerFactoryTest {

    private PlayerAdapter adapter;

    @Before
    public void setUp() throws Exception {
        PlayerFactory factory = new PlayerFactory(new ServerVersion(CraftServer.class.getPackageName()));
        adapter = factory.createAdapter();
    }

    @Test
    public void testGetPing() throws Exception {
        // setting field behavior doesn't work, but Reflections works here
        EntityPlayer player = mock(EntityPlayer.class);
        EntityPlayer.class.getDeclaredField("ping").set(player, 1337);

        CraftPlayer craftPlayer = mock(CraftPlayer.class);
        when(craftPlayer.getHandle()).thenReturn(player);

        assertThat(adapter.getPing(craftPlayer), is(1337));
    }

    @Test
    public void testCredits() throws Exception {
        // setting field behavior doesn't work, but Reflections works here
        EntityPlayer player = mock(EntityPlayer.class);
        EntityPlayer.class.getDeclaredField("viewingCredits").set(player, true);

        CraftPlayer craftPlayer = mock(CraftPlayer.class);
        when(craftPlayer.getHandle()).thenReturn(player);

        assertThat(adapter.isViewingCredits(craftPlayer), is(true));
    }
}
