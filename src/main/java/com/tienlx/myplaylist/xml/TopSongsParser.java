/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.xml;

import com.tienlx.myplaylist.dao.SongDAO;
import com.tienlx.myplaylist.entity.Artist;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import com.tienlx.myplaylist.jaxb.playlist.SongType;
import com.tienlx.myplaylist.jaxb.playlist.TopSongs;
import com.tienlx.myplaylist.json.SongJson;

/**
 *
 * @author tienl_000
 */
public class TopSongsParser {

    private TopSongs result;
    private String filename;
    private String path;
    private ArrayList<String> fileList;

    public TopSongsParser() {
        Date today = new Date();
        SimpleDateFormat fileFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat stringFormat = new SimpleDateFormat("dd-MM-YYYY");
        filename = fileFormat.format(today);
        String dayString = stringFormat.format(today);
        SongDAO dao = new SongDAO();
        List<SongJson> search = dao.search("", 40);
        result = new TopSongs();
        result.setDateCreated(dayString);
        TopSongs.List list = new TopSongs.List();
        ArrayList<SongType> songList = (ArrayList<SongType>) list.getSong();
        SongType song;
        for (SongJson songJson : search) {
            song = new SongType();
            song.setArtist(songJson.getArtist());
            song.setCategory(songJson.getCategory());
            song.setPlayCount(songJson.getPlayCount() + "");
            song.setTitle(songJson.getTitle());
            songList.add(song);
        }
        result.setList(list);
    }

    public boolean checkExisted() {
        return false;
    }

    public void marshallXML(String filename) {
        try {        
            JAXBContext context = JAXBContext.newInstance(TopSongs.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(result, new File(filename));
        } catch (JAXBException ex) {
            Logger.getLogger(TopSongsParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the fileList
     */
    public ArrayList<String> getFileList() {
        if (this.path != null) {
            fileList = new ArrayList<String>();
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    if (listOfFiles[i].getName().contains("xml")) {
                        String fileName = listOfFiles[i].getName();
                        fileList.add(fileName.substring(0,fileName.indexOf(".")));
                    }
                }
            }
        }
        return fileList;
    }

    /**
     * @param fileList the fileList to set
     */
    public void setFileList(ArrayList<String> fileList) {
        this.fileList = fileList;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }
}
