package com.ejafi.smoke;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(Smoke.MOD_ID)
@PrefixGameTestTemplate(false)
public final class SmokeBlockGameTests {
    private static final String EMPTY_7X7X7 = "empty_7x7x7";

    private SmokeBlockGameTests() {}

    @GameTest(template = EMPTY_7X7X7)
    public static void set_block_places_smoke_block_by_default(GameTestHelper helper) {
        BlockPos pos = new TemplatePosFaker().generate();

        helper.setBlock(pos, SmokeBlocks.SMOKE_BLOCK.get());

        helper.assertBlockPresent(SmokeBlocks.SMOKE_BLOCK.get(), pos);
        helper.succeed();
    }
}
