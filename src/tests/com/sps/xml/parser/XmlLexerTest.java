package tests.com.sps.xml.parser;

import static org.junit.jupiter.api.Assertions.*;

import com.sps.xml.exception.XmlLexerException;
import com.sps.xml.parser.Token;
import com.sps.xml.parser.XmlLexer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class XmlLexerTest {
    @Test
    void xmlLexerTest1() throws XmlLexerException {
        String inputData = """
                """;
        Token[] outputData = {};
        test(inputData, outputData);
    }

    @Test
    void xmlLexerTest2() throws XmlLexerException {
        String inputData = """
                <?xml version="1.0"?>
                <root     />
                """;
        Token[] outputData = {
                new Token(Token.PROLOG_BEGIN, null),
                new Token(Token.ATTRIBUTE_NAME, "version"),
                new Token(Token.EQUAL_SIGN, null),
                new Token(Token.ATTRIBUTE_VALUE, "1.0"),
                new Token(Token.PROLOG_END, null),
                new Token(Token.TAG_BEGIN, "root"),
                new Token(Token.TAG_END_AND_CLOSE, null)
        };
        test(inputData, outputData);
    }

    @Test
    void xmlLexerTest3() throws XmlLexerException {
        String inputData = """
                <a a_attr1="1" a_attr2="2">
                    content1
                    <b b_attr1="b_attr1_value"/>
                    content2
                </a>
                """;
        Token[] outputData = {
                new Token(Token.TAG_BEGIN, "a"),
                new Token(Token.ATTRIBUTE_NAME, "a_attr1"),
                new Token(Token.EQUAL_SIGN, null),
                new Token(Token.ATTRIBUTE_VALUE, "1"),
                new Token(Token.ATTRIBUTE_NAME, "a_attr2"),
                new Token(Token.EQUAL_SIGN, null),
                new Token(Token.ATTRIBUTE_VALUE, "2"),
                new Token(Token.TAG_END, null),
                new Token(Token.CONTENT, "content1"),
                new Token(Token.TAG_BEGIN, "b"),
                new Token(Token.ATTRIBUTE_NAME, "b_attr1"),
                new Token(Token.EQUAL_SIGN, null),
                new Token(Token.ATTRIBUTE_VALUE, "b_attr1_value"),
                new Token(Token.TAG_END_AND_CLOSE, null),
                new Token(Token.CONTENT, "content2"),
                new Token(Token.TAG_CLOSE, "a")
        };
        test(inputData, outputData);
    }

    @Test
    void xmlLexerTest4() throws XmlLexerException {
        String inData = """
                <root>
                <!-- comment test -->
                </root>
                """;
        Token[] outputData = {
                new Token(Token.TAG_BEGIN, "root"),
                new Token(Token.TAG_END, null),
                new Token(Token.COMMENT, " comment test "),
                new Token(Token.TAG_CLOSE, "root")
        };
        test(inData, outputData);
    }

    @Test
    void xmlLexerTest5() {
        String inData = """
                <root>
                <!-- comment -- test -->
                </root>
                """;
        Assertions.assertThrows(XmlLexerException.class, () -> test(inData, new Token[]{}));
    }

    static void test(String inputData, Token[] outputData) throws XmlLexerException {
        XmlLexer xmlLexer = new XmlLexer(inputData);
        assertArrayEquals(outputData, xmlLexer.scan().toArray());
    }
}
