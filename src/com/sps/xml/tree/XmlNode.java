package com.sps.xml.tree;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class XmlNode {
    private XmlNode parent;
    private String prefix = null;
    private final Map<String, URI> prefixToNamespace = new HashMap<>();

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

    public void addValue(String additionalValue) {
        if (!value.isEmpty()) {
            value += " " + additionalValue;
        }
        else {
            value = additionalValue;
        }
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

    public void addNamespace(String prefix, URI uri) {
        prefixToNamespace.put(prefix, uri);
    }

    public URI getNamespace(String prefix) {
        if (prefixToNamespace.containsKey(prefix)) {
            return prefixToNamespace.get(prefix);
        } else if (parent != null) {
            return parent.getNamespace(prefix);
        } else {
            return null;
        }
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getNameWithPrefix() {
        if (prefix != null) {
            return prefix + ":" + name;
        }
        return name;
    }

    public String toString() {
        return toString(0).trim();
    }

    private String toString(int indent) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ".repeat(indent)).append("<").append(getNameWithPrefix());
        for (Map.Entry<String, URI> entry : prefixToNamespace.entrySet()) {
            builder.append(String.format(" xmlns:%s=\"%s\"", entry.getKey(), entry.getValue()));
        }
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            builder.append(String.format(" %s=\"%s\"", entry.getKey(), entry.getValue()));
        }
        if (!children.isEmpty() || !value.isEmpty()) {
            builder.append(">\n");
            if (!value.isEmpty()) {
                builder.append(" ".repeat(indent + 4));
                builder.append(value);
                builder.append("\n");
            }
            for (XmlNode child : children) {
                builder.append(child.toString(indent + 4));
            }
            builder.append(" ".repeat(indent));
            builder.append(String.format("</%s>\n", getNameWithPrefix()));
        } else {
            builder.append("/>\n");
        }
        return builder.toString();
    }
}