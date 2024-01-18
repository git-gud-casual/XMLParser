package tests.com.sps.xml.serializers;

import com.sps.xml.annotation.XmlElement;
import com.sps.xml.annotation.XmlValue;

import java.util.Objects;

@XmlElement(name = "root", namespace = "test")
public final class RootInNamespace {
    public static class Child {
        @XmlValue
        Integer value;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Child child = (Child) o;
            return Objects.equals(value, child.value);
        }
    }

    @XmlElement(name = "child", namespace = "test")
    Child child;

    @XmlElement(name = "child", namespace = "test2")
    Child child2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RootInNamespace that = (RootInNamespace) o;
        return Objects.equals(child, that.child) && Objects.equals(child2, that.child2);
    }
}
