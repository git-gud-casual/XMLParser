package com.sps.xml;

import com.sps.xml.annotation.XmlElement;
import com.sps.xml.exception.XmlParseException;
import com.sps.xml.parser.XmlTreeBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Main {
    @XmlElement(name = "123")
    static class XmlRoot {

    }

    public static void main(String[] args) throws NoSuchFieldException, IOException, XmlParseException {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        StringBuilder stringBuilder = new StringBuilder();
        for (String line : Files.readAllLines(Paths.get("test.xml"))) {
            stringBuilder.append(line);
        }

        System.out.println(builder.build(stringBuilder.toString()).toString());
    }
}