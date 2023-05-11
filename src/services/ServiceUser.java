/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.ui.TextField;
import com.codename1.ui.util.Resources;
import gui.SessionManager;
import java.io.IOException;
import java.util.Map;
import com.codename1.ui.Dialog;
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
    public void signup(String nomValue, String prenomValue, String emailValue, String phoneNumberValue, String villeValue, String filePath, String passwordValue) {
        
     
        
        String url = Statics.BASE_URL + "/json/signup?email=" + emailValue +
            "&nom=" + nomValue +
            "&prenom=" + prenomValue +
            "&numTel=" + phoneNumberValue +
            "&ville=" + villeValue +
            "&photo=" + filePath +
            "&pwd=" + passwordValue;
        
        req.setUrl(url);
        req.setPost(false);
       
//        req.addResponseListener((e)-> {
//         
//            //njib data ly7atithom fi form 
//            byte[]data = (byte[]) e.getMetaData();//lazm awl 7aja n7athrhom ke meta data ya3ni na5o id ta3 kol textField 
//            String responseData = new String(data);//ba3dika na5o content 
//            
//            System.out.println("data ===>"+responseData);
//        }
//        );
        
        
        //ba3d execution ta3 requete ely heya url nestanaw response ta3 server.
        NetworkManager.getInstance().addToQueueAndWait(req);
        
    }
    
    
    //SignIn
    
    public void signin(TextField username,TextField password, Resources rs ) {
        
        
        String url = Statics.BASE_URL+"/user/signin?email="+username.getText().toString()+"&password="+password.getText().toString();
        req = new ConnectionRequest(url, false); //false ya3ni url mazlt matba3thtich lel server
        req.setUrl(url);
        
        req.addResponseListener((e) ->{
            
            JSONParser j = new JSONParser();
            
            String json = new String(req.getResponseData()) + "";
            
            
            if(json.equals("failed")) {
                Dialog.show("Echec d'authentification","Email ou mot de passe éronné","OK",null);
            }
            else {
                System.out.println("data =="+json);
                
                Map<String,Object> user = null;
                try {
                    user = j.parseJSON(new CharArrayReader(json.toCharArray()));
                } catch (IOException ex) {
                }

                
             
                //Session 
                float id = Float.parseFloat(user.get("id").toString());
                SessionManager.setId((int)id);//jibt id ta3 user ly3ml login w sajltha fi session ta3i
                
                SessionManager.setPassowrd(user.get("password").toString());
                SessionManager.setUserName(user.get("username").toString());
                SessionManager.setEmail(user.get("email").toString());
                
                //photo 
                
                if(user.get("photo") != null)
                    SessionManager.setPhoto(user.get("photo").toString());
                
            
            
            
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
