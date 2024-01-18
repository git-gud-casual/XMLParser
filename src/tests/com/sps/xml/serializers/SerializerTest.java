package tests.com.sps.xml.serializers;

import com.sps.xml.Xml;
import com.sps.xml.annotation.XmlElement;
import com.sps.xml.serializers.XmlSerializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public final class SerializerTest {
    @Test
    void serializerTest1() throws XmlSerializationException {
        NamespaceRoot inData = new NamespaceRoot();
        inData.attr = "string_attribute";
        inData.children1 = new NamespaceRoot.Child1[0];
        String outData = "<prefix0:root xmlns:prefix0=\"https://test\" attr=\"string_attribute\"/>";

        Assertions.assertEquals(outData, Xml.toXML(inData));
    }

    @Test
    void serializerTest2() throws XmlSerializationException {
        NamespaceRoot inData = new NamespaceRoot();
        inData.attr = "string_attribute";
        inData.children1 = new NamespaceRoot.Child1[0];
        String outData = "<p:root xmlns:p=\"https://test\" attr=\"string_attribute\"/>";
        Map<URI, String> nsToPrefix = new HashMap<>();
        nsToPrefix.put(URI.create(NamespaceRoot.class.getAnnotation(XmlElement.class).namespace()),
                "p");

        Assertions.assertEquals(outData, Xml.toXML(inData, nsToPrefix));
    }

    @Test
    void serializerTest3() throws XmlSerializationException, NoSuchFieldException {
        NamespaceRoot inData = new NamespaceRoot();
        inData.attr = "attr";
        inData.children1 = new NamespaceRoot.Child1[] {new NamespaceRoot.Child1(1),
                new NamespaceRoot.Child1(2)};
        inData.children2 = new NamespaceRoot.Child2[] {new NamespaceRoot.Child2("Node 1"),
                new NamespaceRoot.Child2("Node 2")};
        String outData = """
                <rootPrefix:root xmlns:rootPrefix="https://test" xmlns:childPrefix="1" xmlns:prefix0="2" attr="attr">
                    <childPrefix:child xmlns:childPrefix="1">
                        1
                    </childPrefix:child>
                    <childPrefix:child xmlns:childPrefix="1">
                        2
                    </childPrefix:child>
                    <prefix0:child xmlns:prefix0="2">
                        Node 1
                    </prefix0:child>
                    <prefix0:child xmlns:prefix0="2">
                        Node 2
                    </prefix0:child>
                </rootPrefix:root>""";
        Map<URI, String> nsToPrefix = new HashMap<>();
        nsToPrefix.put(URI.create(NamespaceRoot.class.getAnnotation(XmlElement.class).namespace()),
                "rootPrefix");
        nsToPrefix.put(URI.create(NamespaceRoot.class.getDeclaredField(
                "children1").getAnnotation(XmlElement.class).namespace()),
                "childPrefix");

        Assertions.assertEquals(outData, Xml.toXML(inData, nsToPrefix));
    }

    @Test
    void serializerTest4() throws XmlSerializationException {
        Root inData = new Root();
        inData.child1 = new Root.Child();
        inData.child1.value = 1f;
        inData.child1.attrValue = 1;
        inData.child2 = new Root.Child();
        inData.child2.value = 2f;
        inData.child2.attrValue = 2;
        String outData = """
                <root>
                    <child value="1">
                        1.0
                    </child>
                    <prefix0:child xmlns:prefix0="test" value="2">
                        2.0
                    </prefix0:child>
                </root>""";

        Assertions.assertEquals(outData, Xml.toXML(inData));
    }

    @Test
    void serializerTest5() throws XmlSerializationException, NoSuchFieldException {
        Root inData = new Root();
        inData.child1 = new Root.Child();
        inData.child1.value = 1f;
        inData.child1.attrValue = 1;
        inData.child2 = new Root.Child();
        inData.child2.value = 2f;
        inData.child2.attrValue = 2;
        String outData = """
                <root>
                    <child value="1">
                        1.0
                    </child>
                    <childPrefix:child xmlns:childPrefix="test" value="2">
                        2.0
                    </childPrefix:child>
                </root>""";
        Map<URI, String> nsToPrefix = new HashMap<>();
        nsToPrefix.put(URI.create(Root.class.getDeclaredField(
                        "child2").getAnnotation(XmlElement.class).namespace()),
                "childPrefix");
        Assertions.assertEquals(outData, Xml.toXML(inData, nsToPrefix));
    }
}
