package com.sps.xml.tree;

public final class XmlTree {
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
        return root.toString();
    }
}
