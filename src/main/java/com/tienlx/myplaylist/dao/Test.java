/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.dao;

import com.tienlx.myplaylist.entity.Playlist;
import com.tienlx.myplaylist.entity.Song;
import java.util.List;
import com.tienlx.myplaylist.xml.AlbumParser;
import com.tienlx.myplaylist.xml.type.AlbumType;

/**
 *
 * @author tienl_000
 */
public class Test {

    public static void main(String[] args) {
        /*PlaylistDAO dao = new PlaylistDAO();
        SongDAO songDao = new SongDAO();
        AlbumType album;


        String id = dao.getNewId();

        Song song1 = songDao.get("ZW6BBID6");
        Song song2 = songDao.get("ZW6BZACA");

        String name = "Last Playlist";
        String accountEmail = "tienlx@yourplaylist.tk";
        String accountName = "Admin";

        Playlist albumEntity = new Playlist(id, name, 0, accountEmail, accountName);

        album = new AlbumType(id, name, accountEmail);

        List<Song> songs = album.getSongList();
        songs.add(song1);
        songs.add(song2);

        if (dao.save(albumEntity)) {
            AlbumParser parser = new AlbumParser();
            parser.parse("src/" + id + ".xml", album);
        }*/
        
        AlbumParser parser = new AlbumParser();
        parser.validate("web/playlist/1.xml");
        
    }
}
