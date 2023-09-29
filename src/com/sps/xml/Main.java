package com.sps.xml;

import com.sps.xml.annotation.XmlAttribute;
import com.sps.xml.annotation.XmlElement;
import com.sps.xml.annotation.XmlValue;
import com.sps.xml.exception.XmlParseException;
import com.sps.xml.exception.XmlSerializationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@XmlElement(name = "a")
class A {
    @XmlAttribute(name = "age")
    public Integer age;

    static class Interest {
        @XmlValue
        String interest;
    }

    @XmlElement(name = "hobby")
    public Interest[] interests = new Interest[] {new Interest(), new Interest(), new Interest()};
}


public class Main {
    public static void main(String[] args) throws XmlSerializationException, IOException, XmlParseException {
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : Files.readAllLines(Paths.get("test.xml"))) {
            stringBuilder.append(line);
        }

        A a = (new Xml().fromXML(stringBuilder.toString(), A.class));
        System.out.println("Age:");
        System.out.println(a.age);
        System.out.print("Interests (count ");
        System.out.print(a.interests.length);
        System.out.println(")");
        System.out.println(a.interests[0].interest);
        System.out.println(a.interests[1].interest);
        System.out.println(a.interests[2].interest);
        System.out.println((new Xml()).toXML(a));
    }
}

