package com.pokemoney.userservice.utils;

import java.security.SecureRandom;

/**
 * This class is used to generate various codes.
 */
public class CodeGenerator {
    /**
     * Generate a random number with the given length.
     * @param length Length of the number.
     * @return Random number.
     */
    public static long GenerateNumber(int length) {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextLong((long) Math.pow(10, length));
    }
}
