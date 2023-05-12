/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.Date;

/**
 *
 * @author ZeroS TF
 */
public class Post {
    private int idPost;
    private String description;
    private Date dateP;
    private String image;
    private int idUser;

    public Post() {
    }

    public Post(String description, Date dateP, String image, int idUser) {
        this.description = description;
        this.dateP = dateP;
        this.image = image;
        this.idUser = idUser;
    }

    public Post(int idPost, String description, Date dateP, String image, int idUser) {
        this.idPost = idPost;
        this.description = description;
        this.dateP = dateP;
        this.image = image;
        this.idUser = idUser;
    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateP() {
        return dateP;
    }

    public void setDateP(Date dateP) {
        this.dateP = dateP;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    
}
