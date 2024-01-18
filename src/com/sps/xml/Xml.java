package com.sps.xml;

import com.sps.xml.parser.*;
import com.sps.xml.exception.XmlSerializationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

final public class Xml {
    public static  <T> T fromXML(Path filePath, Class<T> clazz) throws IOException, XmlParserException, XmlSerializationException, XmlLexerException {
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : Files.readAllLines(filePath)) {
            stringBuilder.append(line);
        }
        return fromXML(stringBuilder.toString(), clazz);
    }

    public static <T> T fromXML(String xmlString, Class<T> clazz) throws XmlParserException, XmlSerializationException, XmlLexerException {
        XmlLexer lexer = new XmlLexer(xmlString);
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder(lexer.scan().toArray(new Token[0]));
        return Deserialization.deserialization(treeBuilder.build(), clazz);
    }

    public static <T> String toXML(T object) throws XmlSerializationException {
        return Serialization.serialization(object);
    }
}
