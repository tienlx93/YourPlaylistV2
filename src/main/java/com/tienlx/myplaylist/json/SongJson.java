/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.json;

/**
 *
 * @author tienl_000
 */
public class SongJson {
    private String id;
    private String title;
    private String artist;
    private long playCount;
    private String category;
    private String artistFullName;
    private String image;

    public SongJson() {
    }

    public SongJson(String id, String title, String artist, long playCount, String category, String artistFullName, String image) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.playCount = playCount;
        this.category = category;
        this.artistFullName = artistFullName;
        this.image = image;
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * @param artist the artist to set
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * @return the playCount
     */
    public long getPlayCount() {
        return playCount;
    }

    /**
     * @param playCount the playCount to set
     */
    public void setPlayCount(long playCount) {
        this.playCount = playCount;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the artistFullName
     */
    public String getArtistFullName() {
        return artistFullName;
    }

    /**
     * @param artistFullName the artistFullName to set
     */
    public void setArtistFullName(String artistFullName) {
        this.artistFullName = artistFullName;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }
    
    
}
