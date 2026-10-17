package com.ejafi.smoke;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class SmokeBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Smoke.MOD_ID);

    public static final DeferredBlock<Block> SMOKE_BLOCK =
            BLOCKS.register("smoke_block", () -> new Block(BlockBehaviour.Properties.of()));

    private SmokeBlocks() {}

    static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
