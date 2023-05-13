/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.BOTTOM;
import static com.codename1.ui.Component.CENTER;
import static com.codename1.ui.Component.LEFT;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
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
import entities.Post;
import entities.User;
import java.util.ArrayList;
import services.ServicePost;

/**
 *
 * @author ZeroS TF
 */
public class ListPostForm extends BaseForm {

    Form current;
    private ArrayList<Post> allReclamations; // Store all users
    private ArrayList<Post> filteredReclamations; // Store filtered users
    private TextField searchField; // Add this field
    private Container reclamationsContainer;

    public ListPostForm(Resources res) {
        super("Publications", BoxLayout.y()); //herigate men Newsfeed w l formulaire vertical
        Toolbar tb = new Toolbar(true);
        current = this;
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Publications");
        getContentPane().setScrollVisible(false);
        addSideMenu(res);
        searchField = new TextField("", "Search");
        searchField.addDataChangeListener((i, ii) -> {
            String searchQuery = searchField.getText();

            filterReclamations(searchQuery, res);
        });

        tb.setTitleComponent(searchField);

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

        ButtonGroup barGroup = new ButtonGroup();
        RadioButton mesListes = RadioButton.createToggle("Ajouter", barGroup);
        mesListes.setUIID("SelectBar");
        RadioButton liste = RadioButton.createToggle("...", barGroup);
        liste.setUIID("SelectBar");
        RadioButton partage = RadioButton.createToggle("Posts", barGroup);
        partage.setUIID("SelectBar");
        Label arrow = new Label(res.getImage("news-tab-down-arrow.png"), "Container");

        partage.addActionListener((e) -> {
            InfiniteProgress ip = new InfiniteProgress();
            final Dialog ipDlg = ip.showInifiniteBlocking();
            new ListPostForm(res).show();
            //////////ajouutttt
            //  ListPostForm a = new ListPostForm(res);
            //  a.show();
            refreshTheme();
        });
        mesListes.addActionListener((e) -> {
            InfiniteProgress ip = new InfiniteProgress();
            final Dialog ipDlg = ip.showInifiniteBlocking();
            new AddPostForm(res).show();
            //////////ajouutttt
            //  ListPostForm a = new ListPostForm(res);
            //  a.show();
            refreshTheme();
        });

        add(LayeredLayout.encloseIn(
                GridLayout.encloseIn(3, mesListes, liste, partage)
        ));

//        reclamations = ServicePost.getInstance().getAllPosts();
//        System.out.println("\nles resulats sont::::::::" + reclamations);
//        this.setScrollableY(true);
//        // Create a container to hold the reclamations
//        Container reclamationContainer = new Container();
//        reclamationContainer.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
//        // Add each reclamation to the container with buttons to edit and delete it
//        for (Post user : reclamations) {
//            // Create a container to hold the reclamation's information and buttons
//            Container reclamationRow = new Container(new BorderLayout());
//            reclamationRow.setUIID("ReclamationBox");
//
//            // Create labels to display the user's information
//            Label idLabel = new Label("ID: " + user.getIdPost());
//            idLabel.setUIID("UserLabel");
//            Label descriptionLabel = new Label("Description: " + user.getDescription());
//            descriptionLabel.setUIID("UserLabel");
//            Label dateLabel = new Label("Date: " + user.getDateP());
//            dateLabel.setUIID("UserLabel");
//           
//            Label idUserLabel = new Label("User ID: " + user.getIdUser());
//            idUserLabel.setUIID("UserLabel");
//
//            // Create buttons to edit and delete the reclamation
//            Button editBtn = new Button("Edit");
//            editBtn.addActionListener(e -> {
//                    
//                   new EditPostForm(res,user).show();
//            });
//            Button deleteBtn = new Button("Delete");
//            deleteBtn.addActionListener(e -> {
//                // Delete the reclamation from the server
//                ServicePost.getInstance().deletePost(user.getIdPost());
//
//                // Remove the reclamation from the container
//                reclamationContainer.removeComponent(reclamationRow);
//            });
//
//            // Add the labels to the reclamation row
//            Container labelsContainer = new Container(new GridLayout(5, 1));
//            labelsContainer.add(idLabel);
//            labelsContainer.add(descriptionLabel);
//            labelsContainer.add(dateLabel);
//            labelsContainer.add(idUserLabel);
//
//            reclamationRow.add(BorderLayout.CENTER, labelsContainer);
//
//            // Create a container to hold the buttons
//            Container buttonsContainer = new Container(new GridLayout(2, 1));
//            buttonsContainer.setUIID("ReclamationButtonBox");
//            buttonsContainer.add(editBtn);
//            buttonsContainer.add(deleteBtn);
//
//            reclamationRow.add(BorderLayout.EAST, buttonsContainer);
//
//            // Add the reclamation row to the container
//            reclamationContainer.add(reclamationRow);
//        }
//
//        // Add the container to the form
//        add(reclamationContainer);
        allReclamations = ServicePost.getInstance().getAllPosts();

        filteredReclamations = new ArrayList<>(allReclamations);

// Create container to hold the list of reclamations
        reclamationsContainer = new Container();
        reclamationsContainer.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        this.setScrollableY(true);

// Add each reclamation to the container with buttons to edit and delete it
        for (Post reclamation : filteredReclamations) {
            Container reclamationRow = createProductContainer(reclamation, res);
            reclamationsContainer.add(reclamationRow);
        }

// Add the container to the form
        add(reclamationsContainer);

    }

