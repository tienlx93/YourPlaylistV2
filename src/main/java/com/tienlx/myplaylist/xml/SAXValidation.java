/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.xml;

import com.sun.org.apache.xml.internal.utils.DefaultErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author tienl_000
 */
public class SAXValidation  extends DefaultErrorHandler {
    @Override
    public void error(SAXParseException exception) throws SAXException {
        System.out.println("Parsing error:\n Line: " + exception.getLineNumber()
                + "\n Column: " + exception.getColumnNumber()
                + "\n URI: " + exception.getSystemId()
                + "\n Message: " + exception.getMessage()
                );
    }
}
