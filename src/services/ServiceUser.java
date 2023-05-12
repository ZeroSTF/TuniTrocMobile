/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.MultipartRequest;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.TextField;
import com.codename1.ui.util.Resources;
import gui.SessionManager;
import java.io.IOException;
import java.util.Map;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
import com.codename1.util.regex.StringReader;
import entities.User;
import gui.NewsfeedForm;
import gui.SignInForm;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import utils.Statics;

/**
 *
 * @author ZeroS TF
 */
public class ServiceUser {

    public static ServiceUser instance = null;

    public static boolean resultOk = true;
    String json;

    //initilisation connection request 
    private ConnectionRequest req;

    public static ServiceUser getInstance() {
        if (instance == null) {
            instance = new ServiceUser();
        }
        return instance;
    }

    public ServiceUser() {
        req = new ConnectionRequest();

    }

    //Signup
    public void signup(String nomValue, String prenomValue, String emailValue, String phoneNumberValue, String villeValue, String filePath, String passwordValue, Resources rs) {
        String url = Statics.BASE_URL + "/json/signup";

        MultipartRequest req = new MultipartRequest();
        req.setUrl(url);

        // Add form fields
        req.addArgument("email", emailValue);
        req.addArgument("nom", nomValue);
        req.addArgument("prenom", prenomValue);
        req.addArgument("numTel", phoneNumberValue);
        req.addArgument("ville", villeValue);
        req.addArgument("pwd", passwordValue);
        req.addArgument("photo", filePath);

        // Set response listener
        req.addResponseListener(e -> {
            NetworkEvent event = (NetworkEvent) e;
            int responseCode = event.getResponseCode();
            if (responseCode == 200) {
                Dialog.show("Success", "Account is saved", "OK", null);
                new SignInForm(rs).show();
            } else {
                // Handle error case
                Dialog.show("Error", "Failed to save account", "OK", null);
            }
        });

        // Make the network request
        NetworkManager.getInstance().addToQueue(req);
    }

    //SignIn
    public void signin(TextField username, TextField password, Resources rs) {

        String url = Statics.BASE_URL + "/json/signin?email=" + username.getText().toString() + "&password=" + password.getText().toString();
        req = new ConnectionRequest(url, false); //false ya3ni url mazlt matba3thtich lel server
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener((e) -> {

            JSONParser j = new JSONParser();

            String json = new String(req.getResponseData()) + "";
            System.out.println(json);

            if (json.equals("failed")) {
                Dialog.show("Echec d'authentification", "Email ou mot de passe éronné", "OK", null);
            } else {
                  System.out.println("data =="+json);

                Map<String, Object> user = null;
                try {
                    user = j.parseJSON(new CharArrayReader(json.toCharArray()));
                } catch (IOException ex) {
                }

                //Session 
                float id = Float.parseFloat(user.get("id").toString());
                SessionManager.setId((int) id);

                SessionManager.setPassowrd(user.get("pwd").toString());
                SessionManager.setUserName(user.get("prenom").toString() + " " + user.get("nom").toString());
                SessionManager.setEmail(user.get("email").toString());
                SessionManager.setisAdmin(Boolean.parseBoolean(user.get("role").toString()));
                System.out.println(SessionManager.getIsAdmin());

                //photo 
                if (user.get("photo") != null) {
                    SessionManager.setPhoto(user.get("photo").toString());
                }

                new NewsfeedForm(rs).show();

            }
        });

        NetworkManager.getInstance().addToQueueAndWait(req);
    }

