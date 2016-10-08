/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.dao;

import java.io.Serializable;
import java.util.List;

import com.tienlx.myplaylist.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author tienl_000
 */
public class BaseDAO<E, I extends Serializable> {

    protected Session session;
    private final Class<E> type;

    public BaseDAO(Class<E> type) {
        this.type = type;
        SessionFactory sf = HibernateUtil.getSessionFactory();
        session = sf.getCurrentSession();
    }

    public List<E> findAll() {
        Transaction trans = null;
        try {
            trans = session.beginTransaction();
            trans.begin();
            return session.createCriteria(type).list();
        } catch (Exception e) {
            if (trans != null && trans.isActive()) {
                trans.rollback();
            }
        }
        return null;
        
    }

    public E get(String id) {
        Transaction trans = null;
        try {
            trans = session.beginTransaction();

            E source = (E) session.get(type, id);

            return source;
        } catch (Exception e) {
            if (trans != null && trans.isActive()) {
                trans.rollback();
            }
        }
        return null;
    }

    public boolean save(E item) {
        Transaction trans = null;
        try {
            trans = session.beginTransaction();

            session.save(item);
            session.getTransaction().commit();

            return true;
        } catch (Exception e) {
            if (trans != null && trans.isActive()) {
                trans.rollback();
            }
        }
        return false;
    }

    public boolean remove(E item) {
        Transaction trans = null;
        try {
            trans = session.beginTransaction();
            trans.begin();
            session.delete(item);
            session.flush();
            trans.commit();
            return true;
        } catch (Exception e) {
            if (trans != null && trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();

        }
        return false;
    }
}
