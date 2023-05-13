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
import com.codename1.ui.ComboBox;
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
import entities.Product;
import entities.Reclamation;
import services.ServiceProduct;
import services.ServiceReclamation;

/**
 *
 * @author Lenovo
 */
public class AddProductForm extends BaseForm {

    Form current;

    public AddProductForm(Resources res) {
        super("Produits", BoxLayout.y()); //herigate men Newsfeed w l formulaire vertical
        this.setScrollableY(true);
        Toolbar tb = new Toolbar(true);
        current = this;
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Ajouter Produit");
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

        ButtonGroup barGroup = new ButtonGroup();
        RadioButton mesListes = RadioButton.createToggle("Ajouter Produit", barGroup);
        mesListes.setUIID("SelectBar");
        RadioButton liste = RadioButton.createToggle("...", barGroup);
        liste.setUIID("SelectBar");
        RadioButton partage = RadioButton.createToggle("Produits", barGroup);
        partage.setUIID("SelectBar");

        partage.addActionListener((e) -> {
            InfiniteProgress ip = new InfiniteProgress();
            final Dialog ipDlg = ip.showInifiniteBlocking();
            new ListProductForm(res).show();
            //////////ajouutttt
            //  ListPostForm a = new ListPostForm(res);
            //  a.show();
            refreshTheme();
        });

        add(LayeredLayout.encloseIn(
                GridLayout.encloseIn(3, mesListes, liste, partage)
        ));

        partage.setSelected(true);

        this.setScrollableY(true);
        TextField typeField = new TextField("", "Type");
        TextField categorieField = new TextField("", "Category");
        TextField nomField = new TextField("", "Name");
        TextField libelleField = new TextField("", "Description");
        TextField prixField = new TextField("", "Ville");
        TextField stockField = new TextField("", "Stock");

        Style textFieldStyle = new Style();
        textFieldStyle.setFgColor(0x000000);

        typeField.setUnselectedStyle(textFieldStyle);
        categorieField.setUnselectedStyle(textFieldStyle);
        nomField.setUnselectedStyle(textFieldStyle);
        libelleField.setUnselectedStyle(textFieldStyle);
        prixField.setUnselectedStyle(textFieldStyle);
        stockField.setUnselectedStyle(textFieldStyle);

        Button addBtn = new Button("Add");
        addBtn.addActionListener(e -> {
            try {
                String type = typeField.getText().trim();
                String categorie = categorieField.getText().trim();
                String nom = nomField.getText().trim();
                String libelle = libelleField.getText().trim();
                String prix = prixField.getText().trim();
                //int stock = Integer.parseInt(stockField.getText().trim());

                if (type.isEmpty() || categorie.isEmpty() || nom.isEmpty() || libelle.isEmpty() || prix.isEmpty()) {
                    Dialog.show("Error", "Please enter valid values for all fields", "OK", null);
                    return;
                }

                Product product = new Product();
                product.setType(type);
                product.setCategorie(categorie);
                product.setNom(nom);
                product.setLibelle(libelle);
                product.setPrix(prix);
                // product.setStock(stock);

                ServiceProduct.getInstance().addProduct(product);
                new ListProductForm(res).show();
            } catch (NumberFormatException ex) {
                Dialog.show("Error", "Please enter valid float value for Price and integer value for Stock", "OK", null);
            }
        });

        add(typeField);
        add(categorieField);
        add(nomField);
        add(libelleField);
        add(prixField);
//add(stockField);
        add(addBtn);

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