    public String getPasswordByEmail(String email, Resources rs) {
        String url = Statics.BASE_URL + "/user/getPasswordByEmail?email=" + email;
        req = new ConnectionRequest(url, false);
        req.setUrl(url);

        req.addResponseListener((e) -> {

            JSONParser j = new JSONParser();

            json = new String(req.getResponseData()) + "";
            try {
                System.out.println("data ==" + json);
                Map<String, Object> password = j.parseJSON(new CharArrayReader(json.toCharArray()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        NetworkManager.getInstance().addToQueueAndWait(req);
        return json;
    }

    public void ajoutUser(User user) {
        String url = Statics.BASE_URL + "/addUser?email=" + user.getEmail() + "&pwd=" + user.getPwd() + "&nom=" + user.getNom() + "&prenom=" + user.getPrenom() + "&photo=" + user.getPhoto() + "&numTel=" + user.getNumTel() + "&ville=" + user.getVille() + "&valeurFidelite=" + user.getValeurFidelite() + "&role=" + user.getRole() + "&date=" + user.getDate() + "&etat=" + user.getEtat();
        req.setUrl(url);

        req.addResponseListener((e) -> {
            String str = new String(req.getResponseData());
            System.out.println("data == " + str);
        });

        NetworkManager.getInstance().addToQueueAndWait(req);
    }

    public ArrayList<User> affichageUsers() {
        ArrayList<User> result = new ArrayList<>();

        String url = Statics.BASE_URL + "/json/afficherUser";
        req.setUrl(url);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                JSONParser jsonp = new JSONParser();
                try {
                    Map<String, Object> mapUsers = jsonp.parseJSON(new CharArrayReader(new String(req.getResponseData()).toCharArray()));

                    List<Map<String, Object>> listOfMaps = (List<Map<String, Object>>) mapUsers.get("root");

                    for (Map<String, Object> obj : listOfMaps) {
                        User user = new User();

                        float id = Float.parseFloat(obj.get("id").toString());
                        String email = obj.get("email").toString();
                        String pwd = obj.get("pwd").toString();
                        String nom = obj.get("nom").toString();
                        String prenom = obj.get("prenom").toString();
                        String photo = obj.get("photo").toString();
                        String numTel = obj.get("numTel").toString();
                        String ville = obj.get("ville").toString();
                        int valeurFidelite = Integer.parseInt(obj.get("valeurFidelite").toString());
                        Boolean role = Boolean.parseBoolean(obj.get("role").toString());
                        String dateString = obj.get("date").toString(); // Assuming the date is already formatted correctly
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                        String etat = obj.get("etat").toString();
                        user.setId((int) id);
                        user.setEmail(email);
                        user.setPwd(pwd);
                        user.setNom(nom);
                        user.setPrenom(prenom);
                        user.setPhoto(photo);
                        user.setNumTel(numTel);
                        user.setVille(ville);
                        user.setValeurFidelite(valeurFidelite);
                        user.setRole(role);
                        user.setDate(date);
                        user.setEtat(etat);
                        result.add(user);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        System.out.println("\nles resulats sont::::::::"+result);
        return result;
    }

    
    public ArrayList<User> parseUsers(String jsonText) throws ParseException, java.text.ParseException, IOException {
    ArrayList<User> userList = new ArrayList<>();
    if (jsonText != null && !jsonText.isEmpty()) {
        JSONParser j = new JSONParser();
        Map<String, Object> userJson = j.parseJSON(new StringReader(jsonText));
         List<Map<String, Object>> list = (List<Map<String, Object>>) userJson.get("root");
        for(Map<String, Object> obj : list){
        float id1 = Float.parseFloat(obj.get("id").toString());
        int id = (int)id1;
        String email = obj.get("email").toString();
        String pwd = obj.get("pwd").toString();
        String nom = obj.get("nom").toString();
        String prenom = obj.get("prenom").toString();
        String numTel = obj.get("numTel").toString();
        String ville = obj.get("ville").toString();
        float vf = Float.parseFloat(obj.get("valeurFidelite").toString());
        int valeurFidelite = (int) vf;
        boolean role = Boolean.parseBoolean(obj.get("role").toString());
        String salt = obj.get("salt").toString();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(obj.get("date").toString());
        String etat = obj.get("etat").toString();
        String userIdentifier = obj.get("userIdentifier").toString();
        String username = obj.get("username").toString();
        String password = obj.get("password").toString();
        boolean verified = Boolean.parseBoolean(obj.get("verified").toString());
        //String photo = obj.get("photo").toString();
        List<String> roles = (List<String>) obj.get("roles");

        User u = new User(id, email, pwd, nom, prenom, numTel, ville, valeurFidelite, role, salt, date, etat, userIdentifier, username, password, verified, "", roles);
        userList.add(u);}
    }
    return userList;
}
    
    public ArrayList<User> getAllUsers() {
    ArrayList<User> userList = new ArrayList<>();
    String url = Statics.BASE_URL + "/json/afficherUser";
    ConnectionRequest req = new ConnectionRequest(url);
    req.setPost(false);

    if (req != null) {
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    ArrayList<User> parsedList = parseUsers(new String(req.getResponseData()));
                    userList.addAll(parsedList);
                } catch (ParseException | java.text.ParseException | IOException ex) {
                    ex.printStackTrace();
                }
                req.removeResponseListener(this);
            }
        });
    }
    NetworkManager.getInstance().addToQueueAndWait(req);
    return userList;
}


    // Detail User method
    public User detailUser(int id, User user) {
        String url = Statics.BASE_URL + "/detailUser?" + id;
        req.setUrl(url);

        req.addResponseListener((NetworkEvent evt) -> {
            JSONParser jsonp = new JSONParser();
            try {
                Map<String, Object> obj = jsonp.parseJSON(new CharArrayReader(new String(req.getResponseData()).toCharArray()));

                user.setId(Integer.parseInt(obj.get("id").toString()));
                user.setEmail(obj.get("email").toString());
                user.setPwd(obj.get("pwd").toString());
                user.setNom(obj.get("nom").toString());
                user.setPrenom(obj.get("prenom").toString());
                user.setPhoto(obj.get("photo").toString());
                user.setNumTel(obj.get("numTel").toString());
                user.setVille(obj.get("ville").toString());
                user.setValeurFidelite(Integer.parseInt(obj.get("valeurFidelite").toString()));
                user.setRole(Boolean.valueOf(obj.get("role").toString()));
                // Parse the date using a suitable method and set it to user.setDate()
                // ...
                user.setEtat(obj.get("etat").toString());
            } catch (IOException ex) {
                System.out.println("Error related to SQL: " + ex.getMessage());
            }

            System.out.println("Data === " + req.getResponseData());
        });

        NetworkManager.getInstance().addToQueueAndWait(req);
        return user;
    }
    
    //Delete 
    public boolean deleteUser(int id ) {
        String url = Statics.BASE_URL +"/json/suppUser?id="+id;
        
        req.setUrl(url);
        
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                    
                    req.removeResponseCodeListener(this);
            }
        });
        
        NetworkManager.getInstance().addToQueueAndWait(req);
        return  resultOk;
    }

}
