package com.sps.xml.serializers;

import com.sps.xml.annotation.XmlAttribute;
import com.sps.xml.annotation.XmlElement;
import com.sps.xml.tree.XmlNode;
import com.sps.xml.tree.XmlTree;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public final class Serializer {
    private class Visitor<T> implements ObjectNavigator.Visitor<T> {
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
                    XmlElement anno = element.getAnnotation(XmlElement.class);
                    if (childObject.getClass().isArray()) {
                        if (((Object[]) childObject).length > 0) {
                            URI ns = URI.create(anno.namespace());
                            node.addNamespace(getPrefixByNamespace(ns), ns);
                            for (Object obj : (Object[]) childObject) {
                                node.addChild(getChildNode(obj, anno));
                            }
                        }
                    }
                    else {
                        node.addChild(getChildNode(childObject, anno));
                    }
                }
            } catch (IllegalAccessException ignored) {}
        }

        private XmlNode getChildNode(Object childObject, XmlElement anno) throws XmlSerializationException {
            Visitor<Object> childVisitor = new Visitor<>();
            ObjectNavigator.visitFields(childObject, childVisitor);
            childVisitor.node.setName(anno.name());
            if (!anno.namespace().isEmpty()) {
                URI ns = URI.create(anno.namespace());
                childVisitor.node.setPrefix(getPrefixByNamespace(ns));

                if (childVisitor.node.getNamespace(childVisitor.node.getPrefix()) == null) {
                    childVisitor.node.addNamespace(getPrefixByNamespace(ns), ns);
                }
            }
            return childVisitor.node;
        }
    }

    public <T> String serialize(T object) throws XmlSerializationException {
        XmlTree tree = new XmlTree();
        tree.setRoot(objToNode(object));
        return tree.toString();
    }

    private <T> XmlNode objToNode(T object) throws XmlSerializationException {
        try {
            Visitor<T> visitor = new Visitor<>();
            ObjectNavigator.visitFields(object, visitor);
            XmlElement anno = object.getClass().getAnnotation(XmlElement.class);
            visitor.node.setName(anno.name());
            if (!anno.namespace().isEmpty()) {
                URI ns = URI.create(anno.namespace());
                visitor.node.addNamespace(getPrefixByNamespace(ns), ns);
                visitor.node.setPrefix(getPrefixByNamespace(ns));
            }
            return visitor.node;
        } catch (NullPointerException e) {
            throw new XmlSerializationException("Root object should be annotated with @XmlElement");
        }
    }


    private final Map<URI, String> nsToPrefix;
    private int prefixCount = 0;
    private String getPrefixByNamespace(URI namespace) {
        if (nsToPrefix.containsKey(namespace)) {
            return nsToPrefix.get(namespace);
        }
        String prefix = String.format("prefix%d", prefixCount);
        prefixCount += 1;
        nsToPrefix.put(namespace, prefix);
        return prefix;
    }

    public Serializer() {
        nsToPrefix = new HashMap<>();
    }

    public Serializer(Map<URI, String> namespaceToPrefix) {
        nsToPrefix = namespaceToPrefix;
    }
}
