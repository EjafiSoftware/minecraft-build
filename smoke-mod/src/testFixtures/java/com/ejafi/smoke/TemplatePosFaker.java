package com.ejafi.smoke;

import net.datafaker.Faker;
import net.minecraft.core.BlockPos;

public final class TemplatePosFaker {
    public static final int TEMPLATE_SIZE = 7;

    private static final Faker FAKER = new Faker();

    private final int x = FAKER.number().numberBetween(0, TEMPLATE_SIZE);
    private final int y = FAKER.number().numberBetween(0, TEMPLATE_SIZE);
    private final int z = FAKER.number().numberBetween(0, TEMPLATE_SIZE);

    public BlockPos generate() {
        return new BlockPos(x, y, z);
    }
}
