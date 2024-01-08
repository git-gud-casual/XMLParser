package com.sps.tests;

import static org.junit.jupiter.api.Assertions.*;

import com.sps.xml.annotation.*;
import com.sps.xml.exception.*;
import com.sps.xml.Xml;
import org.junit.jupiter.api.Test;

class XmlTest {
    @XmlElement(name = "root")
    static class Root {
        static class AnotherChild {
            @XmlValue
            double value;
        }
        @XmlElement(name = "child-root")
        static class Child {
            @XmlValue
            Integer value;

            @XmlElement(name = "child")
            AnotherChild child;
        }

        @XmlAttribute(name = "attr1")
        String attr1;

        @XmlElement(name = "child")
        Child[] children;

        @XmlElement(name = "another-child")
        Child child;
    }

    @XmlElement(name = "root")
    interface TestInterface {}
    @XmlElement(name = "root")
    abstract static class TestAbstract {}

    @XmlElement(name = "root")
    static class TestClass {
        @XmlValue
        Object unknownThing;
    }

    @Test
    void testDeserialization() throws XmlParseException, XmlSerializationException {
        Root root = Xml.fromXML("<root attr1=\"test attr1\"/>", Root.class);
        assertEquals("test attr1", root.attr1);
        assertNull(root.child);
        assertEquals(0, root.children.length);
        System.out.println("Passed test 1");
    }

    @Test
    void testDeserializationAndSerialization() throws XmlParseException, XmlSerializationException {
        Root root = Xml.fromXML("""
                                        <root><child>0</child>
                                        <child>1</child>
                                        <!-- comment -->
                                        <another-child/></root>""", Root.class);
        assertNull(root.attr1);
        assertNotNull(root.child);
        assertNull(root.child.value);
        assertEquals(2, root.children.length);
        for (int i = 0; i < root.children.length; i++) {
            assertEquals(i, root.children[i].value);
        }

        assertEquals("""
                <child-root>
                    1
                </child-root>""", Xml.toXML(root.children[1]));
        System.out.println("Passed test 2");
    }

    @Test
    void testSerializationAndDeserialization() throws XmlSerializationException, XmlParseException {
        Root root = new Root();
        root.attr1 = "this is attribute";
        root.child = new Root.Child();
        root.child.value = 111;
        root.child.child = new Root.AnotherChild();
        root.child.child.value = 1.5;
        assertEquals("""
                <root attr1="this is attribute">
                    <another-child>
                        111
                        <child>
                            1.5
                        </child>
                    </another-child>
                </root>""", Xml.toXML(root));
        Root cloneRoot = Xml.fromXML(Xml.toXML(root), Root.class);
        assertEquals(root.attr1, cloneRoot.attr1);
        assertNotNull(cloneRoot.child);
        assertEquals(root.child.value, cloneRoot.child.value);
        assertNotNull(cloneRoot.child.child);
        assertTrue(Math.abs(root.child.child.value - cloneRoot.child.child.value) < Math.pow(10, -16));
        System.out.println("Passed test 3");
    }

    @Test
    void testParseException() {
        assertThrows(XmlParseException.class, () -> Xml.fromXML("<root attr1=\"attr\">", Root.class));
        System.out.println("Passed test 4");
    }

    @Test
    void testSerializationException() {
        assertThrows(XmlSerializationException.class, () ->
                Xml.fromXML("<root attr1=\"attr\"/>", TestInterface.class));
        assertThrows(XmlSerializationException.class, () ->
                Xml.fromXML("<root attr1=\"attr\"/>", TestAbstract.class));
        assertThrows(XmlSerializationException.class, () ->
                Xml.fromXML("<root attr1=\"attr\">unknownValueType</root>", TestClass.class));
        System.out.println("Passed test 5");
    }
}