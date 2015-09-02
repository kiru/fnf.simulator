package com.zuehlke.carrera.simulator.model;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Random;

/**
 * models the noise as a linear function of the velocity
 */
public class Noise {

    public final float constantLevel;

    public final float relativeLevel;

    private final Random random;

    public Noise(float constantLevel, float relativeLevel) {
        this.constantLevel = constantLevel;
        this.relativeLevel = relativeLevel;
        random = new Random(ByteBuffer.wrap(SecureRandom.getSeed(8)).getLong());
    }

    /**
     * adds noice to any value
     *
     * @param originalValue the original value
     * @return the original value plus the noise
     */
    public float apply(float originalValue) {
        float constant = constantLevel * (2.0f * random.nextFloat() - 1.0f);
        float relative = relativeLevel * (2.0f * random.nextFloat() - 1.0f);
        return constant + originalValue * (1.0f + relative);
    }
}
