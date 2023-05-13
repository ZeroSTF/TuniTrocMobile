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
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
import entities.Reclamation;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import utils.Statics;

/**
 *
 * @author hedi
 */
public class ServiceReclamation {
    
    private ConnectionRequest req;
    private boolean resultOK;
    public static ServiceReclamation instance = null;
    public ArrayList<Reclamation> reclamations;
    
    private ServiceReclamation() {
        req = new ConnectionRequest();
    }
    
    public static ServiceReclamation getInstance() {
        if (instance == null) {
            instance = new ServiceReclamation();
        }
        return instance;
    }
    
    public ArrayList<Reclamation> getAllReclamations() {
        ArrayList<Reclamation> reclamationsList = new ArrayList<>();
        String url = Statics.BASE_URL + "/reclamation/json/getAll";
        req.setUrl(url);
        req.setPost(false);

        if (req != null) {
            req.addResponseListener(new ActionListener<NetworkEvent>() {
                @Override
                public void actionPerformed(NetworkEvent evt) {
                    try {
                        ArrayList<Reclamation> parsedList = parseReclamation(new String(req.getResponseData()));
                        reclamationsList.addAll(parsedList);
                    } catch (ParseException | java.text.ParseException | IOException ex) {
                        ex.printStackTrace();
                    }
                    req.removeResponseListener(this);
                }
            });
        }
        NetworkManager.getInstance().addToQueueAndWait(req);
        return reclamationsList;
    }

    public ArrayList<Reclamation> parseReclamation(String jsonText) throws ParseException, java.text.ParseException, IOException {
        ArrayList<Reclamation> reclamationList = new ArrayList<>();
        if (jsonText != null && !jsonText.isEmpty()) {
            JSONParser j = new JSONParser();
            Map<String, Object> reclamationListJson = j.parseJSON(new StringReader(jsonText));
            List<Map<String, Object>> list = (List<Map<String, Object>>) reclamationListJson.get("root");
            for (Map<String, Object> obj : list) {
                Reclamation r = new Reclamation();
                float id = Float.parseFloat(obj.get("id").toString());
                r.setId((int)id);
                r.setCause(obj.get("cause").toString());
                r.setEtat(Boolean.parseBoolean(obj.get("etat").toString()));
                float idUserr = Float.parseFloat(obj.get("id_userR").toString());
                r.setIdUserr((int)idUserr);
                float idUsers = Float.parseFloat(obj.get("id_userS").toString());
                r.setIdUsers((int)idUsers);
//                r.setPhoto(obj.get("photo").toString());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = formatter.parse(obj.get("date").toString());
                r.setDate(date);
                reclamationList.add(r);
            }
        }
        return reclamationList;
    }

    public boolean addReclamation(Reclamation r) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String url = Statics.BASE_URL + "/reclamation/json/new?cause=" + r.getCause()  + "&idUserr=" + r.getIdUserr() + "&idUsers=" + r.getIdUsers()  ;
    ConnectionRequest req = new ConnectionRequest(url);
    req.setPost(false);
    req.addResponseListener(new ActionListener<NetworkEvent>() {
        @Override
        public void actionPerformed(NetworkEvent evt) {
            resultOK = (req.getResponseCode() == 200|| req.getResponseCode()==201);
            req.removeResponseListener(this);
            if (resultOK) {
                Dialog.show("Success", "Réclamation ajoutée avec succès", "OK", null);
            } else {
                Dialog.show("Error", "Erreur lors de l'ajout de la réclamation", "OK", null);
            }
        }
    });
    NetworkManager.getInstance().addToQueueAndWait(req);
    return resultOK;
}
    
    public boolean editReclamation(Reclamation r) {
    String url = Statics.BASE_URL + "/reclamation/edit/" + r.getId();
    req.setUrl(url);
    req.setPost(false);
    req.addArgument("cause", r.getCause());
    req.addArgument("idUserr", String.valueOf(r.getIdUserr()));
    req.addArgument("idUsers", String.valueOf(r.getIdUsers()));
    req.addResponseListener(new ActionListener<NetworkEvent>() {
        @Override
        public void actionPerformed(NetworkEvent evt) {
            resultOK = (req.getResponseCode() == 200||req.getResponseCode() == 201||req.getResponseCode() == 203);
            req.removeResponseListener(this); 
        }
    });
    NetworkManager.getInstance().addToQueueAndWait(req);
    return resultOK;
}
    
    public void deleteReclamation(int id) {
    Dialog d = new Dialog();
    if (d.show("Delete Reclamation", "Do you really want to remove this Reclamation?", "Yes", "No")) {
        req.setUrl(Statics.BASE_URL + "/reclamation/json/delete?id=" + id);
        NetworkManager.getInstance().addToQueueAndWait(req);
        d.dispose();
    }
}


}
