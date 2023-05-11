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

import com.codename1.capture.Capture;
import com.codename1.components.FloatingHint;
import com.codename1.io.FileSystemStorage;
import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.Resources;
import services.ServiceUser;
import java.util.Vector;
import javafx.stage.FileChooser;

/**
 * Signup UI
 *
 * @author ZeroSTF
 */
public class SignUpForm extends BaseForm {

    public SignUpForm(Resources res) {
        super(new BorderLayout());
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        tb.setUIID("Container");
        getTitleArea().setUIID("Container");
        Form previous = Display.getInstance().getCurrent();
        tb.setBackCommand("", e -> previous.showBack());
        setUIID("SignIn");

        TextField nom = new TextField("", "Nom", 20, TextField.ANY);
        TextField prenom = new TextField("", "Prenom", 20, TextField.ANY);
        TextField email = new TextField("", "E-Mail", 20, TextField.EMAILADDR);
        TextField phoneNumber = new TextField("", "Numero de Telephone", 8, TextField.PHONENUMBER);
        ComboBox<String> cities = new ComboBox<>("Ariana", "Beja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba", "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine", "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan");
        Button browse = new Button("Parcourir");
        browse.addActionListener(e -> {
            if (Capture.hasCamera()) {
                Capture.capturePhoto(photo -> {
                    if (photo != null) {
                        String filePath = FileSystemStorage.getInstance().getAppHomePath() + photo;
                        browse.setText(filePath.substring(Math.max(0, filePath.length() - 15)));
                    }
                });
            } else {
                Display.getInstance().openGallery(e2 -> {
                    if (e2 != null && e2.getSource() != null) {
                        String filePath = (String) e2.getSource();
                        browse.setText(filePath.substring(Math.max(0, filePath.length() - 15)));
                    }
                }, Display.GALLERY_IMAGE);
            }
        });

        TextField password = new TextField("", "Password", 20, TextField.PASSWORD);
        TextField confirmPassword = new TextField("", "Confirm Password", 20, TextField.PASSWORD);

        nom.setSingleLineTextArea(false);
        prenom.setSingleLineTextArea(false);
        email.setSingleLineTextArea(false);
        phoneNumber.setSingleLineTextArea(false);
        password.setSingleLineTextArea(false);
        confirmPassword.setSingleLineTextArea(false);
        Button next = new Button("SignUp");
        Button signIn = new Button("Sign In");
        signIn.addActionListener(e -> new SignInForm(res).show());
        signIn.setUIID("Link");
        Label alreadyHaveAnAccount = new Label("Vous avez déjà un compte ?");

        Container content = BoxLayout.encloseY(
                new Label("Inscription", "LogoLabel"),
                new FloatingHint(nom),
                createLineSeparator(),
                new FloatingHint(prenom),
                createLineSeparator(),
                new FloatingHint(email),
                createLineSeparator(),
                new FloatingHint(phoneNumber),
                createLineSeparator(),
                cities,
                createLineSeparator(),
                browse,
                createLineSeparator(),
                new FloatingHint(password),
                createLineSeparator(),
                new FloatingHint(confirmPassword)
        );
        content.setScrollableY(true);
        add(BorderLayout.CENTER, content);
        add(BorderLayout.SOUTH, BoxLayout.encloseY(
                next,
                FlowLayout.encloseCenter(alreadyHaveAnAccount, signIn)
        ));
        next.requestFocus();
        next.addActionListener((e) -> {
            String filePath = browse.getText();
            if (filePath.isEmpty()) {
                Dialog.show("Error", "Please select an image", "OK", null);
                return;
            }

            String nomValue = nom.getText();
            String prenomValue = prenom.getText();
            String emailValue = email.getText();
            String phoneNumberValue = phoneNumber.getText();
            String villeValue = cities.getSelectedItem();
            String passwordValue = password.getText();
            String confirmPasswordValue = confirmPassword.getText();

            if (nomValue.isEmpty() || prenomValue.isEmpty() || emailValue.isEmpty() || phoneNumberValue.isEmpty()
                    || villeValue.isEmpty() || passwordValue.isEmpty() || confirmPasswordValue.isEmpty()) {
                Dialog.show("Error", "Please fill in all the fields", "OK", null);
                return;
            }

            if (!passwordValue.equals(confirmPasswordValue)) {
                Dialog.show("Error", "Passwords do not match", "OK", null);
                return;
            }

            // Perform signup logic here
            // You can access the form field values using the respective variables
            // (nomValue, prenomValue, emailValue, phoneNumberValue, villeValue, filePath, passwordValue, confirmPasswordValue)
            // Example code to display the field values
            System.out.println("Nom: " + nomValue);
            System.out.println("Prenom: " + prenomValue);
            System.out.println("E-Mail: " + emailValue);
            System.out.println("Numero de Telephone: " + phoneNumberValue);
            System.out.println("Ville: " + villeValue);
            System.out.println("Photo: " + filePath);
            System.out.println("Password: " + passwordValue);
            System.out.println("Confirm Password: " + confirmPasswordValue);
            ServiceUser.getInstance().signup(nomValue, prenomValue, emailValue, phoneNumberValue, villeValue, filePath, passwordValue);

            // Call your service or API to save the user's signup data
            Dialog.show("Success", "Account is saved", "OK", null);
            new SignInForm(res).show();
        });
    }

}
