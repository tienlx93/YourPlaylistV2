/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.dao;

import com.tienlx.myplaylist.entity.Artist;
import org.hibernate.Query;

/**
 *
 * @author tienl_000
 */
public class ArtistDAO extends BaseDAO<Artist, String> {

    public ArtistDAO() {
        super(Artist.class);
    }
    
    public Artist getByName(String searchName) {
        Artist artist = null;
        try {
            session.getTransaction().begin();
            String sql = "FROM Artist WHERE NameSearch LIKE ?";
            Query query = session.createQuery(sql);
            query.setParameter(0, searchName);
            
            artist = (Artist) query.uniqueResult();

            session.flush();
            session.getTransaction().commit();

        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        }
        return artist;
    }

}
