package com.ejafi.smoke;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Smoke.MOD_ID)
public final class Smoke {
    public static final String MOD_ID = "smoke";

    public Smoke(IEventBus modEventBus) {
        SmokeBlocks.register(modEventBus);
    }
}
