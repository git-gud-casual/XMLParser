package com.sps.xml;

import com.sps.xml.exception.XmlParseException;

import java.util.Stack;

final class XmlTreeBuilder {
    public static XmlTree build(String xmlString) throws XmlParseException {
        XmlTree tree = new XmlTree();
        tree.setRoot(parse(xmlString));
        return tree;
    }

    private static XmlTree.XmlNode parse(String xmlString) throws XmlParseException {
        xmlString = trimXmlString(xmlString);
        if (xmlString.isEmpty()) {
            return null;
        }

        XmlTree.XmlNode node;
        Stack<XmlTree.XmlNode> nodeStack = new Stack<>();
        do {
            if (!startsWithTag(xmlString)) {
                if (nodeStack.empty()) {
                    throw new XmlParseException("Expected new tag");
                }
                else if (!xmlString.contains("<")) {
                    throw new XmlParseException("Expected new tag opening bracket");
                }

                int newTagBegin = xmlString.indexOf('<');
                node = nodeStack.peek();
                node.setValue(node.getValue() + xmlString.substring(0, newTagBegin).trim());
                xmlString = xmlString.substring(newTagBegin);
            }
            else {
                if (startsWithOpenTag(xmlString)) {
                    node = new XmlTree.XmlNode();
                    node.setParent(nodeStack.empty() ? null : nodeStack.peek());
                    node.setName(getTagName(xmlString));
                    setTagAttributes(xmlString, node);
                    if (!isCloseTag(xmlString)) {
                        nodeStack.add(node);
                    }
                } else {
                    node = nodeStack.pop();
                    if (!node.getName().equals(getTagName(xmlString))) {
                        throw new XmlParseException("Waiting closing statement");
                    }
                }
                xmlString = xmlString.substring(xmlString.indexOf('>') + 1);
            }
            xmlString = trimXmlString(xmlString);
        } while (!nodeStack.empty() && !xmlString.isEmpty());
        if (!nodeStack.empty()) {
            throw new XmlParseException("Expected closing statement");
        }

        return node;
    }

    private static boolean startsWithTag(String xmlString) {
        return xmlString.startsWith("<");
    }

    private static boolean startsWithOpenTag(String xmlString) {
        return !xmlString.startsWith("</");
    }

    private static boolean isCloseTag(String xmlString) {
        return xmlString.startsWith("</") || (xmlString.indexOf("/>") < xmlString.indexOf(">")
                && xmlString.contains("/>"));
    }

    private static String getTagName(String xmlString) throws XmlParseException {
        int endOfName = -1;
        for (String varOfNameEnd : new String[] {" ", "/>", ">"}) {
            int pos = xmlString.indexOf(varOfNameEnd);
            if (endOfName == -1 || pos != -1 && pos < endOfName) {
                endOfName = pos;
            }
        }

        if (endOfName == -1) {
            throw new XmlParseException("Expected tag name");
        }
        return xmlString.substring(xmlString.startsWith("</") ? 2 : 1, endOfName);
    }

    private static void setTagAttributes(String xmlString, XmlTree.XmlNode node) throws XmlParseException {
        int beginOfAttr = xmlString.indexOf(" ");
        if (beginOfAttr == -1 || beginOfAttr > xmlString.indexOf(">")) {
            return;
        }

        try {
            String[] attr = xmlString.substring(beginOfAttr + 1, xmlString.indexOf(">")).split("=\"");
            for (int i = 0; i < attr.length; i += 2) {
                String name = attr[i].trim();
                String value = attr[i + 1].substring(0, attr[i + 1].indexOf('\"'));
                node.addAttribute(name, value);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new XmlParseException("Error while parsing attributes");
        }
    }

    private static String trimXmlString(String xmlString) throws XmlParseException {
        xmlString = xmlString.strip();

        String closeStatement;
        if (xmlString.startsWith("<?")) {
            closeStatement = "?>";
        }
        else if (xmlString.startsWith("<!--")) {
            closeStatement = "-->";
        }
        else if (xmlString.startsWith("<!")) {
            closeStatement = ">";
        }
        else {
            return xmlString;
        }
        int closeStatementPos = xmlString.indexOf(closeStatement);
        if (closeStatementPos == -1) {
            throw new XmlParseException(String.format("Expected \"%s\" close statement.", closeStatement));
        }
        return trimXmlString(xmlString.substring(closeStatementPos + closeStatement.length()));
    }
}
