//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.03 at 03:38:59 PM ICT 
//


package com.tienlx.myplaylist.jaxb.artist;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the jaxb.artist package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Artist_QNAME = new QName("http://yourplaylist.tk/Schema", "Artist");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: jaxb.artist
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Artist }
     * 
     */
    public Artist createArtist() {
        return new Artist();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Artist }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://yourplaylist.tk/Schema", name = "Artist")
    public JAXBElement<Artist> createArtist(Artist value) {
        return new JAXBElement<Artist>(_Artist_QNAME, Artist.class, null, value);
    }

}
