/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.xml.type;

import com.tienlx.myplaylist.entity.Song;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tienl_000
 */
public class AlbumType {
    private String id;
    private String name;
    private String accountEmail;
    private List<Song> songList;

    public AlbumType(String id, String name, String accountEmail) {
        this.id = id;
        this.name = name;
        this.accountEmail = accountEmail;
        songList = new ArrayList<Song>();
    }
    
    

    public AlbumType() {
        songList = new ArrayList<Song>();
    }
    

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the accountEmail
     */
    public String getAccountEmail() {
        return accountEmail;
    }

    /**
     * @param accountEmail the accountEmail to set
     */
    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    /**
     * @return the songList
     */
    public List<Song> getSongList() {
        return songList;
    }

    /**
     * @param songList the songList to set
     */
    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }
    
}
