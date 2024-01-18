package com.sps.xml;

import com.sps.xml.annotation.XmlAttribute;
import com.sps.xml.annotation.XmlElement;
import com.sps.xml.exception.XmlSerializationException;
import com.sps.xml.tree.XmlNode;
import com.sps.xml.tree.XmlTree;

import java.lang.reflect.Field;

final class Serialization {
    private static class Visitor<T> implements ObjectNavigator.Visitor<T> {
        private T object;
        private XmlNode node;

        @Override
        public void start(T obj) {
            object = obj;
            node = new XmlNode();
        }

        @Override
        public void visitAttribute(Field attribute) {
            attribute.setAccessible(true);

            try {
                String attrName = attribute.getAnnotation(XmlAttribute.class).name();
                Object value = attribute.get(object);
                if (value != null) {
                    node.addAttribute(attrName, String.valueOf(value));
                }
            } catch (IllegalAccessException ignored) {}
        }

        @Override
        public void visitValue(Field value) {
            value.setAccessible(true);

            try {
                Object valueString = value.get(object);
                if (valueString != null) {
                    node.setValue(String.valueOf(valueString));
                }
            } catch (IllegalAccessException ignored) {}
        }

        @Override
        public void visitXmlElement(Field element) throws XmlSerializationException {
            element.setAccessible(true);

            try {
                Object childObject = element.get(object);
                if (childObject != null) {
                    String childName = element.getAnnotation(XmlElement.class).name();
                    if (childObject.getClass().isArray()) {
                        for (Object obj : (Object[]) childObject) {
                            node.addChild(getChildNode(obj, childName));
                        }
                    }
                    else {
                        node.addChild(getChildNode(childObject, childName));
                    }
                }
            } catch (IllegalAccessException ignored) {}
        }

        private XmlNode getChildNode(Object childObject, String childName) throws XmlSerializationException {
            Visitor<Object> childVisitor = new Visitor<>();
            ObjectNavigator.visitFields(childObject, childVisitor);
            childVisitor.node.setName(childName);
            return childVisitor.node;
        }
    }

    public static <T> String serialization(T object) throws XmlSerializationException {
        XmlTree tree = new XmlTree();
        tree.setRoot(objToNode(object));
        return tree.toString();
    }

    private static <T> XmlNode objToNode(T object) throws XmlSerializationException {
        try {
            Visitor<T> visitor = new Visitor<>();
            ObjectNavigator.visitFields(object, visitor);
            visitor.node.setName(object.getClass().getAnnotation(XmlElement.class).name());
            return visitor.node;
        } catch (NullPointerException e) {
            throw new XmlSerializationException("Root object should be annotated with @XmlElement");
        }
    }
}
