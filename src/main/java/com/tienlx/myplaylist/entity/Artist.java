package com.tienlx.myplaylist.entity;
// Generated 12/02/2015 11:33:24 PM by Hibernate Tools 3.2.1.GA


import javax.persistence.*;

/**
 * Artist generated by hbm2java
 */
@Entity
@Table(name = "artist"
        , catalog = "yourplaylist"
)
public class Artist implements java.io.Serializable {


    private String artistId;
    private String nameSearch;
    private String name;
    private String image;
    private String bio;
    private String info;

    public Artist() {
    }


    public Artist(String nameSearch, String name, String image) {
        this.nameSearch = nameSearch;
        this.name = name;
        this.image = image;
    }

    public Artist(String nameSearch, String name, String image, String bio, String info, String artistId) {
        this.nameSearch = nameSearch;
        this.name = name;
        this.image = image;
        this.bio = bio;
        this.info = info;
        this.artistId = artistId;
    }

    @Column(name = "NameSearch", unique = true, nullable = false, length = 50)
    public String getNameSearch() {
        return this.nameSearch;
    }

    public void setNameSearch(String nameSearch) {
        this.nameSearch = nameSearch;
    }

    @Column(name = "Name", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "Image", nullable = false, length = 250)
    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Column(name = "Bio")
    @Lob
    public String getBio() {
        return this.bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Column(name = "Info")
    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Id
    @Column(name = "artist_id")
    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String id) {
        this.artistId = id;
    }
}


