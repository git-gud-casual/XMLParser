package com.sps.xml.serialization;

import com.sps.xml.parser.XmlTree;

import java.lang.reflect.Field;

public class Serialization {
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

    public static <T> String serialization(T object) {
        XmlTree tree = new XmlTree();
        tree.setRoot(objToNode(object));
        return tree.toString();
    }

    private static <T> XmlTree.XmlNode objToNode(T object) {
        XmlTree.XmlNode node = new XmlTree.XmlNode();
        return node;
    }
}
