/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.xml;

import com.tienlx.myplaylist.entity.Song;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import com.tienlx.myplaylist.xml.type.AlbumType;

/**
 *
 * @author tienl_000
 */
public class AlbumParser {

    public boolean parse(String filename, AlbumType album) { //TEST OK
        FileOutputStream outputFile;
        XMLOutputFactory xof = XMLOutputFactory.newInstance();
        try {
            outputFile = new FileOutputStream(filename);
            XMLStreamWriter writer = xof.createXMLStreamWriter(outputFile, "UTF-8");

            writer.writeStartDocument();
            writer.writeStartElement("SongList");
            writer.writeNamespace("", "http://yourplaylist.tk/Schema");
//            writer.writeNamespace("xsi", "http://www.w3.org/2000/10/XMLSchema-instance");
//            writer.writeAttribute("http://www.w3.org/2000/10/XMLSchema-instance", 
//                    "schemaLocation", "http://yourplaylist.tk/Schema SongList.xsd");

            writer.writeStartElement("Id");
            writer.writeCharacters(album.getId());
            writer.writeEndElement();

            writer.writeStartElement("Name");
            writer.writeCharacters(album.getName());
            writer.writeEndElement();

            writer.writeStartElement("AccountEmail");
            writer.writeCharacters(album.getAccountEmail());
            writer.writeEndElement();

            writer.writeStartElement("List");
            for (Song song : album.getSongList()) {
                writer.writeStartElement("Song");

                writer.writeStartElement("Id");
                writer.writeCharacters(song.getId());
                writer.writeEndElement();

                writer.writeStartElement("Title");
                writer.writeCharacters(song.getTitle());
                writer.writeEndElement();

                writer.writeStartElement("Artist");
                writer.writeCharacters(song.getArtist());
                writer.writeEndElement();

                writer.writeEndElement(); //End Song
            }
            writer.writeEndElement(); //End List

            writer.writeEndElement(); //End SongList
            writer.writeEndDocument();

            writer.flush();
            writer.close();

            return validate(filename);
        } catch (XMLStreamException ex) {
            Logger.getLogger(AlbumParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AlbumParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean validate(String filename) { //TESTOK
        DefaultHandler handler = new DefaultHandler();
        ErrorHandler error = new SAXValidation();
        try {
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(handler);
            parser.setErrorHandler(error);
            parser.setFeature("http://apache.org/xml/features/validation/schema", true);
            parser.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", 
                    "http://yourplaylist.tk/Schema SongList.xsd");
            parser.parse(filename);
            return true;
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
