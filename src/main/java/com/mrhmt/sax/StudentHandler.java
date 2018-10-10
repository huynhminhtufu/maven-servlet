package com.mrhmt.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class StudentHandler extends DefaultHandler {
    private String username;
    private String password;
    private boolean foundUsername;
    private boolean foundPassword;
    private boolean found;
    private String currentTagName;
    private String fullName;

    public StudentHandler() {
        this.foundUsername = false;
        this.foundPassword = false;
    }

    public StudentHandler(String username, String password) {
        this.username = username;
        this.password = password;
        this.foundUsername = false;
        this.foundPassword = false;
    }

    public String getFullName() {
        return this.fullName;
    }

    public boolean isFound() {
        return this.found;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (!this.found) {
            this.currentTagName = qName;

            if (qName.equals("student")) {
                String id = attributes.getValue("id");

                if (id.equals(this.username)) {
                    this.foundUsername = true;
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        this.currentTagName = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (!this.found) {
            if (this.foundUsername) {
                String str = new String(ch, start, length);
                if (currentTagName.equals("lastname")) {
                    this.fullName = str.trim();
                } else if (currentTagName.equals("middlename")) {
                    this.fullName += str.trim();
                } else if (currentTagName.equals("firstname")) {
                    this.fullName += str.trim();
                } else if (currentTagName.equals("password")) {
                    if (str.trim().equals(this.password)) {
                        this.foundPassword = true;

                        return;
                    }

                    this.foundUsername = false;
                }
            }

            if (this.foundPassword) {
                String str = new String(ch, start, length);

                if (currentTagName.equals("status")) {
                    if (!str.trim().equals("dropout")) {
                        this.found = true;

                        return;
                    }

                    this.found = false;
                }
            }
        }
    }
}
