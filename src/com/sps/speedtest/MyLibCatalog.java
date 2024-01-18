package com.sps.speedtest;

import com.sps.xml.annotation.XmlAttribute;
import com.sps.xml.annotation.XmlElement;

class Book {
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

@XmlElement(name = "catalog")
class MyLibCatalog {
    @XmlElement(name = "book")
    Book[] books;
}