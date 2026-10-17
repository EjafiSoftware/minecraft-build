package com.ejafi.smoke;

import static org.assertj.core.api.Assertions.assertThat;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

class SmokeBlocksTest {
    @Test
    void register_addsSmokeBlockToBlockRegistry_byDefault() {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(SmokeBlocks.SMOKE_BLOCK.get());

        assertThat(key).isEqualTo(ResourceLocation.fromNamespaceAndPath(Smoke.MOD_ID, "smoke_block"));
    }
}
