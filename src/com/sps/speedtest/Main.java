package com.sps.speedtest;

import com.sps.xml.Xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.nio.file.Path;


class Test1 extends SpeedTest {
    MyLibCatalog myCatalog;
    JaxbCatalog jaxbCatalog;

    public Test1(String testName) {
        super(testName);
    }

    @Override
    void myLibDeserializationTest() {
        try {
            myCatalog = Xml.fromXML(Path.of("resources/books.xml"), MyLibCatalog.class);
        } catch (Exception ignored){}
    }

    @Override
    void anotherLibDeserializationTest() {
        try {
            JAXBContext context = JAXBContext.newInstance(JaxbCatalog.class);
            jaxbCatalog = (JaxbCatalog) context.createUnmarshaller().
                    unmarshal(new FileReader("resources/books.xml"));
        } catch (Exception ignored){}
    }

    @Override
    void myLibSerializationTest() {
        try(FileWriter fileWriter = new FileWriter("resources/MyBooks.xml"))  {
            String xml = Xml.toXML(myCatalog);
            fileWriter.write(xml);
        } catch (Exception ignored){}
    }

    @Override
    void anotherLibSerializationTest() {
        try {
            JAXBContext context = JAXBContext.newInstance(JaxbCatalog.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(jaxbCatalog,
                    new FileWriter("resources/JaxbBooks.xml"));
        } catch (Exception ignored){}
    }
}

class Test2 extends SpeedTest {
    MyPurchaseOrders myOrders;
    JaxbPurchaseOrders jaxbOrders;

    public Test2(String testName) {
        super(testName);
    }

    @Override
    void myLibDeserializationTest() {
        try {
            myOrders = Xml.fromXML(Path.of("resources/orders.xml"), MyPurchaseOrders.class);
        } catch (Exception ignored){}
    }

    @Override
    void anotherLibDeserializationTest() {
        try {
            JAXBContext context = JAXBContext.newInstance(JaxbPurchaseOrders.class);
            jaxbOrders = (JaxbPurchaseOrders) context.createUnmarshaller().
                    unmarshal(new FileReader("resources/orders.xml"));
        } catch (Exception ignored){}
    }

    @Override
    void myLibSerializationTest() {
        try(FileWriter fileWriter = new FileWriter("resources/MyOrders.xml"))  {
            String xml = Xml.toXML(myOrders);
            fileWriter.write(xml);
        } catch (Exception ignored){}
    }

    @Override
    void anotherLibSerializationTest() {
        try {
            JAXBContext context = JAXBContext.newInstance(JaxbPurchaseOrders.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(jaxbOrders,
                    new FileWriter("resources/JaxbOrders.xml"));
        } catch (Exception ignored){}
    }
}

public class Main {
    public static void main(String[] args) {
        (new Test1("books.xml")).test();
        (new Test2("orders.xml")).test();
    }
}
