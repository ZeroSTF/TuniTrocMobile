/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import entities.Product;
import utils.Statics;

public class ServiceProduct {
    
    private ConnectionRequest req;
    private boolean resultOK;
    public static ServiceProduct instance = null;
    public ArrayList<Product> products;
    
    private ServiceProduct() {
        req = new ConnectionRequest();
    }
    
    public static ServiceProduct getInstance() {
        if (instance == null) {
            instance = new ServiceProduct();
        }
        return instance;
    }
    
    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> productList = new ArrayList<>();
        String url = Statics.BASE_URL + "/produit/json/getAll";
        req.setUrl(url);
        req.setPost(false);

        if (req != null) {
            req.addResponseListener(new ActionListener<NetworkEvent>() {
                @Override
                public void actionPerformed(NetworkEvent evt) {
                    try {
                        ArrayList<Product> parsedList = parseProduct(new String(req.getResponseData()));
                        productList.addAll(parsedList);
                    } catch (ParseException  | IOException ex) {
                        ex.printStackTrace();
                    }
                    req.removeResponseListener(this);
                }
            });
        }
        NetworkManager.getInstance().addToQueueAndWait(req);
        return productList;
    }

    public ArrayList<Product> parseProduct(String jsonText) throws ParseException, java.text.ParseException, IOException {
        ArrayList<Product> productList = new ArrayList<>();
        if (jsonText != null && !jsonText.isEmpty()) {
            JSONParser j = new JSONParser();
            Map<String, Object> productListJson = j.parseJSON(new StringReader(jsonText));
            List<Map<String, Object>> list = (List<Map<String, Object>>) productListJson.get("root");
            for (Map<String, Object> obj : list) {
                Product p = new Product();
                float id = Float.parseFloat(obj.get("id").toString());
                p.setId((int)id);
                p.setType(obj.get("type").toString());
                p.setCategorie(obj.get("categorie").toString());
                p.setNom(obj.get("nom").toString());
                p.setLibelle(obj.get("libelle").toString());
                String prix = obj.get("ville").toString();
                p.setPrix(prix);
              //  float stock = Float.parseFloat(obj.get("stock").toString());
              //  p.setStock((int)stock);
                productList.add(p);
            }
        }
        return productList;
    }
    
    public boolean addProduct(Product p) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String url = Statics.BASE_URL + "/produit/json/new?type=" + p.getType() + "&categorie=" + p.getCategorie()
            + "&nom=" + p.getNom() + "&libelle=" + p.getLibelle() + "&prix=" + p.getPrix() + "&stock=" + p.getStock();
    ConnectionRequest req = new ConnectionRequest(url);
    req.setPost(false);
    req.addResponseListener(new ActionListener<NetworkEvent>() {
        @Override
        public void actionPerformed(NetworkEvent evt) {
            resultOK = req.getResponseCode() == 200;
            req.removeResponseListener(this);
            if (resultOK) {
                Dialog.show("Success", "Produit ajouté avec succès", "OK", null);
            } else {
                Dialog.show("Error", "Erreur lors de l'ajout du produit", "OK", null);
            }
        }
    });
    NetworkManager.getInstance().addToQueueAndWait(req);
    return resultOK;
}
    

    public boolean editProduct(Product p) {

    String url = Statics.BASE_URL + "/produit/json/editj?id="+p.getId()+"&type=" + p.getType()+ "&categorie=" + p.getCategorie()+ "&nom=" + p.getNom() + "&libelle=" + p.getLibelle() + "&prix=" + p.getPrix() + "&stock=" + p.getStock(); //création de l'URL
    req.setUrl(url);// Insertion de l'URL de notre demande de connexion
    NetworkManager.getInstance().addToQueueAndWait(req);
    System.out.println(url);
    req.addResponseListener(new ActionListener<NetworkEvent>() {
        @Override
        public void actionPerformed(NetworkEvent evt) {
            resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
            req.removeResponseListener(this); 

        }
    });
    NetworkManager.getInstance().addToQueueAndWait(req);
    return resultOK;
}
    
    public void deleteProduct(int id) {

    Dialog d = new Dialog();
    if (d.show("Delete Product", "Do you really want to remove this Product", "Yes", "No")) {

        req.setUrl(Statics.BASE_URL + "/produit/json/delete?id=" + id);
        //System.out.println(Statics.BASE_URL+"/deleteProductMobile?id="+id);
        NetworkManager.getInstance().addToQueueAndWait(req);
    }
    d.dispose();
}




}
