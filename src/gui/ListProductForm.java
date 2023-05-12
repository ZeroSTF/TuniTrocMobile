/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import entities.Product;
import entities.Reclamation;
import java.util.ArrayList;
import services.ServiceProduct;
import services.ServiceReclamation;

/**
 *
 * @author ilyes
 */
public class ListProductForm extends BaseForm{
    Form current;
    private ArrayList<Product> products;
    public ListProductForm(Resources res ) {
          super("Produits",BoxLayout.y()); //herigate men Newsfeed w l formulaire vertical
        Toolbar tb = new Toolbar(true);
        current = this ;
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Produits");
        getContentPane().setScrollVisible(false);
        addSideMenu(res);
        
        
        tb.addSearchCommand(e ->  {
            
        });
        
        Tabs swipe = new Tabs();
        
        Label s1 = new Label();
        Label s2 = new Label();
        
        addTab(swipe,s1, res.getImage("back-logo.jpeg"),"","",res);

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
        RadioButton mesListes = RadioButton.createToggle("Ajouter Produit", barGroup);
        mesListes.setUIID("SelectBar");
        RadioButton liste = RadioButton.createToggle("...", barGroup);
        liste.setUIID("SelectBar");
        RadioButton partage = RadioButton.createToggle("Produits", barGroup);
        partage.setUIID("SelectBar");
        Label arrow = new Label(res.getImage("news-tab-down-arrow.png"), "Container");



        mesListes.addActionListener((e) -> {
               InfiniteProgress ip = new InfiniteProgress();
        final Dialog ipDlg = ip.showInifiniteBlocking();
        new AddProductForm(res).show();
        //////////ajouutttt
        //  ListPostForm a = new ListPostForm(res);
          //  a.show();
            refreshTheme();
        });

        add(LayeredLayout.encloseIn(
                GridLayout.encloseIn(3, mesListes, liste, partage),
                FlowLayout.encloseBottom(arrow)
        ));

        partage.setSelected(true);
        arrow.setVisible(false);
        addShowListener(e -> {
            arrow.setVisible(true);
            updateArrowPosition(partage, arrow);
        });
        bindButtonSelection(mesListes, arrow);
        bindButtonSelection(liste, arrow);
        bindButtonSelection(partage, arrow);
        // special case for rotation
        addOrientationListener(e -> {
            updateArrowPosition(barGroup.getRadioButton(barGroup.getSelectedIndex()), arrow);
        });


        
ArrayList<Product> products = ServiceProduct.getInstance().getAllProducts();

// Create container to hold the list of products
Container productsContainer = new Container();
productsContainer.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
productsContainer.setScrollableY(true);

// Add each product to the container with buttons to edit and delete it
for (Product product : products) {
    // Create a container to hold the product's information and buttons
    Container productRow = new Container(new BorderLayout());
    productRow.setUIID("ProductBox");

    // Create labels to display the product's information
    Label idLabel = new Label("ID: " + product.getId());
    idLabel.setUIID("ProductLabel");
    Label typeLabel = new Label("Type: " + product.getType());
    typeLabel.setUIID("ProductLabel");
    Label categorieLabel = new Label("Categorie: " + product.getCategorie());
    categorieLabel.setUIID("ProductLabel");
    Label nomLabel = new Label("Nom: " + product.getNom());
    nomLabel.setUIID("ProductLabel");
    Label libelleLabel = new Label("Libelle: " + product.getLibelle());
    libelleLabel.setUIID("ProductLabel");
    Label prixLabel = new Label("Ville: " + product.getPrix());
    prixLabel.setUIID("ProductLabel");
    Label stockLabel = new Label("Stock: " + product.getStock());
    stockLabel.setUIID("ProductLabel");

    // Create buttons to edit and delete the product
    Button editBtn = new Button("Edit");
    editBtn.addActionListener(e -> {
         new EditProductForm(res, product).show();
    });
    Button deleteBtn = new Button("Delete");
    deleteBtn.addActionListener(e -> {
        // Delete the product from the server
        ServiceProduct.getInstance().deleteProduct(product.getId());
        

        // Remove the product from the container
        productsContainer.removeComponent(productRow);
    });

    // Add the labels to the product row
    Container labelsContainer = new Container(new GridLayout(7, 1));
    labelsContainer.add(idLabel);
    labelsContainer.add(typeLabel);
    labelsContainer.add(categorieLabel);
    labelsContainer.add(nomLabel);
    labelsContainer.add(libelleLabel);
    labelsContainer.add(prixLabel);
  //  labelsContainer.add(stockLabel);

    productRow.add(BorderLayout.CENTER, labelsContainer);

    // Create a container to hold the buttons
    Container buttonsContainer = new Container(new GridLayout(2, 1));
    buttonsContainer.setUIID("ProductButtonBox");
     buttonsContainer.add(editBtn);
    buttonsContainer.add(deleteBtn);

    productRow.add(BorderLayout.EAST, buttonsContainer);

    // Add the product row to the container
    productsContainer.add(productRow);
}

// Add the container to the form
add(productsContainer);


        
        
        
    }
    
    
    
    
    
    
    
    
       private void addTab(Tabs swipe, Label spacer , Image image, String string, String text, Resources res) {
        int size = Math.min(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
        
        if(image.getHeight() < size) {
            image = image.scaledHeight(size);
        }
        
        
        
        if(image.getHeight() > Display.getInstance().getDisplayHeight() / 2 ) {
            image = image.scaledHeight(Display.getInstance().getDisplayHeight() / 2);
        }
        
        ScaleImageLabel imageScale = new ScaleImageLabel(image);
        imageScale.setUIID("Container");
        imageScale.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        
        Label overLay = new Label("","ImageOverlay");
        
        
        Container page1 = 
                LayeredLayout.encloseIn(
                imageScale,
                        overLay,
                        BorderLayout.south(
                        BoxLayout.encloseY(
                        new SpanLabel(text, "LargeWhiteText"),
                                        spacer
                        )
                    )
                );
        
        swipe.addTab("",res.getImage("back-logo.jpeg"), page1);
        
        
        
        
    }
    
    
    
    public void bindButtonSelection(Button btn , Label l ) {
        
        btn.addActionListener(e-> {
        if(btn.isSelected()) {
            updateArrowPosition(btn,l);
        }
    });
    }

    private void updateArrowPosition(Button btn, Label l) {
        
        l.getUnselectedStyle().setMargin(LEFT, btn.getX() + btn.getWidth()  / 2  - l.getWidth() / 2 );
        l.getParent().repaint();
    }

    
   
   
}

