package com.sps.xml.parser;

import java.util.Stack;

public class XmlTreeBuilder {

    XmlTree build(String xmlString) throws Exception {
        XmlTree tree = new XmlTree();
        tree.root = parse(xmlString);
        return tree;
    }

    private XmlTree.XmlNode parse(String xmlString) throws Exception {
        XmlTree.XmlNode root = new XmlTree.XmlNode(), node = root;

        Stack<XmlTree.XmlNode> nodeStack = new Stack<>();
        nodeStack.push(root);

        int pos = 0;
        while (!nodeStack.empty()) {
            xmlString = xmlString.strip();

            if (xmlString.startsWith("</" + nodeStack.peek().getName() + ">")) {
                nodeStack.pop();
            }


        }

        return root;
    }
}
