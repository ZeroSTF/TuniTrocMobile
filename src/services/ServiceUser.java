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
import com.codename1.ui.TextField;
import com.codename1.ui.util.Resources;
import gui.SessionManager;
import java.io.IOException;
import java.util.Map;
import com.codename1.ui.Dialog;
import gui.NewsfeedForm;
import gui.SignInForm;
import utils.Statics;

/**
 *
 * @author ZeroS TF
 */
public class ServiceUser {
    public static ServiceUser instance = null ;
    
    public static boolean resultOk = true;
    String json;

    //initilisation connection request 
    private ConnectionRequest req;
    
    
    public static ServiceUser getInstance() {
        if(instance == null )
            instance = new ServiceUser();
        return instance ;
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
    
    public void signin(TextField username,TextField password, Resources rs ) {
        
        
        String url = Statics.BASE_URL+"/json/signin?email="+username.getText().toString()+"&password="+password.getText().toString();
        req = new ConnectionRequest(url, false); //false ya3ni url mazlt matba3thtich lel server
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener((e) ->{
            
            JSONParser j = new JSONParser();
            
            String json = new String(req.getResponseData()) + "";
            
            
            if(json.equals("failed")) {
                Dialog.show("Echec d'authentification","Email ou mot de passe éronné","OK",null);
            }
            else {
              //  System.out.println("data =="+json);
                
                Map<String,Object> user = null;
                try {
                    user = j.parseJSON(new CharArrayReader(json.toCharArray()));
                } catch (IOException ex) {
                }

                
             
                //Session 
                float id = Float.parseFloat(user.get("id").toString());
                SessionManager.setId((int)id);//jibt id ta3 user ly3ml login w sajltha fi session ta3i
                
                SessionManager.setPassowrd(user.get("pwd").toString());
                SessionManager.setUserName(user.get("prenom").toString()+" "+user.get("nom").toString());
                SessionManager.setEmail(user.get("email").toString());
                SessionManager.setisAdmin(Boolean.parseBoolean(user.get("role").toString()));
                System.out.println(SessionManager.getIsAdmin());
                
                //photo 
                
                if(user.get("photo") != null)
                    SessionManager.setPhoto(user.get("photo").toString());
                
                new NewsfeedForm(rs).show();
                
            
            
            
        }});

        NetworkManager.getInstance().addToQueueAndWait(req);
    }
    
    public String getPasswordByEmail(String email, Resources rs ) {
        
        
        String url = Statics.BASE_URL+"/user/getPasswordByEmail?email="+email;
        req = new ConnectionRequest(url, false); 
        req.setUrl(url);
        
        req.addResponseListener((e) ->{
            
            JSONParser j = new JSONParser();
            
             json = new String(req.getResponseData()) + "";
            try {
                System.out.println("data =="+json);
                Map<String,Object> password = j.parseJSON(new CharArrayReader(json.toCharArray()));
            }catch(Exception ex) {
                ex.printStackTrace();
            }
        });

        NetworkManager.getInstance().addToQueueAndWait(req);
    return json;
    }
    
}
