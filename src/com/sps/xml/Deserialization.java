package com.sps.xml;

import com.sps.xml.annotation.XmlAttribute;
import com.sps.xml.annotation.XmlElement;
import com.sps.xml.exception.XmlSerializationException;
import com.sps.xml.tree.XmlNode;
import com.sps.xml.tree.XmlTree;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class Deserialization {
    private static class Visitor<T> implements ObjectNavigator.Visitor<T> {
        private T object;
        private final XmlNode currNode;
        private final List<XmlNode> nodeChildren;

        public Visitor(XmlNode node) {
            currNode = node;
            nodeChildren = currNode.getChildren();
        }

        @Override
        public void start(T obj) {
            object = obj;
        }

        @Override
        public void visitAttribute(Field attribute) throws XmlSerializationException {
            attribute.setAccessible(true);

            try {
                String attrName = attribute.getAnnotation(XmlAttribute.class).name();
                attribute.set(object, strToFieldClass(attribute, currNode.getAttribute(attrName)));
            } catch (IllegalAccessException ignored) {}
        }

        @Override
        public void visitValue(Field value) throws XmlSerializationException {
            value.setAccessible(true);

            try {
                value.set(object, strToFieldClass(value, currNode.getValue()));
            } catch (IllegalAccessException ignored) {}
        }

        @Override
        public void visitXmlElement(Field element) throws XmlSerializationException {
            element.setAccessible(true);

            try {
                String childName = element.getAnnotation(XmlElement.class).name();
                Class<?> fieldClazz = element.getType();

                if (fieldClazz.isArray()) {
                    ArrayList<Object> objects = new ArrayList<>();
                    Iterator<XmlNode> iterator = nodeChildren.iterator();
                    while (iterator.hasNext()) {
                        XmlNode childNode = iterator.next();
                        if (childName.equals(childNode.getName())) {
                            objects.add(getObjectFromNode(fieldClazz.getComponentType(), childNode));
                            iterator.remove();
                        }
                    }
                    element.set(object, objects.toArray((Object[])
                            Array.newInstance(fieldClazz.getComponentType(),
                                    objects.size())));
                }
                else {
                    Iterator<XmlNode> iterator = nodeChildren.iterator();
                    while (iterator.hasNext()) {
                        XmlNode childNode = iterator.next();
                        if (childName.equals(childNode.getName())) {
                            element.set(object, getObjectFromNode(fieldClazz, childNode));
                            iterator.remove();
                            return;
                        }
                    }
                    element.set(object, null);
                }
            } catch (IllegalAccessException ignored) {}
        }

        private <S> S getObjectFromNode(Class<S> clazz, XmlNode node) throws XmlSerializationException {
            Visitor<S> visitor = new Visitor<>(node);
            ObjectNavigator.visitFields(getInstanceOfClass(clazz), visitor);
            return visitor.object;
        }

        private Object strToFieldClass(Field field, String string) throws XmlSerializationException {
            if (string == null || string.isEmpty()) {
                return null;
            }

            Class<?> fieldClazz = field.getType();
            try {
                if (fieldClazz == String.class) {
                    return string;
                } else if (fieldClazz == int.class || fieldClazz == Integer.class) {
                    return Integer.parseInt(string);
                } else if (fieldClazz == float.class || fieldClazz == Float.class) {
                    return Float.parseFloat(string);
                } else if (fieldClazz == double.class || fieldClazz == Double.class) {
                    return Double.parseDouble(string);
                } else if (fieldClazz == boolean.class || fieldClazz == Boolean.class) {
                    return Boolean.parseBoolean(string);
                } else if (fieldClazz == long.class || fieldClazz == Long.class) {
                    return Long.parseLong(string);
                } else if (fieldClazz == char.class || fieldClazz == Character.class) {
                    if (string.length() != 1) {
                        throw new XmlSerializationException("Can`t cast string \"" + string + "\" to char");
                    }
                    return string.charAt(0);
                } else if (fieldClazz == short.class || fieldClazz == Short.class) {
                    return Short.parseShort(string);
                } else if (fieldClazz == byte.class || fieldClazz == Byte.class) {
                    return Byte.parseByte(string);
                }
            } catch (NumberFormatException e) {
                throw new XmlSerializationException(String.format("Can`t cast string %s to %s",
                        string, fieldClazz.getName()));
            }
            throw new XmlSerializationException(String.format("Can`t cast String to %s", fieldClazz.getName()));
        }
    }

    private static <S> S getInstanceOfClass(Class<S> clazz) throws XmlSerializationException {
        try {
            Constructor<S> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new XmlSerializationException(String.format("Class %s should have constructor without arguments",
                    clazz.getName()));
        } catch (IllegalAccessException e) {
            throw new XmlSerializationException(String.format("Can`t get access to constructor of class %s",
                    clazz.getName()));
        } catch (InstantiationException e) {
            throw new XmlSerializationException(String.format("Class %s can not be abstract or interface",
                    clazz.getName()));
        } catch (InvocationTargetException e) {
            throw new XmlSerializationException(String.format("Constructor of class %s threw exception",
                    clazz.getName()));
        }
    }

    public static <T> T deserialization(XmlTree tree, Class<T> clazz) throws XmlSerializationException {
        try {
            if (tree.getRoot() == null || !clazz.getAnnotation(XmlElement.class)
                    .name().equals(tree.getRoot().getName())) {
                return null;
            }
        } catch (NullPointerException e) {
            throw new XmlSerializationException("Root Object should be annotated");
        }

        T object = getInstanceOfClass(clazz);
        ObjectNavigator.visitFields(object, new Visitor<>(tree.getRoot()));
        return object;
    }
}
