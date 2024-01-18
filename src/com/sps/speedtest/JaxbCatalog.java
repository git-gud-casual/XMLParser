package com.sps.speedtest;


import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


class JaxbBook {
    @XmlAttribute(name = "id")
    String bookId;

    @XmlElement(name = "author")
    String author;
    @XmlElement(name = "title")
    String title;
    @XmlElement(name = "genre")
    String genre;
    @XmlElement(name = "price")
    Float price;
    @XmlElement(name = "publish_date")
    String publishDate;
    @XmlElement(name = "description")
    String desc;
}

@XmlRootElement(name = "catalog")
public class JaxbCatalog {
    @XmlElement(name = "book")
    JaxbBook[] books;
}
