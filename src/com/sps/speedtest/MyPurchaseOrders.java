package com.sps.speedtest;

import com.sps.xml.annotation.*;

class Address1  {
    @XmlElement(name = "Name", namespace = "www.contoso.com")
    String name;
    @XmlElement(name = "Street", namespace = "www.contoso.com")
    String street;
    @XmlElement(name = "City", namespace = "www.contoso.com")
    String city;
    @XmlElement(name = "State", namespace = "www.contoso.com")
    String state;
    @XmlElement(name = "Country", namespace = "www.contoso.com")
    String country;
    @XmlElement(name = "Zip", namespace = "www.contoso.com")
    Integer zip;
}

class Item {
    @XmlAttribute(name = "PartNumber")
    String partNumber;

    @XmlElement(name = "ProductName", namespace = "www.contoso.com")
    String name;
    @XmlElement(name = "Quantity", namespace = "www.contoso.com")
    Integer quantity;
    @XmlElement(name = "USPrice", namespace = "www.contoso.com")
    Float price;
}

class Items {
    @XmlElement(name = "Item", namespace = "www.contoso.com")
    Item[] items;
}

class Order {
    @XmlAttribute(name = "PurchaseOrderNumber")
    Integer orderNumber;
    @XmlAttribute(name = "OrderDate")
    String date;
    @XmlElement(name = "Address", namespace = "www.contoso.com")
    Address1[] addresses;
    @XmlElement(name = "DeliveryNotes", namespace = "www.contoso.com")
    String note;

    @XmlElement(name = "Items", namespace = "www.contoso.com")
    Items items;
}

class Address2 {
    @XmlElement(name = "Name", namespace = "http://www.adventure-works.com")
    String name;
    @XmlElement(name = "Street", namespace = "http://www.adventure-works.com")
    String street;
    @XmlElement(name = "City", namespace = "http://www.adventure-works.com")
    String city;
    @XmlElement(name = "State", namespace = "http://www.adventure-works.com")
    String state;
    @XmlElement(name = "Country", namespace = "http://www.adventure-works.com")
    String country;
    @XmlElement(name = "Zip", namespace = "http://www.adventure-works.com")
    Integer zip;
}

class Item1 {
    @XmlAttribute(name = "PartNum")
    String partNum;
    @XmlElement(name = "ProductID", namespace = "http://www.adventure-works.com")
    String id;
    @XmlElement(name = "Qty", namespace = "http://www.adventure-works.com")
    Integer quantity;
    @XmlElement(name = "Price", namespace = "http://www.adventure-works.com")
    Float price;
}

class Order2 {
    @XmlAttribute(name = "PONumber")
    String partNumber;
    @XmlAttribute(name = "Date")
    String date;
    @XmlElement(name = "ShippingAddress", namespace = "http://www.adventure-works.com")
    Address2 shippingAddress;
    @XmlElement(name = "BillingAddress", namespace = "http://www.adventure-works.com")
    Address2 billingAddress;
    @XmlElement(name = "DeliveryInstructions", namespace = "http://www.adventure-works.com")
    String deliveryInstructions;
    @XmlElement(name = "Item", namespace = "http://www.adventure-works.com")
    Item1[] items;
}

@XmlElement(name = "PurchaseOrders", namespace = "www.contoso.com")
public class MyPurchaseOrders {
    @XmlElement(name = "PurchaseOrder", namespace = "www.contoso.com")
    Order[] orders;

    @XmlElement(name = "PurchaseOrder", namespace = "http://www.adventure-works.com")
    Order2 extraOrder;
}
