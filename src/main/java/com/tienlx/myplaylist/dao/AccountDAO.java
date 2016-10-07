/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.dao;

import com.tienlx.myplaylist.entity.Account;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author tienl_000
 */
public class AccountDAO extends BaseDAO<Account, String> {

    public AccountDAO() {
        super(Account.class);
    }

    public Account login(String email, String password) { //TESTED OK
        try {
            session.getTransaction().begin();
            String sql = "from Account where email = ? and password = ?";
            Query query = session.createQuery(sql);
            query.setString(0, email);
            query.setString(1, password);
            com.tienlx.myplaylist.entity.Account account = (Account) query.uniqueResult();

            if (account != null) {
                return account;
            }
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        }
        return null;
    }


}
