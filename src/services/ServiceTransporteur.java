/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.l10n.ParseException;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
import entities.Transporteur;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import utils.Statics;

/**
 *
 * @author ilyes
 */
public class ServiceTransporteur {
    private ConnectionRequest req;
private boolean resultOK;
public static ServiceTransporteur instance = null;
public ArrayList<Transporteur> transporteurs;

private ServiceTransporteur() {
    req = new ConnectionRequest();
}

public static ServiceTransporteur getInstance() {
    if (instance == null) {
        instance = new ServiceTransporteur();
    }
    return instance;
}

public ArrayList<Transporteur> getAllTransporteurs() {
    ArrayList<Transporteur> transporteurList = new ArrayList<>();
    String url = Statics.BASE_URL + "/transporteur/json/getAll";
    req.setUrl(url);
    req.setPost(false);

    if (req != null) {
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    ArrayList<Transporteur> parsedList = parseTransporteur(new String(req.getResponseData()));
                    transporteurList.addAll(parsedList);
                } catch (ParseException | IOException ex) {
                    ex.printStackTrace();
                }
                req.removeResponseListener(this);
            }
        });
    }
    NetworkManager.getInstance().addToQueueAndWait(req);
    return transporteurList;
}

public ArrayList<Transporteur> parseTransporteur(String jsonText) throws ParseException, IOException {
    ArrayList<Transporteur> transporteurList = new ArrayList<>();
    if (jsonText != null && !jsonText.isEmpty()) {
        JSONParser j = new JSONParser();
        Map<String, Object> transporteurListJson = j.parseJSON(new StringReader(jsonText));
        List<Map<String, Object>> list = (List<Map<String, Object>>) transporteurListJson.get("root");
        for (Map<String, Object> obj : list) {
            Transporteur t = new Transporteur();
            float id = Float.parseFloat(obj.get("id").toString());
            t.setId((int) id);
            t.setNom(obj.get("nom").toString());
            t.setPrenom(obj.get("prenom").toString());
            float id1 = Float.parseFloat(obj.get("numTel").toString());
            t.setNumTel((int) id1);
            transporteurList.add(t);
        }
    }
    return transporteurList;
}

public boolean addTransporteur(Transporteur t) {
    String url = Statics.BASE_URL + "/transporteur/json/new?nom=" + t.getNom() + "&prenom=" + t.getPrenom() + "&numTel=" + t.getNumTel();
    ConnectionRequest req = new ConnectionRequest(url);
    req.setPost(false);
    req.addResponseListener(new ActionListener<NetworkEvent>() {
        @Override
        public void actionPerformed(NetworkEvent evt) {
            resultOK = req.getResponseCode() == 200;
            req.removeResponseListener(this);
            
                Dialog.show("Success", "Transporteur ajouté avec succès", "OK", null);
           
        }
    });
    NetworkManager.getInstance().addToQueueAndWait(req);
    return resultOK;
}

public boolean editTransporteur(Transporteur t) {
    String url = Statics.BASE_URL + "/transporteur/json/edit?id=" + t.getId() + "&nom=" + t.getNom() + "&prenom=" + t.getPrenom() + "&numTel=" + t.getNumTel();
    ConnectionRequest req = new ConnectionRequest(url);
    req.setPost(false);
    req.addResponseListener(new ActionListener<NetworkEvent>() {
        @Override
        public void actionPerformed(NetworkEvent evt) {
            resultOK = req.getResponseCode() == 200;
            req.removeResponseListener(this);
            if (resultOK) {
                Dialog.show("Success", "Transporteur modifié avec succès", "OK", null);
            } else {
                Dialog.show("Error", "Erreur lors de la modification du transporteur", "OK", null);
            }
        }
    });
    NetworkManager.getInstance().addToQueueAndWait(req);
    return resultOK;
}

public boolean deleteTransporteur(int id) {
    Dialog d = new Dialog();
    boolean resultOK = true;
    if (d.show("Delete Transporteur", "Do you really want to remove this Transporteur", "Yes", "No")) {
        String url = Statics.BASE_URL + "/transporteur/json/delete?id=" + id;
        ConnectionRequest req = new ConnectionRequest(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
              //  resultOK = req.getResponseCode() == 200;
                req.removeResponseListener(this);
                if (resultOK) {
                    Dialog.show("Success", "Transporteur supprimé avec succès", "OK", null);
                } else {
                    Dialog.show("Error", "Erreur lors de la suppression du transporteur", "OK", null);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
    }
    d.dispose();
    return resultOK;
}


}
