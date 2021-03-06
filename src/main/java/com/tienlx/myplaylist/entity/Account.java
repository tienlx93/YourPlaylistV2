package com.tienlx.myplaylist.entity;
// Generated 12/02/2015 11:33:24 PM by Hibernate Tools 3.2.1.GA


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Account generated by hbm2java
 */
@Entity
@Table(name = "account"
        , catalog = "yourplaylist"
)
public class Account implements java.io.Serializable {


    private String email;
    private String username;
    private String password;
    private int role;

    public Account() {
    }

    public Account(String email, String username, String password, int role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Id
    @Column(name = "Email", unique = true, nullable = false, length = 50)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "Username", nullable = false, length = 50)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "Password", nullable = false, length = 20)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "Role", nullable = false)
    public int getRole() {
        return this.role;
    }

    public void setRole(int role) {
        this.role = role;
    }

}


