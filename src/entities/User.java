/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.time.LocalDateTime;


/**
 *
 * @author ZeroS TF
 */
public class User {
    private int id;
    private String email;
    private String pwd;
    private String nom;
    private String prenom;
    private String photo;
    private String numTel;
    private String ville;
    private int valeurFidelite;
    private Boolean role;
    private String salt;
    private LocalDateTime date;
    private String etat;

    public User() {
    }

    public User(int id, String email, String pwd, String nom, String prenom, String photo, String numTel, String ville, int valeurFidelite, Boolean role, String salt, LocalDateTime date, String etat) {
        this.id = id;
        this.email = email;
        this.pwd = pwd;
        this.nom = nom;
        this.prenom = prenom;
        this.photo = photo;
        this.numTel = numTel;
        this.ville = ville;
        this.valeurFidelite = valeurFidelite;
        this.role = role;
        this.salt = salt;
        this.date = date;
        this.etat = etat;
    }

    public User(String email, String pwd, String nom, String prenom, String photo, String numTel, String ville, int valeurFidelite, Boolean role, String salt, LocalDateTime date, String etat) {
        this.email = email;
        this.pwd = pwd;
        this.nom = nom;
        this.prenom = prenom;
        this.photo = photo;
        this.numTel = numTel;
        this.ville = ville;
        this.valeurFidelite = valeurFidelite;
        this.role = role;
        this.salt = salt;
        this.date = date;
        this.etat = etat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public int getValeurFidelite() {
        return valeurFidelite;
    }

    public void setValeurFidelite(int valeurFidelite) {
        this.valeurFidelite = valeurFidelite;
    }

    public Boolean getRole() {
        return role;
    }

    public void setRole(Boolean role) {
        this.role = role;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email=" + email + ", pwd=" + pwd + ", nom=" + nom + ", prenom=" + prenom + ", photo=" + photo + ", numTel=" + numTel + ", ville=" + ville + ", valeurFidelite=" + valeurFidelite + ", role=" + role + ", salt=" + salt + ", date=" + date + ", etat=" + etat + '}';
    }
    
    
}
