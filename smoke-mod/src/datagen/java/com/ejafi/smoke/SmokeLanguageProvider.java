package com.ejafi.smoke;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

final class SmokeLanguageProvider extends LanguageProvider {
    SmokeLanguageProvider(PackOutput output) {
        super(output, Smoke.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlock(SmokeBlocks.SMOKE_BLOCK, "Smoke Block");
    }
}
