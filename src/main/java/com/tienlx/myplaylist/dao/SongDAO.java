/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.dao;

import com.tienlx.myplaylist.entity.Song;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import com.tienlx.myplaylist.json.SongJson;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Transaction;

/**
 *
 * @author tienl_000
 */
public class SongDAO extends BaseDAO<Song, String> {

    public SongDAO() {
        super(Song.class);
    }

    public List<String> listArtists() {
        ArrayList<String> list;
        try {
            session.getTransaction().begin();
            String sql = "SELECT ArtistSearch FROM song GROUP BY ArtistSearch";
            Query query = session.createSQLQuery(sql);
            List rows = query.list();
            list = (ArrayList<String>) rows;
            session.flush();
            session.getTransaction().commit();

        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public List search(String name, int limit) {
        List list;
        List<SongJson> songListJson = new ArrayList<SongJson>();
        try {
            session.getTransaction().begin();
            String sql;
            if (limit == 0 || limit >= 100) {
                sql = "SELECT a.Id, a.Title, a.Artist, a.PlayCount, a.Category, b.Name, b.Image "
                        + "FROM song a LEFT JOIN artist b ON a.ArtistSearch LIKE CONCAT('%', b.NameSearch) "
                        + "WHERE a.TitleSearch LIKE ? OR a.ArtistSearch LIKE ? "
                        + "ORDER BY RAND()";
            } else {
                sql = "SELECT a.Id, a.Title, a.Artist, a.PlayCount, a.Category, b.Name, b.Image "
                        + "FROM song a LEFT JOIN artist b ON a.ArtistSearch LIKE CONCAT('%', b.NameSearch) "
                        + "WHERE a.TitleSearch LIKE ? OR a.ArtistSearch LIKE ? "
                        + "ORDER BY PlayCount DESC";
            }
            SQLQuery query = session.createSQLQuery(sql);
            String search = "%" + name + "%";
            query.setString(0, search);
            query.setString(1, search);
            if (limit == 0) {
                list = query.list();
            } else {
                list = query.setMaxResults(limit).list();
            }
            SongJson song;
            for (Object item : list) {
                Object[] row = (Object[]) item;
                String id = (String) row[0];
                String title = (String) row[1];
                String artist = (String) row[2];
                BigInteger playCount = (BigInteger) row[3];
                String category = (String) row[4];
                String artistFullName = (String) row[5];
                String image = (String) row[6];

                song = new SongJson(id, title, artist, playCount.longValue(), category, artistFullName, image, artist);
                songListJson.add(song);
            }
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        }
        return songListJson;
    }

    public List<String> getCategoryList() {
        ArrayList<String> list;
        try {
            session.getTransaction().begin();
            String sql = "SELECT Category FROM song GROUP BY Category";
            Query query = session.createSQLQuery(sql);
            List rows = query.list();
            list = (ArrayList<String>) rows;
            session.flush();
            session.getTransaction().commit();

        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public boolean updatePlayCount(String id) {
        Transaction trans = null;
        try {
            Song song = get(id);
            
            trans = session.beginTransaction();
            song.setPlayCount(song.getPlayCount() + 1);
            session.getTransaction().commit();

            return true;
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
        }
        return false;
    }
}
