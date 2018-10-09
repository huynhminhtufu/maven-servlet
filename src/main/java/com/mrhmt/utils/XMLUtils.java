package com.mrhmt.utils;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.Serializable;

public class XMLUtils implements Serializable {
    public static Document parseFileToDOM(String xmlFilePath) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFilePath);

        return doc;
    }

    public static XPath createXPath() {
        XPathFactory factory = XPathFactory.newInstance();

        return factory.newXPath();
    }
}
