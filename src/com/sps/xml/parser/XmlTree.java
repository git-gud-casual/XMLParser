package com.sps.xml.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlTree {
    static class XmlNode {
        private XmlNode parent;

        private String name;
        private String value;

        private List<XmlNode> children;
        private Map<String, String> attributes;

        public XmlNode(String name, String value) {
            this.name = name;
            this.value = value;

            attributes = new HashMap<>();
            children = new ArrayList<>();
        }

        public XmlNode() {
            this(null, null);
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

        public void addChild(XmlNode child) {
            children.add(child);
        }
    }

    XmlNode root = null;

    public XmlNode getRoot() {
        return root;
    }
}
