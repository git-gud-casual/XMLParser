package tests.com.sps.xml.parser;

import com.sps.xml.parser.Token;
import com.sps.xml.parser.XmlParserException;
import com.sps.xml.parser.XmlTreeBuilder;
import com.sps.xml.tree.XmlTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;

public final class XmlTreeBuilderTest {
    @Test
    void xmlTreeBuilderTest1() throws XmlParserException {
        Token[] inData = {};
        String outData = "";
        test(inData, outData);
    }

    @Test
    void xmlTreeBuilderTest2() throws XmlParserException {
        Token[] inData = {
                new Token(Token.TAG_BEGIN, "a"),
                new Token(Token.ATTRIBUTE_NAME, "xmlns:prefix1"),
                new Token(Token.EQUAL_SIGN, null),
                new Token(Token.ATTRIBUTE_VALUE, "1"),
                new Token(Token.ATTRIBUTE_NAME, "a_attr2"),
                new Token(Token.EQUAL_SIGN, null),
                new Token(Token.ATTRIBUTE_VALUE, "2"),
                new Token(Token.TAG_END, null),
                new Token(Token.CONTENT, "content1"),
                new Token(Token.TAG_BEGIN, "prefix1:b"),
                new Token(Token.ATTRIBUTE_NAME, "b_attr1"),
                new Token(Token.EQUAL_SIGN, null),
                new Token(Token.ATTRIBUTE_VALUE, "b_attr1_value"),
                new Token(Token.TAG_END_AND_CLOSE, null),
                new Token(Token.CONTENT, "content2"),
                new Token(Token.TAG_CLOSE, "a")
        };
        String outData = """
                <a xmlns:prefix1="1" a_attr2="2">
                    content1 content2
                    <prefix1:b b_attr1="b_attr1_value"/>
                </a>""";

        test(inData, outData);

        XmlTree tree = (new XmlTreeBuilder(inData)).build();
        Assertions.assertEquals(tree.getRoot().getNamespace("prefix1"), URI.create("1"));
        Assertions.assertEquals(tree.getRoot().getChildren().get(0).getNamespace("prefix1"),
                URI.create("1"));
    }

    @Test
    void xmlTreeBuilderTest3() {
        Token[] inData = {
                new Token(Token.TAG_BEGIN, "prefix2:a"),
                new Token(Token.ATTRIBUTE_NAME, "xmlns:prefix1"),
                new Token(Token.EQUAL_SIGN, null),
                new Token(Token.ATTRIBUTE_VALUE, "1"),
                new Token(Token.TAG_END, null),
                new Token(Token.TAG_CLOSE, "prefix2:a")
        };
        Assertions.assertThrows(XmlParserException.class, () -> test(inData, ""));
    }

    @Test
    void xmlTreeBuilderTest4() throws XmlParserException {
        Token[] inData = {
                new Token(Token.PROLOG_BEGIN, null),
                new Token(Token.ATTRIBUTE_NAME, "version"),
                new Token(Token.EQUAL_SIGN, null),
                new Token(Token.ATTRIBUTE_VALUE, "1"),
                new Token(Token.PROLOG_END, null),
                new Token(Token.TAG_BEGIN, "p:a"),
                new Token(Token.ATTRIBUTE_NAME, "xmlns:p"),
                new Token(Token.EQUAL_SIGN, null),
                new Token(Token.ATTRIBUTE_VALUE, "1"),
                new Token(Token.TAG_END, null),
                new Token(Token.TAG_BEGIN, "p:b"),
                new Token(Token.ATTRIBUTE_NAME, "xmlns:p"),
                new Token(Token.EQUAL_SIGN, null),
                new Token(Token.ATTRIBUTE_VALUE, "2"),
                new Token(Token.TAG_END_AND_CLOSE, null),
                new Token(Token.COMMENT, "comment example"),
                new Token(Token.TAG_CLOSE, "p:a")
        };
        String outData = """
                <p:a xmlns:p="1">
                    <p:b xmlns:p="2"/>
                </p:a>""";

        test(inData, outData);

        XmlTree tree = (new XmlTreeBuilder(inData)).build();
        Assertions.assertEquals(tree.getRoot().getNamespace("p"), URI.create("1"));
        Assertions.assertEquals(tree.getRoot().getChildren().get(0).getNamespace("p"),
                URI.create("2"));
    }

    @Test
    void xmlTreeBuilderTest5() {
        Token[] inData = {
                new Token(Token.TAG_BEGIN, "p:a"),
                new Token(Token.ATTRIBUTE_NAME, "xmlns:p"),
                new Token(Token.EQUAL_SIGN, null),
                new Token(Token.ATTRIBUTE_VALUE, "1"),
                new Token(Token.TAG_END, null),
                new Token(Token.TAG_CLOSE, "a")
        };
        Assertions.assertThrows(XmlParserException.class, () -> test(inData, ""));
    }

    void test(Token[] inData, String outputData) throws XmlParserException {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder(inData);
        Assertions.assertEquals(outputData, treeBuilder.build().toString());
    }
}
