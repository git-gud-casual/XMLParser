package com.sps.xml.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlTree {
    static public class XmlNode {
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

        public XmlNode getParent() {
            return parent;
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

        public XmlNode getChild(int index) {
            return  children.get(index);
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
                stringBuilder.append("<");
                stringBuilder.append(node.name);
                for (Map.Entry<String, String> entry : node.attributes.entrySet()) {
                    stringBuilder.append(String.format(" %s=\"%s\"", entry.getKey(), entry.getValue()));
                }

                if (!node.hasChildren() && node.value.isEmpty()) {
                    stringBuilder.append("/>");
                }
                else {
                    stringBuilder.append(">\n");
                    indent += 2;
                    stringBuilder.append(" ".repeat(indent));
                    stringBuilder.append(node.value);
                    stringBuilder.append('\n');
                    for (XmlNode child : node.children) {
                        buildString(child);
                        stringBuilder.append('\n');
                    }
                    indent -= 2;
                    stringBuilder.append(" ".repeat(indent));
                    stringBuilder.append("</");
                    stringBuilder.append(node.name);
                    stringBuilder.append(">");
                }
            }
        }

        new Inner().buildString(root);
        return stringBuilder.toString();
    }
}
