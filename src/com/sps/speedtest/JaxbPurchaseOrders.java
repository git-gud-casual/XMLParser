package com.sps.speedtest;

import javax.xml.bind.annotation.*;


class JaxbItem {
    @XmlAttribute(name = "PartNumber")
    String partNumber;

    @XmlElement(name = "ProductName", namespace = "www.contoso.com")
    String name;
    @XmlElement(name = "Quantity", namespace = "www.contoso.com")
    Integer quantity;
    @XmlElement(name = "USPrice", namespace = "www.contoso.com")
    Float price;
}

class JaxbAddress {
    @XmlAttribute(name = "Type")
    String deliveryType;

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

class JaxbPurchaseOrder {
    @XmlAttribute(name = "PurchaseOrderNumber")
    Integer orderNumber;
    @XmlAttribute(name = "OrderDate")
    String date;
    @XmlElement(name = "Address", namespace = "www.contoso.com")
    JaxbAddress[] addresses;
    @XmlElement(name = "DeliveryNotes", namespace = "www.contoso.com")
    String note;

    @XmlElementWrapper(name = "Items", namespace = "www.contoso.com")
    @XmlElement(name = "Item", namespace = "www.contoso.com")
    JaxbItem[] items;
}

class JaxbAddress1 {
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

class JaxbItem1 {
    @XmlAttribute(name = "PartNum")
    String partNum;
    @XmlElement(name = "ProductID", namespace = "http://www.adventure-works.com")
    String id;
    @XmlElement(name = "Qty", namespace = "http://www.adventure-works.com")
    Integer quantity;
    @XmlElement(name = "Price", namespace = "http://www.adventure-works.com")
    Float price;
}

class JaxbOrder1 {
    @XmlAttribute(name = "PONumber")
    String partNumber;
    @XmlAttribute(name = "Date")
    String date;
    @XmlElement(name = "ShippingAddress", namespace = "http://www.adventure-works.com")
    JaxbAddress1 shippingAddress;
    @XmlElement(name = "BillingAddress", namespace = "http://www.adventure-works.com")
    JaxbAddress1 billingAddress;
    @XmlElement(name = "DeliveryInstructions", namespace = "http://www.adventure-works.com")
    String deliveryInstructions;
    @XmlElement(name = "Item", namespace = "http://www.adventure-works.com")
    JaxbItem1[] items;
}


@XmlRootElement(name = "PurchaseOrders", namespace = "www.contoso.com")
public class JaxbPurchaseOrders {
    @XmlElement(name = "PurchaseOrder", namespace = "www.contoso.com")
    JaxbPurchaseOrder[] purchaseOrders;

    @XmlElement(name = "PurchaseOrder", namespace = "http://www.adventure-works.com")
    JaxbOrder1 extraOrder;
}
