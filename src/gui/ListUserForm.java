/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.BOTTOM;
import static com.codename1.ui.Component.CENTER;
import static com.codename1.ui.Component.LEFT;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Tabs;
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
import java.util.ArrayList;

/**
 *
 * @author Lenovo
 */
public class ListUserForm extends BaseForm {

    Form current;
    private ArrayList<User> allReclamations; // Store all users
    private ArrayList<User> filteredReclamations; // Store filtered users
    private TextField searchField; // Add this field
    private Container reclamationContainer;

    public ListUserForm(Resources res) {
        super("Utilisateurs", BoxLayout.y()); //herigate men Newsfeed w l formulaire vertical
        Toolbar tb = new Toolbar(true);
        current = this;
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Utilisateurs");
        getContentPane().setScrollVisible(false);
        addSideMenu(res);

        tb.addSearchCommand(e -> {

        });

        Tabs swipe = new Tabs();

        Label s1 = new Label();
        Label s2 = new Label();

        addTab(swipe, s1, res.getImage("back-logo.jpeg"), "", "", res);

        swipe.setUIID("Container");
        swipe.getContentPane().setUIID("Container");
        swipe.hideTabs();

        ButtonGroup bg = new ButtonGroup();
        int size = Display.getInstance().convertToPixels(1);
        Image unselectedWalkthru = Image.createImage(size, size, 0);
        Graphics g = unselectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAlpha(100);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        Image selectedWalkthru = Image.createImage(size, size, 0);
        g = selectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        RadioButton[] rbs = new RadioButton[swipe.getTabCount()];
        FlowLayout flow = new FlowLayout(CENTER);
        flow.setValign(BOTTOM);
        Container radioContainer = new Container(flow);
        for (int iter = 0; iter < rbs.length; iter++) {
            rbs[iter] = RadioButton.createToggle(unselectedWalkthru, bg);
            rbs[iter].setPressedIcon(selectedWalkthru);
            rbs[iter].setUIID("Label");
            radioContainer.add(rbs[iter]);
        }

        rbs[0].setSelected(true);
        swipe.addSelectionListener((i, ii) -> {
            if (!rbs[ii].isSelected()) {
                rbs[ii].setSelected(true);
            }
        });

        Component.setSameSize(radioContainer, s1, s2);
        add(LayeredLayout.encloseIn(swipe, radioContainer));

        allReclamations = ServiceUser.getInstance().getAllUsers();
        filteredReclamations = new ArrayList<>(allReclamations);
        System.out.println("\nles resulats sont::::::::" + allReclamations);
        this.setScrollableY(true);
        // Create a container to hold the reclamations
        reclamationContainer = new Container();
        reclamationContainer.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        // Add each reclamation to the container with buttons to edit and delete it
        for (User user : allReclamations) {
            // Create a container to hold the reclamation's information and buttons
            Container reclamationRow = new Container(new BorderLayout());
            reclamationRow.setUIID("ReclamationBox");

            // Create labels to display the user's information
            Label idLabel = new Label("ID: " + user.getId());
            idLabel.setUIID("UserLabel");
            Label nomLabel = new Label("Nom: " + user.getNom());
            nomLabel.setUIID("UserLabel");
            Label prenomLabel = new Label("Prénom: " + user.getPrenom());
            prenomLabel.setUIID("UserLabel");
            Label emailLabel = new Label("Email: " + user.getEmail());
            emailLabel.setUIID("UserLabel");
            Label numTelLabel = new Label("Numéro de téléphone: " + user.getNumTel());
            numTelLabel.setUIID("UserLabel");
            Label villeLabel = new Label("Ville: " + user.getVille());
            villeLabel.setUIID("UserLabel");
            Label valeurFideliteLabel = new Label("Valeur de fidélité: " + user.getValeurFidelite());
            valeurFideliteLabel.setUIID("UserLabel");
            Label roleLabel = new Label("Role: " + user.getRole());
            roleLabel.setUIID("UserLabel");
            Label dateLabel = new Label("Date: " + user.getDate());
            dateLabel.setUIID("UserLabel");
            Label etatLabel = new Label("Etat: " + user.getEtat());
            etatLabel.setUIID("UserLabel");

            // Create buttons to edit and delete the reclamation
            Button editBtn = new Button("Edit");
            editBtn.addActionListener(e -> {
                //   new EditReclamationForm(reclamation).show();
            });
            Button deleteBtn = new Button("Delete");
            deleteBtn.addActionListener(e -> {
                // Delete the reclamation from the server
                reclamationContainer.removeComponent(reclamationRow);
                ServiceUser.getInstance().deleteUser(user.getId());

                // Remove the reclamation from the container
            });

            // Add the labels to the reclamation row
            Container labelsContainer = new Container(new GridLayout(7, 1));
            labelsContainer.add(idLabel);
            labelsContainer.add(nomLabel);
            labelsContainer.add(emailLabel);
            labelsContainer.add(etatLabel);
            labelsContainer.add(dateLabel);

            reclamationRow.add(BorderLayout.CENTER, labelsContainer);

            // Create a container to hold the buttons
            Container buttonsContainer = new Container(new GridLayout(2, 1));
            buttonsContainer.setUIID("ReclamationButtonBox");
            //buttonsContainer.add(editBtn);
            buttonsContainer.add(deleteBtn);

            reclamationRow.add(BorderLayout.EAST, buttonsContainer);

            // Add the reclamation row to the container
            reclamationContainer.add(reclamationRow);
        }

        // Add the container to the form
        add(reclamationContainer);

        // Create the search field
        searchField = new TextField("", "Search");
        searchField.addDataChangeListener((i, ii) -> {
            String searchQuery = searchField.getText();

            filterUsers(searchQuery);
        });

        tb.setTitleComponent(searchField);

    }

