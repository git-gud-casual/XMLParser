package com.sps.xml.parser;

import com.sps.xml.exception.XmlParseException;

import java.util.Stack;

public class XmlTreeBuilder {
    public XmlTree build(String xmlString) throws XmlParseException {
        XmlTree tree = new XmlTree();
        tree.setRoot(parse(xmlString));
        return tree;
    }

    private XmlTree.XmlNode parse(String xmlString) throws XmlParseException {
        //TODO: Rewrite this shit
        xmlString = trimXmlString(xmlString);
        if (xmlString.isEmpty()) {
            return null;
        }

        XmlTree.XmlNode node = new XmlTree.XmlNode();
        Stack<XmlTree.XmlNode> nodeStack = new Stack<>();
        do {
            System.out.println(xmlString);

            if (!isTag(xmlString)) {
                if (nodeStack.empty()) {
                    throw new XmlParseException("Unexpected closing statement");
                }
                else if (!xmlString.contains("<")) {
                    throw new XmlParseException("Expected new tag opening bracket");
                }

                int newTagBegin = xmlString.indexOf('<');
                node = nodeStack.peek();
                node.setValue(node.getValue() + xmlString.substring(0, newTagBegin).trim());
                xmlString = xmlString.substring(newTagBegin);
            }
            else if (isOpenTag(xmlString)) {
                node = new XmlTree.XmlNode();
                node.setParent(nodeStack.empty() ? null : nodeStack.peek());
                node.setName(getTagName(xmlString));
                setTagAttributes(xmlString, node);
                if (!isCloseTag(xmlString)) {
                    nodeStack.add(node);
                }
                xmlString = xmlString.substring(xmlString.indexOf('>') + 1);
            }
            else {
                node = nodeStack.pop();
                if (!node.getName().equals(getTagName(xmlString))) {
                    throw new XmlParseException("Waiting closing statement");
                }
                xmlString = xmlString.substring(xmlString.indexOf(">") + 1);
            }
            xmlString = trimXmlString(xmlString);
        } while (!nodeStack.empty() && !xmlString.isEmpty());
        if (!nodeStack.empty()) {
            throw new XmlParseException("Unexpected EOF");
        }

        return node;
    }

    private boolean isTag(String xmlString) {
        return xmlString.startsWith("<");
    }

    private boolean isOpenTag(String xmlString) {
        return !xmlString.startsWith("</");
    }

    private boolean isCloseTag(String xmlString) {
        return xmlString.startsWith("</") || (xmlString.indexOf("/>") < xmlString.indexOf(">")
                && xmlString.contains("/>"));
    }

    private String getTagName(String xmlString) throws XmlParseException {
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

    private void setTagAttributes(String xmlString, XmlTree.XmlNode node) throws XmlParseException {
        try {
            for (String attr : xmlString.substring(0, xmlString.indexOf(">")).split(" ")) {
                if (attr.contains("=")) {
                    String[] nameAndValue = attr.split("=");
                    if (!(nameAndValue[1].startsWith("\"") && nameAndValue[1].endsWith("\""))) {
                        throw new XmlParseException("Value should be in \"\"");
                    }
                    node.addAttribute(nameAndValue[0], nameAndValue[1].substring(1,
                            nameAndValue[1].length() - 1));
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new XmlParseException("Error while parsing attributes");
        }
    }

    private String trimXmlString(String xmlString) throws XmlParseException {
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
