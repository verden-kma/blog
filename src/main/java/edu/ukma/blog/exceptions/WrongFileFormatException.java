package edu.ukma.blog.exceptions;

import java.util.Arrays;

public class WrongFileFormatException extends IllegalArgumentException {
    public WrongFileFormatException(String desirableFormat, final String[] acceptable, String actual) {
        super("format expected: \"" + desirableFormat +
                " acceptable formats: " + Arrays.toString(acceptable) +
                ", format passed: \"" + actual + "\"");
    }
}
