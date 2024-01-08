package com.sps.xml;

import com.sps.xml.exception.XmlLexerException;
import com.sps.xml.exception.XmlParseException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;


record Token(int tokenType, String value) {
    public static final int END = 1,
                            TEXT = 1 << 2,
                            OPENING_BLOCK_TAG_NAME = 1 << 3,
                            CLOSING_BLOCK_TAG_NAME = 1 << 4,
                            ATTRIBUTE_NAME = 1 << 5,
                            ATTRIBUTE_VALUE = 1 << 6,
                            COMMENT = 1 << 7;
}


class XmlLexer implements Iterable<Token> {
    private final List<Token> tokens = new LinkedList<>();

    private final String inS;
    private int curTokenType;
    private StringBuffer curVal;

    public XmlLexer(String inS) {
        this.inS = inS;
    }

    public void scan() throws XmlLexerException {
        curTokenType = Token.TEXT;
        curVal = new StringBuffer();

        for (char c : inS.toCharArray()) {
            process(c);
        }
        curTokenType = Token.END;
    }

    private void process(char c) throws XmlLexerException {
        switch (curTokenType) {
            case Token.END:
                throw new XmlLexerException("Unexpected EOF");
            case Token.TEXT:
                if (c == '>') {
                    throw new XmlLexerException("Unexpected >");
                }
                else if (c == '<') {
                    if (!curVal.isEmpty()) {
                        tokens.add(new Token(curTokenType, String.join(curVal)));
                        curVal.delete(0, curVal.length());
                    }
                    curTokenType = Token.OPENING_BLOCK_TAG_NAME | Token.COMMENT;
                }
                else {
                    curVal.append(c);
                }
                break;
            case Token.OPENING_BLOCK_TAG_NAME | Token.COMMENT:
                break;

        }
    }

    @Override
    public Iterator<Token> iterator() {
        return tokens.iterator();
    }
}

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
