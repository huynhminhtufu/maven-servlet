package com.mrhmt.utils;

import com.mrhmt.dto.StudentDTO;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.parsers.*;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static XMLStreamReader parseFileToStAXCursor(InputStream is) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(is);

        return reader;
    }

    public static String getNodeStAXValue(XMLStreamReader reader, String elementName,
                                          String namespaceURI, String attrName) throws  XMLStreamException {
        if (reader != null) {
            while (reader.hasNext()) {
                int cursor = reader.getEventType();

                if (cursor == XMLStreamConstants.START_ELEMENT) {
                    String tagName = reader.getLocalName();

                    if (tagName.equals(elementName)) {
                        String result = reader.getAttributeValue(namespaceURI, attrName);

                        return result;
                    }
                }

                reader.next();
            }
        }

        return null;
    }

    public static String getTextStAXContext(XMLStreamReader reader, String elementName) throws XMLStreamException {
        if (reader != null) {
            while (reader.hasNext()) {
                int cursor = reader.getEventType();

                if (cursor == XMLStreamConstants.START_ELEMENT) {
                    String tagName = reader.getLocalName();

                    if (tagName.equals(elementName)) {
                        reader.next();

                        String result = reader.getText();
                        reader.nextTag();

                        return result;
                    }
                }

                reader.next();
            }
        }

        return null;
    }

    public static XMLEventReader parseFileToStAXIterator(InputStream is) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader reader = factory.createXMLEventReader(is);

        return reader;
    }

    public static String getNodeStAXValue(XMLEventReader reader, String elementName, String namespaceURI, String attrName)
            throws XMLStreamException {
        if (reader != null) {
            while (reader.hasNext()) {
                XMLEvent event = reader.peek();

                if (event.isStartElement()) {
                    StartElement start = (StartElement) event;

                    if (start.getName().getLocalPart().equals(elementName)) {
                        Attribute attr = start.getAttributeByName(new QName(namespaceURI, attrName));

                        if (attr != null) {
                            String value = attr.getValue();

                            return value;
                        }
                    }
                }

                reader.nextEvent();
            }
        }

        return null;
    }

    public static String getTextStAXContext(XMLEventReader reader, String elementName) throws XMLStreamException {
        if (reader != null) {
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.isStartElement()) {
                    StartElement start = (StartElement) event;
                    if (start.getName().getLocalPart().equals(elementName)) {
                        event = reader.nextEvent();
                        Characters chars = (Characters) event;
                        String value = chars.getData();
                        reader.nextEvent();

                        return value;
                    }
                }
            }
        }

        return "";
    }

    public static void deleteNodeInStAX(String id, String xmlFileName, String realPath) {
        InputStream is = null;
        XMLEventReader reader = null;
        OutputStream os = null;
        XMLEventWriter writer = null;

        try {
            is = new FileInputStream(realPath + xmlFileName);
            reader = parseFileToStAXIterator(is);

            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            os = new FileOutputStream(realPath + xmlFileName + ".new");
            writer = xof.createXMLEventWriter(os);

            boolean delete = false;

            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.getEventType() == XMLStreamConstants.START_ELEMENT && event.asStartElement()
                        .getName().getLocalPart().equals("student")) {
                    Attribute attr = event.asStartElement().getAttributeByName(new QName("id"));
                    String tmp = attr.getValue();

                    if (tmp.equals(id)) {
                        delete = true;
                        continue;
                    }
                } else if (event.getEventType() == XMLStreamConstants.END_ELEMENT
                            && event.asEndElement().getName().getLocalPart().equals("student")) {
                    if (delete) {
                        delete = false;
                        continue;
                    }
                } else if (delete) {
                    continue;
                }

                writer.add(event);
            }

                writer.flush();
                writer.close();

                is.close();
                os.close();

                File file = new File(realPath + xmlFileName);
                file.delete();

                file = new File(realPath + xmlFileName + ".new");
                file.renameTo(new File(realPath + xmlFileName));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException ex) {
                    Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, "Reader", ex);
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, "IS", ex);
                }
            }

            if (writer != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, "Writer", ex);
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, "OS", ex);
                }
            }
        }
    }

    public static void updateNodeInStAXUsingJAXB(String id, String sClass, String address, String xmlFileName, String realPath) {
        InputStream is = null;
        OutputStream os = null;
        XMLEventReader reader = null;
        XMLEventWriter writer = null;

        try {
            XMLInputFactory xif = XMLInputFactory.newInstance();
            is = new FileInputStream(realPath + xmlFileName);
            reader = xif.createXMLEventReader(is);

            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            os = new FileOutputStream(realPath + xmlFileName + ".new");
            writer = xof.createXMLEventWriter(os);

            JAXBContext jaxb = JAXBContext.newInstance(StudentDTO.class);
            Unmarshaller unmarshaller = jaxb.createUnmarshaller();
            Marshaller marshaller = jaxb.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

            while (reader.hasNext()) {
                if (reader.peek().isStartElement() && reader.peek().asStartElement().getName().getLocalPart().equals("student")) {
                    StudentDTO dto = (StudentDTO) unmarshaller.unmarshal(reader);

                    if (dto.getId().equals(id)) {
                        dto.setAddress(address);
                        dto.setsClass(sClass);
                    }

                    marshaller.marshal(dto, writer);
                } else {
                    writer.add(reader.nextEvent());
                }
            }

            writer.flush();
            writer.close();

            is.close();
            os.close();

            File file = new File(realPath + xmlFileName);
            file.delete();

            file = new File(realPath + xmlFileName + ".new");
            file.renameTo(new File(realPath + xmlFileName));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException ex) {
                    Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, "Reader", ex);
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, "IS", ex);
                }
            }

            if (writer != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, "Writer", ex);
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, "OS", ex);
                }
            }
        }
    }

    public static boolean insertNodeInStAXUsingJAXB(String id, String sClass, String lastname, String middlename,
                                                    String firstname, String address, String password, String status,
                                                    int sex, String xmlFileName, String realPath) {
        InputStream is = null;
        OutputStream os = null;
        XMLEventReader reader = null;
        XMLEventWriter writer = null;

        try {
            XMLInputFactory xif = XMLInputFactory.newInstance();
            is = new FileInputStream(realPath + xmlFileName);
            reader = xif.createXMLEventReader(is);

            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            os = new FileOutputStream((realPath + xmlFileName + ".new"));
            writer = xof.createXMLEventWriter(os);

            JAXBContext jaxb = JAXBContext.newInstance(StudentDTO.class);
            Unmarshaller unmarshaller = jaxb.createUnmarshaller();
            Marshaller marshaller = jaxb.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

            while (reader.hasNext()) {
                if (reader.peek().isEndElement() && reader.peek().asEndElement().getName().getLocalPart().equals("students")) {
                    StudentDTO dto = new StudentDTO(id, sClass, firstname, middlename, lastname, sex, password, address, status);
                    marshaller.marshal(dto, writer);
                    writer.add(reader.nextEvent());
                } else {
                    writer.add(reader.nextEvent());
                }
            }

            writer.flush();
            writer.close();

            is.close();
            os.close();

            File file = new File(realPath + xmlFileName);
            file.delete();

            file = new File(realPath + xmlFileName + ".new");
            file.renameTo(new File(realPath + xmlFileName));

            return true;
        }  catch (FileNotFoundException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException ex) {
                    Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, "Reader", ex);
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, "IS", ex);
                }
            }

            if (writer != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, "Writer", ex);
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    Logger.getLogger(XMLUtils.class.getName()).log(Level.SEVERE, "OS", ex);
                }
            }
        }

        return false;
    }
}
