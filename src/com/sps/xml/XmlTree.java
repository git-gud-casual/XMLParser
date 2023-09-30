package com.sps.xml;

import java.util.*;

final class XmlTree {
    public static int STRING_INDENT = 4;

    final static public class XmlNode {
        private XmlNode parent;

        private String name;
        private String value;

        private final List<XmlNode> children;
        private final Map<String, String> attributes;

        public XmlNode(String name, String value, XmlNode parent) {
            this.name = name;
            this.value = value;
            this.parent = parent;

            attributes = new HashMap<>();
            children = new ArrayList<>();
        }

        public XmlNode() {
            this(null, "", null);
        }

        public void setParent(XmlNode parent) {
            if (parent != null) {
                parent.addChild(this);
            }
            this.parent = parent;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public void addAttribute(String name, String value) {
            attributes.put(name, value);
        }

        public String getAttribute(String name) {
            return attributes.get(name);
        }

        public int childrenCount() {
            return children.size();
        }

        public boolean hasChildren() {
            return childrenCount() > 0;
        }

        public void addChild(XmlNode child) {
            children.add(child);
            child.parent = this;
        }

        public List<XmlNode> getChildren() {
            return new ArrayList<>(children);
        }

        public String getTagString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<").append(getName());
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                stringBuilder.append(String.format(" %s=\"%s\"", entry.getKey(), entry.getValue()));
            }
            return stringBuilder.toString();
        }
    }

    private XmlNode root = null;

    public XmlNode getRoot() {
        return root;
    }

    public void setRoot(XmlNode root) {
        this.root = root;
    }

    public String toString() {
        if (root == null) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        class Inner {
            int indent = 0;
            void buildString(XmlNode node) {
                stringBuilder.append(" ".repeat(indent));
                stringBuilder.append(node.getTagString());

                if (!node.hasChildren() && node.value.isEmpty()) {
                    stringBuilder.append("/>");
                }
                else {
                    stringBuilder.append(">\n");
                    indent += STRING_INDENT;
                    if (!node.value.isEmpty()) {
                        stringBuilder.append(" ".repeat(indent));
                        stringBuilder.append(node.value).append('\n');
                    }
                    for (XmlNode child : node.children) {
                        buildString(child);
                        stringBuilder.append('\n');
                    }
                    indent -= STRING_INDENT;
                    stringBuilder.append(" ".repeat(indent));
                    stringBuilder.append(String.format("</%s>", node.getName()));
                }
            }
        }

        new Inner().buildString(root);
        return stringBuilder.toString();
    }
}
