package com.ejafi.smoke;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Smoke.MOD_ID)
public final class SmokeData {
    private SmokeData() {}

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        event.getGenerator()
                .addProvider(
                        event.includeClient(),
                        new SmokeLanguageProvider(event.getGenerator().getPackOutput()));
    }
}
