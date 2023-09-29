package com.sps.xml;

import com.sps.xml.exception.XmlParseException;
import com.sps.xml.exception.XmlSerializationException;
import com.sps.xml.parser.XmlTreeBuilder;
import com.sps.xml.serialization.Deserialization;
import com.sps.xml.serialization.Serialization;

public class Xml {

    public <T> T fromXML(String xmlString, Class<T> clazz) throws XmlParseException, XmlSerializationException {
        return Deserialization.deserialization(XmlTreeBuilder.build(xmlString), clazz);
    }

    public <T> String toXML(T object) throws XmlSerializationException {
        return Serialization.serialization(object);
    }
}
