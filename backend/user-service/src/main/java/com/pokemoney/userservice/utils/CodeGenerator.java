package com.pokemoney.userservice.utils;

/**
 * This class is used to generate various codes.
 */
public class CodeGenerator {
    /**
     * Generate a random number with the given length.
     * @param length Length of the number.
     * @return Random number.
     */
    public static int GenerateNumber(int length) {
        return (int) ((Math.random() * 9 + 1) * Math.pow(10, length - 1));
    }
}
