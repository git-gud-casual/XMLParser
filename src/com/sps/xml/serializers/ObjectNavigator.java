package com.sps.xml.serializers;

import com.sps.xml.annotation.XmlAttribute;
import com.sps.xml.annotation.XmlElement;
import com.sps.xml.annotation.XmlValue;

import java.lang.reflect.Field;

public final class ObjectNavigator {
    public interface Visitor<T> {
        void start(T obj);

        void visitAttribute(Field attribute) throws XmlSerializationException;

        void visitValue(Field value) throws XmlSerializationException;

        void visitXmlElement(Field element) throws XmlSerializationException;
    }

    static <T> void visitFields(T obj, Visitor<T> visitor) throws XmlSerializationException {
        visitor.start(obj);
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(XmlElement.class)) {
                visitor.visitXmlElement(field);
            } else if (field.isAnnotationPresent(XmlAttribute.class)) {
                visitor.visitAttribute(field);
            } else if (field.isAnnotationPresent(XmlValue.class)) {
                visitor.visitValue(field);
            }
        }
    }
}
