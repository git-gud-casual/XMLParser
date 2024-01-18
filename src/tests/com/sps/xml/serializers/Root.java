package tests.com.sps.xml.serializers;

import com.sps.xml.annotation.XmlAttribute;
import com.sps.xml.annotation.XmlElement;
import com.sps.xml.annotation.XmlValue;

import java.util.Objects;

@XmlElement(name = "root")
class Root {
    static class Child {
        @XmlValue
        Float value;

        @XmlAttribute(name = "value")
        Integer attrValue;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Child child = (Child) o;
            return Objects.equals(value, child.value) && Objects.equals(attrValue, child.attrValue);
        }
    }

    @XmlElement(name = "child")
    Child child1;

    @XmlElement(name = "child", namespace = "test")
    Child child2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Root root = (Root) o;
        return Objects.equals(child1, root.child1) && Objects.equals(child2, root.child2);
    }
}
