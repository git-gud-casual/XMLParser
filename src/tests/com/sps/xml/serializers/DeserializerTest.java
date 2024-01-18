package tests.com.sps.xml.serializers;

import com.sps.xml.Xml;
import com.sps.xml.annotation.XmlElement;
import com.sps.xml.annotation.XmlValue;
import com.sps.xml.parser.XmlLexerException;
import com.sps.xml.parser.XmlParserException;
import com.sps.xml.serializers.XmlSerializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertThrows;

@XmlElement(name = "root")
interface RootInterface {

}

@XmlElement(name = "root")
abstract class RootAbs {

}

@XmlElement(name = "root")
class RootWrong {
    @XmlValue
    Object problemObject;
}


public final class DeserializerTest {
    @Test
    void deserializerTest1() throws XmlLexerException, XmlSerializationException, XmlParserException {
        String inData = """
                <prefix:root  xmlns:prefix="https://test" xmlns:c1="1"  xmlns:c2="2" attr="attrTest">
                    <c1:child>
                        1
                    </c1:child>
                    
                    <c2:child>
                        Number 1
                    </c2:child>
                    <c2:child>
                        Number 2
                    </c2:child>
                    
                    <c1:child>
                        2
                    </c1:child>
                    
                    <child>
                        Extra Node
                    </child>
                </prefix:root>
                """;
        NamespaceRoot outData = new NamespaceRoot();
        outData.attr = "attrTest";
        outData.children1 = new NamespaceRoot.Child1[] {new NamespaceRoot.Child1(1), new NamespaceRoot.Child1(2)};
        outData.children2 = new NamespaceRoot.Child2[] {new NamespaceRoot.Child2("Number 1"), new NamespaceRoot.Child2("Number 2")};

        NamespaceRoot givenData = Xml.fromXML(inData, NamespaceRoot.class);
        Assertions.assertEquals(outData, givenData);
    }

    @Test
    void deserializerTest2() throws XmlLexerException, XmlSerializationException, XmlParserException {
        String inData = """
                <prefix:root  xmlns:prefix="none">
                </prefix:root>
                """;
        NamespaceRoot givenData = Xml.fromXML(inData, NamespaceRoot.class);
        Assertions.assertNull(givenData);
    }

    @Test
    void deserializerTest3() throws XmlLexerException, XmlSerializationException, XmlParserException {
        String inData = """
                <prefix:root  xmlns:prefix="https://test" xmlns:c1="1"  xmlns:c2="2">
                    <child>
                        Extra Node
                    </child>
                </prefix:root>
                """;
        NamespaceRoot outData = new NamespaceRoot();
        outData.children1 = new NamespaceRoot.Child1[0];
        outData.children2 = new NamespaceRoot.Child2[0];

        NamespaceRoot givenData = Xml.fromXML(inData, NamespaceRoot.class);
        Assertions.assertEquals(outData, givenData);
    }

    @Test
    void deserializerTest4() throws XmlLexerException, XmlSerializationException, XmlParserException {
        String inData = """
                <root xmlns:p="test">
                    <p:child value="2">
                        2.2
                    </p:child>
                    <child value="1">
                        1.1
                    </child>
                </root>
                """;
        Root outData = new Root();
        outData.child1 = new Root.Child();
        outData.child2 = new Root.Child();
        outData.child1.attrValue = 1;
        outData.child1.value = 1.1f;
        outData.child2.attrValue = 2;
        outData.child2.value = 2.2f;

        Root givenData = Xml.fromXML(inData, Root.class);
        Assertions.assertEquals(outData, givenData);
    }

    @Test
    void deserializerTest5() {
        assertThrows(XmlSerializationException.class, () ->
                Xml.fromXML("<root attr1=\"attr\"/>", RootInterface.class));
        assertThrows(XmlSerializationException.class, () ->
                Xml.fromXML("<root attr1=\"attr\"/>", RootAbs.class));
        assertThrows(XmlSerializationException.class, () ->
                Xml.fromXML("<root attr1=\"attr\">unknownValueType</root>", RootWrong.class));
    }
}
