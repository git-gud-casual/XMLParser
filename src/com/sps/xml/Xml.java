package com.sps.xml;

import com.sps.xml.exception.XmlParseException;
import com.sps.xml.exception.XmlSerializationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Xml {
    public static  <T> T fromXML(Path filePath, Class<T> clazz) throws IOException, XmlParseException, XmlSerializationException {
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : Files.readAllLines(filePath)) {
            stringBuilder.append(line);
        }
        return fromXML(stringBuilder.toString(), clazz);
    }

    public static <T> T fromXML(String xmlString, Class<T> clazz) throws XmlParseException, XmlSerializationException {
        return Deserialization.deserialization(XmlTreeBuilder.build(xmlString), clazz);
    }

    public static <T> String toXML(T object) throws XmlSerializationException {
        return Serialization.serialization(object);
    }
}
