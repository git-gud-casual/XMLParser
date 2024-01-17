package com.sps.xml.parser;

import com.sps.xml.exception.XmlLexerException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;


public final class XmlLexer {
    private final List<Token> tokens = new LinkedList<>();
    private final String inString;
    private int curTokenType;
    private StringBuffer curVal;

    @Target(value = ElementType.METHOD)
    @Retention(value = RetentionPolicy.RUNTIME)
    private @interface StateHandler {
        int value();
    }
    Map<Integer, Method> typeToHandler;

    public XmlLexer(String inString) {
        this.inString = inString;
        initializeHandlersMap();
    }

    private void initializeHandlersMap() {
        typeToHandler = new HashMap<>();
        for (Method method : getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(StateHandler.class)) {
                typeToHandler.put(
                        method.getAnnotation(StateHandler.class).value(),
                        method);
            }
        }
    }

    public List<Token> scan() throws XmlLexerException {
        tokens.clear();
        curTokenType = Token.CONTENT;
        curVal = new StringBuffer();

        for (char c : inString.toCharArray()) {
            process(c);
        }
        return tokens;
    }

    private void process(char c) throws XmlLexerException {
        try {
            typeToHandler.get(curTokenType).invoke(this, c);
        }
        catch (InvocationTargetException | IllegalAccessException e) {
            Logger.getLogger("XmlLexer").warning(e.toString());
        }
    }

    /* State handlers */
    @StateHandler(Token.CONTENT)
    private void contentHandler(char c) throws XmlLexerException {
        if (c == '>') {
            throw new XmlLexerException("Unexpected >");
        }
        else if (c == '<') {
            if (!curVal.isEmpty()) {
                tokens.add(new Token(curTokenType, curVal.toString().strip()));
                curVal.setLength(0);
            }
            curTokenType = Token.TAG_BEGIN | Token.TAG_CLOSE;
        }
        else if (c != '\n' && c != ' ' || !curVal.isEmpty()) {
            curVal.append(c);
        }
    }

    @StateHandler(Token.TAG_BEGIN | Token.TAG_CLOSE)
    private void defineTagType(char c) {
        if (c == '/') {
            curTokenType = Token.TAG_CLOSE;
        } else {
            curTokenType = Token.TAG_BEGIN;
            curVal.append(c);
        }
    }

    @StateHandler(Token.TAG_BEGIN)
    private void tagBeginHandler(char c) {
        if (c == ' ' || c == '/' || c == '>') {
            if (!curVal.isEmpty()) {
                tokens.add(new Token(curTokenType, curVal.toString()));
                curVal.setLength(0);
            }

            if (c == ' ') {
                curTokenType = Token.ATTRIBUTE_NAME | Token.TAG_END | Token.TAG_END_AND_CLOSE;
            }
            else if (c == '/') {
                curTokenType = Token.TAG_END_AND_CLOSE;
            }
            else {
                tokens.add(new Token(Token.TAG_END, null));
                curTokenType = Token.CONTENT;
            }

        } else {
            curVal.append(c);
        }
    }

    @StateHandler(Token.TAG_CLOSE)
    private void tagCloseHandler(char c) {
        if (c == ' ' || c == '/' || c == '>') {
            if (!curVal.isEmpty()) {
                tokens.add(new Token(curTokenType, curVal.toString()));
                curVal.setLength(0);
            }
            if (c == '>') {
                curTokenType = Token.CONTENT;
            }
        } else {
            curVal.append(c);
        }
    }

    @StateHandler(Token.ATTRIBUTE_NAME | Token.TAG_END | Token.TAG_END_AND_CLOSE)
    private void afterTagNameHandler(char c) {
        if (c == '/') {
            curTokenType = Token.TAG_END_AND_CLOSE;
        }
        else if (c == '>') {
            tokens.add(new Token(Token.TAG_END, null));
            curTokenType = Token.CONTENT;
        }
        else if (c != ' ') {
            curVal.append(c);
            curTokenType = Token.ATTRIBUTE_NAME;
        }
    }

    @StateHandler(Token.ATTRIBUTE_NAME)
    private void attributeNameHandler(char c) {
        if (c == '=') {
            tokens.add(new Token(curTokenType, curVal.toString()));
            curVal.setLength(0);
            curTokenType = Token.EQUAL_SIGN;
        }
        else {
            curVal.append(c);
        }
    }

    @StateHandler(Token.EQUAL_SIGN)
    private void afterSingHandler(char c) {
        if (c == '"') {
            tokens.add(new Token(curTokenType, null));
            curTokenType = Token.ATTRIBUTE_VALUE;
        }
    }

    @StateHandler(Token.ATTRIBUTE_VALUE)
    private void attrValueHandler(char c) {
        if (c == '"') {
            tokens.add(new Token(curTokenType, curVal.toString()));
            curVal.setLength(0);
            curTokenType = Token.ATTRIBUTE_NAME | Token.TAG_END | Token.TAG_END_AND_CLOSE;
        }
        else {
            curVal.append(c);
        }
    }

    @StateHandler(Token.TAG_END_AND_CLOSE)
    private void selfClosedTag(char c) throws XmlLexerException {
        if (c != '>') {
            throw new XmlLexerException("Expected close bracket \">\"");
        }
        else {
            tokens.add(new Token(curTokenType, null));
        }
        curTokenType = Token.CONTENT;
    }
}