package com.github.games647.clearnms.asm;

import org.junit.Test;

import static org.hamcrest.core.Is.is;

import static org.junit.Assert.assertThat;

public class AdapterFactoryTest {

    @Test
    public void toInternalName() {
        assertThat(AdapterFactory.toInternalName("com.mojang.test"), is("com/mojang/test"));
    }

    @Test
    public void toExternalName() {
        assertThat(AdapterFactory.toExternalName("com/mojang/test"), is("com.mojang.test"));
    }
}
