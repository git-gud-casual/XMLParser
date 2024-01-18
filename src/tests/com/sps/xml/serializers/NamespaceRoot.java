package tests.com.sps.xml.serializers;

import com.sps.xml.annotation.XmlAttribute;
import com.sps.xml.annotation.XmlElement;
import com.sps.xml.annotation.XmlValue;

import java.util.Arrays;
import java.util.Objects;

@XmlElement(name = "root", namespace = "https://test")
public final class NamespaceRoot {
    @XmlAttribute(name = "attr")
    String attr;

    static class Child1 {
        @XmlValue
        Integer value;

        public Child1() {

        }

        public Child1(Integer value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NamespaceRoot.Child1 child1 = (NamespaceRoot.Child1) o;
            return Objects.equals(value, child1.value);
        }
    }

    static class Child2 {
        @XmlValue
        String value;

        public Child2() {

        }

        public Child2(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NamespaceRoot.Child2 child2 = (NamespaceRoot.Child2) o;
            return Objects.equals(value, child2.value);
        }
    }

    @XmlElement(name = "child", namespace = "1")
    NamespaceRoot.Child1[] children1;

    @XmlElement(name = "child", namespace = "2")
    NamespaceRoot.Child2[] children2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamespaceRoot root = (NamespaceRoot) o;
        return Arrays.equals(children1, root.children1) && Arrays.equals(children2, root.children2)
                && Objects.equals(attr, root.attr);
    }
}