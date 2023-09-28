package com.sps.xml;

import com.sps.xml.annotation.XmlElement;
import com.sps.xml.exception.XmlParseException;
import com.sps.xml.parser.XmlTreeBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

@XmlElement(name = "123")
class XmlRoot {
    @XmlElement(name = "341")
    public static class XmlChild {

    }
    private XmlChild child;
}


public class Main {


    public static void main(String[] args) throws NoSuchFieldException, IOException, XmlParseException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        /*
        XmlTreeBuilder builder = new XmlTreeBuilder();

        StringBuilder stringBuilder = new StringBuilder();
        for (String line : Files.readAllLines(Paths.get("test.xml"))) {
            stringBuilder.append(line);
        }

        System.out.println(builder.build(stringBuilder.toString()).toString());*/

        XmlRoot xmlRoot = new XmlRoot();
        Field child = xmlRoot.getClass().getDeclaredFields()[0];
        child.setAccessible(true);
        child.set(xmlRoot, new XmlRoot.XmlChild());
        System.out.println(child.get(xmlRoot));
    }
}