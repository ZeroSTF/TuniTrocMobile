/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.codename1.io.Preferences;

/**
 *
 * @author Lenovo
 */
public class SessionManager {
    
    public static Preferences pref ; // 3ibara memoire sghira nsajlo fiha data 
    
    
    
    // hethom données ta3 user lyt7b tsajlhom fi session  ba3d login 
    private static int id ; 
    private static String userName ; 
    private static String email; 
    private static String passowrd ;
    private static String ville;
    private static boolean isAdmin;
    private static String numTel;
    public static int valeurFidelite;
    


    public static Preferences getPref() {
        return pref;
    }

    public static void setPref(Preferences pref) {
        SessionManager.pref = pref;
    }

    public static int getId() {
        return pref.get("id",id);// kif nheb njib id user connecté apres njibha men pref 
    }

    public static void setId(int id) {
        pref.set("id",id);//nsajl id user connecté  w na3tiha identifiant "id";
    }

    
    public static String getUserName() {
        return pref.get("username",userName);
    }

    public static void setUserName(String userName) {
         pref.set("username",userName);
    }

    public static String getEmail() {
        return pref.get("email",email);
    }

    public static void setEmail(String email) {
         pref.set("email",email);
    }

    public static String getPassowrd() {
        return pref.get("passowrd",passowrd);
    }

    public static void setPassowrd(String passowrd) {
         pref.set("passowrd",passowrd);
    }

    public static String getVille() {
        return pref.get("ville",ville);
    }

    public static void setVille(String ville) {
         pref.set("ville",ville);
    }

    public static boolean isIsAdmin() {
        return pref.get("isAdmin",isAdmin);
    }

    public static void setIsAdmin(boolean isAdmin) {
        pref.set("isAdmin",isAdmin);
    }

    public static String getNumTel() {
        return pref.get("numTel",numTel);
    }

    public static void setNumTel(String numTel) {
        pref.set("numTel",numTel);
    }
    
    public static int getValeurFidelite() {
        return pref.get("valeurFidelite",valeurFidelite);
    }

    public static void setValeurFidelite(int valeurFidelite) {
        pref.set("valeurFidelite",valeurFidelite);
    }
    
    
    
    
}
