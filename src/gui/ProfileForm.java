/*
 * Copyright (c) 2016, Codename One
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions 
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */
package gui;

import com.codename1.components.ScaleImageLabel;
import com.codename1.ui.Button;
import com.codename1.ui.CheckBox;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Component;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import entities.User;
import services.ServiceUser;

/**
 * The user profile form
 *
 * @author Shai Almog
 */
public class ProfileForm extends BaseForm {

    public ProfileForm(Resources res) {
        super("Newsfeed", BoxLayout.y());
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Profile");
        getContentPane().setScrollVisible(false);

        super.addSideMenu(res);

        tb.addSearchCommand(e -> {
        });

        Image img = res.getImage("profile-background.jpg");
        if (img.getHeight() > Display.getInstance().getDisplayHeight() / 3) {
            img = img.scaledHeight(Display.getInstance().getDisplayHeight() / 3);
        }
        ScaleImageLabel sl = new ScaleImageLabel(img);
        sl.setUIID("BottomPad");
        sl.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);

        add(LayeredLayout.encloseIn(
                sl,
                BorderLayout.south(
                        GridLayout.encloseIn(3,
                                FlowLayout.encloseCenter(
                                        new Label(res.getImage("profile-pic.jpg"), "PictureWhiteBackgrond"))
                        )
                )
        ));

        TextField prenom = new TextField(SessionManager.getUserName().substring(0, SessionManager.getUserName().indexOf(" ")));
        prenom.setUIID("TextFieldBlack");
        addStringValue("Prenom", prenom);
        TextField nom = new TextField(SessionManager.getUserName().substring(SessionManager.getUserName().indexOf(" ") + 1));
        nom.setUIID("TextFieldBlack");
        addStringValue("Nom", nom);

        TextField email = new TextField(SessionManager.getEmail(), "E-Mail", 20, TextField.EMAILADDR);
        email.setUIID("TextFieldBlack");
        email.setEditable(false);
        addStringValue("E-Mail", email);

        ComboBox<String> cities = new ComboBox<>("Ariana", "Beja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba", "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine", "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan");
        cities.setSelectedItem(SessionManager.getVille()); // Set the default selected item to the user's "ville"
        add(cities);
        
        TextField numTel = new TextField(SessionManager.getNumTel(), "Numero de téléphone");
        numTel.setUIID("TextFieldBlack");
        addStringValue("Numero de téléphone", numTel);
        
        TextField valeurFidelite = new TextField(Integer.toString(SessionManager.getValeurFidelite()), "Points de fidélité");
        valeurFidelite.setUIID("TextFieldBlack");
        valeurFidelite.setEditable(false);
        addStringValue("Points de fidélité", valeurFidelite);
        Button modifierBtn = new Button("Modifier");
    modifierBtn.addActionListener(e -> {
        // Create a User object with updated values
        User updatedUser = new User();
        updatedUser.setId(SessionManager.getId());
        updatedUser.setNom(nom.getText());
        updatedUser.setPrenom(prenom.getText());
        updatedUser.setVille(cities.getSelectedItem());
        updatedUser.setNumTel(numTel.getText());

        // Call the editUser function to update the user
        System.out.println("siuuuuuuu   "+updatedUser.getId());
        boolean success = ServiceUser.getInstance().editUser(updatedUser);
        if (success) {
            // Show a success message or perform any additional actions
            SessionManager.setUserName(prenom.getText()+" "+nom.getText());
            SessionManager.setVille(cities.getSelectedItem());
            SessionManager.setNumTel(numTel.getText());
            
            Dialog.show("Success", "User updated successfully", "OK", null);
        } else {
            // Show an error message or handle the error case
            Dialog.show("Error", "Failed to update user", "OK", null);
        }
    });

    add(modifierBtn);
    }

    private void addStringValue(String s, Component v) {
        add(BorderLayout.west(new Label(s, "PaddedLabel")).
                add(BorderLayout.CENTER, v));
        add(createLineSeparator(0xeeeeee));
    }
}
