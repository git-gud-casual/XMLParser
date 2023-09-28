package com.sps.xml.serialization;

import com.sps.xml.annotation.XmlAttribute;
import com.sps.xml.annotation.XmlElement;
import com.sps.xml.annotation.XmlValue;

import java.lang.reflect.Field;

final class ObjectNavigator {
    public interface Visitor {
        void start(Object obj);

        void visitAttribute(Field attribute);

        void visitValue(Field value);

        void visitXmlElement(Field element);
    }

    static <T> void visitFields(T obj, Visitor visitor) {
        visitor.start(obj);

        for (Field field : obj.getClass().getFields()) {
            Class<?> fieldClazz = field.getDeclaringClass();
            if (fieldClazz.isAnnotationPresent(XmlElement.class)) {
                visitor.visitXmlElement(field);
            }
            else if(fieldClazz.isAnnotationPresent(XmlAttribute.class)) {
                visitor.visitAttribute(field);
            }
            else if (fieldClazz.isAnnotationPresent(XmlValue.class)) {
                visitor.visitXmlElement(field);
            }
        }
    }
}