    private void filterReclamations(String searchQuery, Resources res) {
        reclamationsContainer.removeAll();
        filteredReclamations.clear();

        if (searchQuery.isEmpty()) {
            filteredReclamations.addAll(allReclamations);
        } else {
            // Filter products based on the search query
            for (Post product : allReclamations) {

                if (productMatchesQuery(product, searchQuery)) {
                    filteredReclamations.add(product);
                }
            }
        }

        for (Post product : filteredReclamations) {
            Container productRow = createProductContainer(product, res);
            reclamationsContainer.add(productRow);
        }

        revalidate();
        repaint();
    }

    private Container createProductContainer(Post product, Resources res) {
        Container productRow = new Container(new BorderLayout());
        productRow.setUIID("ProductBox");

//        // Create labels to display the product's information
//        Label idLabel = new Label("ID: " + product.getId());
//        idLabel.setUIID("ProductLabel");
//        Label typeLabel = new Label("Type: " + product.getType());
//        typeLabel.setUIID("ProductLabel");
//        Label categorieLabel = new Label("Categorie: " + product.getCategorie());
//        categorieLabel.setUIID("ProductLabel");
//        Label nomLabel = new Label("Nom: " + product.getNom());
//        nomLabel.setUIID("ProductLabel");
//        Label libelleLabel = new Label("Libelle: " + product.getLibelle());
//        libelleLabel.setUIID("ProductLabel");
//        Label prixLabel = new Label("Ville: " + product.getPrix());
//        prixLabel.setUIID("ProductLabel");
//        Label stockLabel = new Label("Stock: " + product.getStock());
//        stockLabel.setUIID("ProductLabel");
//
//        // Create buttons to edit and delete the product
//        Button editBtn = new Button("Edit");
//        editBtn.addActionListener(e -> {
//            new EditProductForm(res, product).show();
//        });
//        Button deleteBtn = new Button("Delete");
//        deleteBtn.addActionListener(e -> {
//            // Delete the product from the server
//            ServiceProduct.getInstance().deleteProduct(product.getId());
//
//            // Remove the product from the container
//            reclamationsContainer.removeComponent(productRow);
//        });
//
//        // Add the labels to the product row
//        Container labelsContainer = new Container(new GridLayout(7, 1));
//        labelsContainer.add(idLabel);
//        labelsContainer.add(typeLabel);
//        labelsContainer.add(categorieLabel);
//        labelsContainer.add(nomLabel);
//        labelsContainer.add(libelleLabel);
//        labelsContainer.add(prixLabel);
//        //  labelsContainer.add(stockLabel);
 // Create a container to hold the reclamation's information and buttons
            Container reclamationRow = new Container(new BorderLayout());
            reclamationRow.setUIID("ReclamationBox");

            // Create labels to display the user's information
            Label idLabel = new Label("ID: " + product.getIdPost());
            idLabel.setUIID("UserLabel");
            Label descriptionLabel = new Label("Description: " + product.getDescription());
            descriptionLabel.setUIID("UserLabel");
            Label dateLabel = new Label("Date: " + product.getDateP());
            dateLabel.setUIID("UserLabel");
           
            Label idUserLabel = new Label("User ID: " + product.getIdUser());
            idUserLabel.setUIID("UserLabel");

            // Create buttons to edit and delete the reclamation
            Button editBtn = new Button("Edit");
            editBtn.addActionListener(e -> {
                    
                   new EditPostForm(res,product).show();
            });
            Button deleteBtn = new Button("Delete");
            deleteBtn.addActionListener(e -> {
                // Delete the reclamation from the server
                ServicePost.getInstance().deletePost(product.getIdPost());

                // Remove the reclamation from the container
                reclamationsContainer.removeComponent(reclamationRow);
            });

            // Add the labels to the reclamation row
            Container labelsContainer = new Container(new GridLayout(5, 1));
            labelsContainer.add(idLabel);
            labelsContainer.add(descriptionLabel);
            labelsContainer.add(dateLabel);
            labelsContainer.add(idUserLabel);



        productRow.add(BorderLayout.CENTER, labelsContainer);

        // Create a container to hold the buttons
        Container buttonsContainer = new Container(new GridLayout(2, 1));
        buttonsContainer.setUIID("ProductButtonBox");
        buttonsContainer.add(editBtn);
        buttonsContainer.add(deleteBtn);

        productRow.add(BorderLayout.EAST, buttonsContainer);

        return productRow;
    }

    private boolean productMatchesQuery(Post product, String searchQuery) {
        String lowercaseNom = product.getDescription().toLowerCase();

        // Compare the attributes with the search query
        return lowercaseNom.contains(searchQuery);
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
