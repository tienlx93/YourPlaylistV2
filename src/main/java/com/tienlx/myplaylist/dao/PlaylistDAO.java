/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.dao;

import com.tienlx.myplaylist.entity.Playlist;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Transaction;

/**
 *
 * @author tienl_000
 */
public class PlaylistDAO extends BaseDAO<Playlist, String> {

    public PlaylistDAO() {
        super(Playlist.class);
    }

    public String getNewId() {
        String id;
        try {
            session.getTransaction().begin();
            String sql = "select MAX(CAST(Id AS DECIMAL)) FROM playlist";
            Query query = session.createSQLQuery(sql);
            List rows = query.list();
            if (rows.size() > 0 && rows.get(0) != null) {
                String number = rows.get(0).toString();
                int value = Integer.parseInt(number);
                id = (value + 1) + "";
            } else {
                id = "1";
            }

        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        }

        return id;
    }
    
    public String getLastIdByEmail(String email) {
        String lastId = null;
        try {
            session.getTransaction().begin();
            String sql = "select MAX(CAST(Id AS DECIMAL)) FROM playlist WHERE AccountEmail LIKE ?";
            Query query = session.createSQLQuery(sql);
            query.setParameter(0, email);
            Object result = query.uniqueResult();
            if (result != null) {
                lastId = result.toString();        
            }
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        }

        return lastId;
    }
    
    public List<Playlist> getPlaylistByEmail(String email) {
        List<Playlist> playlists = null;
        try {
            session.getTransaction().begin();
            String sql = "FROM Playlist WHERE accountEmail LIKE ?";
            Query query = session.createQuery(sql);
            query.setParameter(0, email);
            playlists = query.list();    

        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        }

        return playlists;
    }
    
    public List<Playlist> getTopPlaylist(int limit) {
        List<Playlist> playlists = null;
        try {
            session.getTransaction().begin();
            String sql = "FROM Playlist ORDER BY PlayCount DESC";
            Query query = session.createQuery(sql);
            playlists = query.setMaxResults(limit).list();    

        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        }

        return playlists;
    }
    
    public boolean updatePlayCount(String id) {
        Transaction trans = null;
        try {
            Playlist playlist = get(id);
            
            trans = session.beginTransaction();
            playlist.setPlayCount(playlist.getPlayCount() + 1);
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
