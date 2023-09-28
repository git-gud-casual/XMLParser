package com.sps.xml.serialization;

import com.sps.xml.parser.XmlTree;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Deserialization {
    private static class Visitor implements ObjectNavigator.Visitor {

        @Override
        public void start(Object obj) {

        }

        @Override
        public void visitAttribute(Field attribute) {

        }

        @Override
        public void visitValue(Field value) {

        }

        @Override
        public void visitXmlElement(Field element) {

        }
    }

    public static <T> T deserialization(XmlTree tree, Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return deserialization(tree.getRoot(), clazz);
    }

    private static <T> T deserialization(XmlTree.XmlNode node, Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (node == null) {
            return null;
        }

        T object = clazz.getDeclaredConstructor().newInstance();
        return object;
    }
}
