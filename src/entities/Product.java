/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

/**
 *
 * @author ilyes
 */

public class Product {
    private int id;
    private String type;
    private String categorie;
    private String nom;
    private String libelle;
    private String prix;
    private int stock;

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    

    public Product(int id, String type, String categorie, String nom, String libelle, String prix, int stock) {
        this.id = id;
        this.type = type;
        this.categorie = categorie;
        this.nom = nom;
        this.libelle = libelle;
        this.prix = prix;
        this.stock = stock;
    }
    

    public Product(String type, String categorie, String nom, String libelle, String prix, int stock) {
        this.type = type;
        this.categorie = categorie;
        this.nom = nom;
        this.libelle = libelle;
        this.prix = prix;
        this.stock = stock;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}

