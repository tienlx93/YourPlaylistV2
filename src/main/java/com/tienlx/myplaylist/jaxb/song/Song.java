//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.03 at 09:42:56 PM ICT 
//


package com.tienlx.myplaylist.jaxb.song;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Song complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Song">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Artist" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AlbumArt" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Source" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Lyrics" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Song", propOrder = {
    "title",
    "artist",
    "albumArt",
    "source",
    "lyrics"
})
@XmlRootElement(name="Song")
public class Song {

    @XmlElement(name = "Title", required = true)
    protected String title;
    @XmlElement(name = "Artist", required = true)
    protected String artist;
    @XmlElement(name = "AlbumArt", required = true)
    protected String albumArt;
    @XmlElement(name = "Source", required = true)
    protected String source;
    @XmlElement(name = "Lyrics", required = true)
    protected String lyrics;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the artist property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Sets the value of the artist property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArtist(String value) {
        this.artist = value;
    }

    /**
     * Gets the value of the albumArt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlbumArt() {
        return albumArt;
    }

    /**
     * Sets the value of the albumArt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlbumArt(String value) {
        this.albumArt = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSource(String value) {
        this.source = value;
    }

    /**
     * Gets the value of the lyrics property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLyrics() {
        return lyrics;
    }

    /**
     * Sets the value of the lyrics property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLyrics(String value) {
        this.lyrics = value;
    }

}
