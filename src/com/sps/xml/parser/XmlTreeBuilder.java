package com.sps.xml.parser;

import com.sps.xml.tree.XmlNode;
import com.sps.xml.tree.XmlTree;

import java.net.URI;
import java.util.List;
import java.util.Stack;

public final class XmlTreeBuilder {
    /* Semantically correct tokens */
    private final Token[] tokens;

    public XmlTreeBuilder(Token[] tokens) {
        this.tokens = tokens;
    }

    public XmlTree build() throws XmlParserException {
        XmlTree tree = new XmlTree();
        tree.setRoot(parse());
        return tree;
    }

    private XmlNode parse() throws XmlParserException {
        Stack<XmlNode> nodeStack = new Stack<>();
        XmlNode currNode = null;
        int i = 0;
        while (i < tokens.length) {
            Token token = tokens[i];
            switch (token.tokenType()) {
                case Token.TAG_BEGIN:
                    currNode = new XmlNode();
                    currNode.setParent(nodeStack.empty() ? null : nodeStack.peek());
                    if (token.value().contains(":")) {
                        String[] splitTagName = token.value().split(":");
                        if (splitTagName.length != 2 || splitTagName[0].isEmpty() || splitTagName[1].isEmpty()) {
                            throw new XmlParserException(
                                    String.format("Wrong tag name \"%s\"", token.value())
                            );
                        }
                        currNode.setPrefix(splitTagName[0]);
                        currNode.setName(splitTagName[1]);
                    } else {
                        currNode.setName(token.value());
                    }
                    break;
                case Token.TAG_END:
                    nodeStack.add(currNode);
                case Token.TAG_END_AND_CLOSE:
                    assert currNode != null;
                    if (currNode.getPrefix() != null && currNode.getNamespace(currNode.getPrefix()) == null) {
                        throw new XmlParserException(String.format("Namespace for prefix \"%s\" not found",
                                currNode.getPrefix()));
                    }
                    break;
                case Token.ATTRIBUTE_NAME:
                    assert currNode != null;
                    if (token.value().startsWith("xmlns:")) {
                        String[] splitName = token.value().split(":");
                        if (token.value().charAt(5) != ':'
                                || splitName.length != 2 ||
                                token.value().length() == 6) {
                            throw new XmlParserException(
                                    String.format("Wrong \"%s\"", token.value())
                            );
                        }
                        try {
                            currNode.addNamespace(splitName[1], URI.create(tokens[i + 2].value()));
                        } catch(IllegalArgumentException e) {
                            throw new XmlParserException(
                                    String.format("Namespace should be URI. Given \"%s\"", tokens[i + 2].value())
                            );
                        }
                    }
                    else if (token.value().equals("xmlns")) {
                        try {
                            currNode.setDefaultNamespace(URI.create(tokens[i + 2].value()));
                        } catch(IllegalArgumentException e) {
                            throw new XmlParserException(
                                    String.format("Namespace should be URI. Given \"%s\"", tokens[i + 2].value())
                            );
                        }
                    }
                    else {
                        currNode.addAttribute(token.value(), tokens[i + 2].value());
                    }
                    i += 2;
                    break;
                case Token.PROLOG_BEGIN:
                    do {
                        i++;
                    } while (tokens[i].tokenType() != Token.PROLOG_END);
                    break;
                case Token.TAG_CLOSE:
                    currNode = nodeStack.pop();
                    if (!token.value().equals(currNode.getNameWithPrefix())) {
                        throw new XmlParserException(
                                String.format("Expected close tag \"%s\". Got \"%s\"",
                                        currNode.getNameWithPrefix(),
                                        token.value()));
                    }
                    break;
                case Token.CONTENT:
                    if (!nodeStack.isEmpty()) {
                        nodeStack.peek().addValue(token.value());
                    }
                    break;
                case Token.EQUAL_SIGN:
                case Token.ATTRIBUTE_VALUE:
                case Token.PROLOG_END:
                    throw new XmlParserException(
                            String.format("Unexpected toke type \"%s\"", token.tokenTypeToStr())
                    );
                default:
                    break;
            }

            i++;
        }

        if (!nodeStack.empty()) {
            throw new XmlParserException("XML was not closed");
        }
        return currNode;
    }
}
