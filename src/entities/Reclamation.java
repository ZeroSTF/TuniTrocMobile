/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.util.Date;

/**
 *
 * @author ilyes
 */
public class Reclamation {
        private int id;
    private String cause;
    private boolean etat;
    private int idUserr;
    private int idUsers;
    private String photo;
    private Date date;

    public Reclamation() {
    }

    public Reclamation(String cause, boolean etat, int idUserr, int idUsers, String photo, Date date) {
        this.cause = cause;
        this.etat = etat;
        this.idUserr = idUserr;
        this.idUsers = idUsers;
        this.photo = photo;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    public int getIdUserr() {
        return idUserr;
    }

    public void setIdUserr(int idUserr) {
        this.idUserr = idUserr;
    }

    public int getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(int idUsers) {
        this.idUsers = idUsers;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    
    
}
