package com.sps.xml.parser;

public record Token(int tokenType, String value) {
    public static final int TAG_BEGIN = 1,
            TAG_END = 1 << 2,
            TAG_CLOSE = 1 << 3,
            TAG_END_AND_CLOSE = 1 << 4,
            ATTRIBUTE_NAME = 1 << 5,
            EQUAL_SIGN = 1 << 6,
            ATTRIBUTE_VALUE = 1 << 7,
            CONTENT = 1 << 8;

    private static String tokenTypeToStr(int tokenType) {
        return switch (tokenType) {
            case TAG_BEGIN -> "TAG_BEGIN";
            case TAG_END -> "TAG_END";
            case TAG_CLOSE -> "TAG_CLOSE";
            case TAG_END_AND_CLOSE -> "TAG_END_AND_CLOSE";
            case ATTRIBUTE_NAME -> "ATTRIBUTE_NAME";
            case EQUAL_SIGN -> "EQUAL_SIGN";
            case ATTRIBUTE_VALUE -> "ATTRIBUTE_VALUE";
            case CONTENT -> "CONTENT";
            default -> "NOT_DEFINED_TOKEN";
        };
    }

    public String toString() {
        return String.format("%s: %s", tokenTypeToStr(tokenType), value);
    }
}