    private void filterUsers(String searchQuery) {
        reclamationContainer.removeAll();
        filteredReclamations.clear();

        if (searchQuery.isEmpty()) {
            filteredReclamations.addAll(allReclamations);
        } else {
            String lowercaseQuery = searchQuery.toLowerCase();

            for (User user : allReclamations) {
                if (userMatchesQuery(user, lowercaseQuery)) {
                    filteredReclamations.add(user);
                }
            }
        }

        for (User user : filteredReclamations) {
            Container reclamationRow = createUserContainer(user);
            reclamationContainer.add(reclamationRow);
        }

        revalidate();
        repaint();
    }

    private boolean userMatchesQuery(User user, String searchQuery) {
        String lowercasedNom = user.getNom().toLowerCase();
        String lowercasedPrenom = user.getPrenom().toLowerCase();
        String lowercasedEmail = user.getEmail().toLowerCase();

        return lowercasedNom.contains(searchQuery)
                || lowercasedPrenom.contains(searchQuery)
                || lowercasedEmail.contains(searchQuery);
    }

    private Container createUserContainer(User user) {
        Container reclamationRow = new Container(new BorderLayout());
        reclamationRow.setUIID("ReclamationBox");

        // Create labels to display the user's information
        Label idLabel = new Label("ID: " + user.getId());
        idLabel.setUIID("UserLabel");
        Label nomLabel = new Label("Nom: " + user.getNom());
        nomLabel.setUIID("UserLabel");
        Label emailLabel = new Label("Email: " + user.getEmail());
        emailLabel.setUIID("UserLabel");
        // Add more labels for other user information

        // Create a container to hold the labels
        Container labelsContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        labelsContainer.add(idLabel);
        labelsContainer.add(nomLabel);
        labelsContainer.add(emailLabel);
        // Add more labels to the container

        // Create a container to hold the buttons
        Container buttonsContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        // Create buttons and add them to the container

        // Add the labels container and buttons container to the reclamation row
        reclamationRow.add(BorderLayout.CENTER, labelsContainer);
        reclamationRow.add(BorderLayout.EAST, buttonsContainer);

        return reclamationRow;
    }

    private void addTab(Tabs swipe, Label spacer, Image image, String string, String text, Resources res) {
        int size = Math.min(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());

        if (image.getHeight() < size) {
            image = image.scaledHeight(size);
        }

        if (image.getHeight() > Display.getInstance().getDisplayHeight() / 2) {
            image = image.scaledHeight(Display.getInstance().getDisplayHeight() / 2);
        }

        ScaleImageLabel imageScale = new ScaleImageLabel(image);
        imageScale.setUIID("Container");
        imageScale.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);

        Label overLay = new Label("", "ImageOverlay");

        Container page1
                = LayeredLayout.encloseIn(
                        imageScale,
                        overLay,
                        BorderLayout.south(
                                BoxLayout.encloseY(
                                        new SpanLabel(text, "LargeWhiteText"),
                                        spacer
                                )
                        )
                );

        swipe.addTab("", res.getImage("back-logo.jpeg"), page1);

    }

    public void bindButtonSelection(Button btn, Label l) {

        btn.addActionListener(e -> {
            if (btn.isSelected()) {
                updateArrowPosition(btn, l);
            }
        });
    }

    private void updateArrowPosition(Button btn, Label l) {

        l.getUnselectedStyle().setMargin(LEFT, btn.getX() + btn.getWidth() / 2 - l.getWidth() / 2);
        l.getParent().repaint();
    }

}
