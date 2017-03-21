package com.opp.util;

import java.util.Random;

/**
 * Created by ctobe on 8/24/16.
 */
public class MathUtil {

    public static int getRandomInt(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
