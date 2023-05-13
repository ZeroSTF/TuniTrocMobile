package gui;

import com.codename1.components.MultiButton;
import entities.User;
import services.ServiceUser;
import com.codename1.components.ToastBar;
import com.codename1.googlemaps.MapContainer;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.maps.Coord;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Style;
import java.io.IOException;
import java.util.List;


import java.util.ArrayList;

public class MapForm extends Form {
    Form f = new Form();
  MapContainer cnt = null;

    public MapForm() {
        super("Users Per Month", new BorderLayout());

         try{
        cnt = new MapContainer("AIzaSyCy-fMWerzvXcPCV0FDI07hW2DAzs_mnpY");
    }catch(Exception ex) {
        ex.printStackTrace();
    }
        Style s = new Style();
        s.setFgColor(0xff0000);
        s.setBgTransparency(0);

        // Get the list of users
        ArrayList<User> userList = ServiceUser.getInstance().getAllUsers();

        // Iterate over the user list and mark their locations on the map
        for (User user : userList) {
            String cityName = user.getVille();
            Coord coord = getCoordinatesForCity(cityName);
            if (coord != null) {
                // Add a marker to the map for each user's location
               cnt.addMarker(
                        createMarkerImage(user),
                        coord // Coordinates for the city

                );
            }
        }

        // Set the initial zoom level and center the map
        cnt.zoom(new Coord(34, 9), 12);
        cnt.setCameraPosition(new Coord(34, 9));
    }

    private Coord getCoordinatesForCity(String cityName) {
        // Implement your logic to retrieve the coordinates for the given city
        // You can use a geocoding service or a custom lookup table based on the city name
        // Return null if the coordinates are not available
        // Example: Using a hardcoded lookup table
        switch (cityName) {
            case "Tunis":
                return new Coord(36.8065, 10.1815);
            case "Sfax":
                return new Coord(34.7399, 10.7601);
            // Add more city coordinates as needed
            default:
                return null;
        }
    }

    private Component createMarkerImage(User user) {
        // Create a custom marker component using MultiButton or any other suitable component
        MultiButton marker = new MultiButton(user.getPrenom() + " " + user.getNom());
        marker.setTextLine2(user.toString());
        marker.setUIID("Marker");
        return marker;
    }
}


