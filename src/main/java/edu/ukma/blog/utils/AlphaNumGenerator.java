package edu.ukma.blog.utils;

import java.security.SecureRandom;
import java.util.Random;

public class AlphaNumGenerator {
    private static final char[] ALPHA_NUM = "1234567890abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final Random RANDOM = new SecureRandom();

    public static String generate(int length) {
        if (length < 0) throw new IllegalArgumentException("length < 0: " + length);
        char[] content = new char[length];
        for (int i = 0; i < length; i++) {
            content[i] = ALPHA_NUM[RANDOM.nextInt(ALPHA_NUM.length)];
        }
        return new String(content);
    }
}
