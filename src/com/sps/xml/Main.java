package com.sps.xml;

import com.sps.xml.annotation.XmlElement;


public class Main {
    @XmlElement(name = "123")
    static class XmlRoot {

    }

    public static void main(String[] args) throws NoSuchFieldException {
        XmlElement annotation = XmlRoot.class.getAnnotation(XmlElement.class);
        System.out.println(annotation);
    }
}