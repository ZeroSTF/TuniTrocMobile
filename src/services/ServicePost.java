/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import com.codename1.util.regex.StringReader;
import entities.Post;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import utils.Statics;

/**
 *
 * @author ZeroS TF
 */
public class ServicePost {
    public static ServicePost instance = null ;
    
    public static boolean resultOk = true;
    String json;

    //initilisation connection request 
    private ConnectionRequest req;
    
    
    public static ServicePost getInstance() {
        if(instance == null )
            instance = new ServicePost();
        return instance ;
    }
  
    public ServicePost() {
        req = new ConnectionRequest();
    }
    
    public ArrayList<Post> getAllPosts() {
    ArrayList<Post> postsList = new ArrayList<>();
    String url = Statics.BASE_URL + "/json/afficherPost";
    ConnectionRequest req = new ConnectionRequest(url);
    req.setPost(false);

    if (req != null) {
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    ArrayList<Post> parsedList = parsePosts(new String(req.getResponseData()));
                    postsList.addAll(parsedList);
                } catch (ParseException | java.text.ParseException | IOException ex) {
                    ex.printStackTrace();
                }
                req.removeResponseListener(this);
            }
        });
    }
    NetworkManager.getInstance().addToQueueAndWait(req);
    return postsList;
}

public ArrayList<Post> parsePosts(String jsonText) throws ParseException, java.text.ParseException, IOException {
    ArrayList<Post> postList = new ArrayList<>();
    if (jsonText != null && !jsonText.isEmpty()) {
        JSONParser j = new JSONParser();
        Map<String, Object> postListJson = j.parseJSON(new StringReader(jsonText));
        List<Map<String, Object>> list = (List<Map<String, Object>>) postListJson.get("root");
        for (Map<String, Object> obj : list) {
            Post p = new Post();
            float id = Float.parseFloat(obj.get("idPost").toString());
            p.setIdPost((int) id);
            p.setDescription(obj.get("description").toString());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(obj.get("dateP").toString());
            p.setDateP(date);
            p.setImage(obj.get("image").toString());
            float idUser = Float.parseFloat(obj.get("idUser").toString());
            p.setIdUser((int) idUser);
            System.out.println("ID USER:::::::::"+idUser+"     "+(int) idUser);
            postList.add(p);
        }
    }
    return postList;
}

public boolean addPost(Post p) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String url = Statics.BASE_URL + "/JSON/ajouterPost";
    ConnectionRequest req = new ConnectionRequest(url);
    req.setPost(true);
    req.addArgument("description", p.getDescription());
    req.addArgument("idUser", String.valueOf(p.getIdUser()));
    req.addResponseListener(new ActionListener<NetworkEvent>() {
        @Override
        public void actionPerformed(NetworkEvent evt) {
            resultOk = req.getResponseCode() == 200;
            req.removeResponseListener(this);
            if (resultOk) {
                Dialog.show("Success", "Post added successfully", "OK", null);
            } else {
                Dialog.show("Error", "Error adding the post", "OK", null);
            }
        }
    });
    NetworkManager.getInstance().addToQueueAndWait(req);
    return resultOk;
}

public boolean editPost(Post post) {
    String url = Statics.BASE_URL + "/JSON/modifPost?id=" + post.getIdPost();
    ConnectionRequest req = new ConnectionRequest(url);
    req.setPost(false);
    req.addArgument("description", post.getDescription());
    req.addArgument("idUser", String.valueOf(post.getIdUser()));
    req.addResponseListener(new ActionListener<NetworkEvent>() {
        @Override
        public void actionPerformed(NetworkEvent evt) {
            resultOk = req.getResponseCode() == 200;
            req.removeResponseListener(this);
        }
    });
    NetworkManager.getInstance().addToQueueAndWait(req);
    return resultOk;
}
public void deletePost(int id) {
    Dialog d = new Dialog();
    if (d.show("Delete Post", "Do you really want to remove this Post?", "Yes", "No")) {
        ConnectionRequest req = new ConnectionRequest();
        req.setUrl(Statics.BASE_URL + "/JSON/suppPost?id=" + id);
        NetworkManager.getInstance().addToQueueAndWait(req);
        d.dispose();
    }
}
    
}
