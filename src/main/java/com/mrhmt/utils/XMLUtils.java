package com.mrhmt.utils;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
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

    public static void parseFileToSAX(String xmlFile, DefaultHandler handler) throws SAXException, ParserConfigurationException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        parser.parse(xmlFile, handler);
    }
}
