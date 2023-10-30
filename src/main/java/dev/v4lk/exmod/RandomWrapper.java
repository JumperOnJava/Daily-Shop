package dev.v4lk.exmod;

import java.util.Random;

public class RandomWrapper extends Random {
    net.minecraft.util.math.random.Random mcRandom;
    public RandomWrapper(net.minecraft.util.math.random.Random mcRandom) {
        this.mcRandom = mcRandom;
    }

    @Override
    public int nextInt() {
        return mcRandom.nextInt();
    }

    @Override
    public int nextInt(int bound) {
        return mcRandom.nextInt(bound);
    }

    @Override
    public int nextInt(int origin, int bound) {
        return mcRandom.nextBetween(origin, bound);
    }
}
