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
import entities.Reclamation;
import java.util.ArrayList;
import services.ServiceReclamation;
import com.codename1.components.SpanLabel;
import com.codename1.ui.TextField;
import entities.User;
import services.ServiceUser;

/**
 *
 * @author Lenovo
 */
public class ListReclamationFormF extends BaseForm {

    Form current;
    private ArrayList<Reclamation> allReclamations; // Store all users
    private ArrayList<Reclamation> filteredReclamations; // Store filtered users
    private TextField searchField; // Add this field
    private Container reclamationsContainer;

    public ListReclamationFormF(Resources res) {
        super("Vos Reclamations", BoxLayout.y()); //herigate men Newsfeed w l formulaire vertical
        Toolbar tb = new Toolbar(true);
        current = this;
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Reclamations");
        getContentPane().setScrollVisible(false);
        addSideMenu(res);
        searchField = new TextField("", "Search");
        searchField.addDataChangeListener((i, ii) -> {
            String searchQuery = searchField.getText();

            filterReclamations(searchQuery);
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
        RadioButton partage = RadioButton.createToggle("Reclamations", barGroup);
        partage.setUIID("SelectBar");

        //   mesListes.addActionListener((e) -> {
        //        InfiniteProgress ip = new InfiniteProgress();
        //  final Dialog ipDlg = ip.showInifiniteBlocking();
        //new AddReclamationForm(res).show();
        //////////ajouutttt
        //  ListPostForm a = new ListPostForm(res);
        //  a.show();
        //        refreshTheme();
        //   });
        mesListes.addActionListener((e) -> {
            InfiniteProgress ip = new InfiniteProgress();
            final Dialog ipDlg = ip.showInifiniteBlocking();
            new AddReclamationForm(res).show();
            //////////ajouutttt
            //  ListPostForm a = new ListPostForm(res);
            //  a.show();
            refreshTheme();
        });

        add(LayeredLayout.encloseIn(
                GridLayout.encloseIn(3, mesListes, liste, partage)
        ));

        partage.setSelected(true);
        // special case for rotation

        allReclamations = ServiceReclamation.getInstance().getAllReclamations();
        filteredReclamations = new ArrayList<>(allReclamations);

// Create container to hold the list of reclamations
        
        reclamationsContainer = new Container();
        reclamationsContainer.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        this.setScrollableY(true);

// Add each reclamation to the container with buttons to edit and delete it
        for (Reclamation reclamation : filteredReclamations) {
    Container reclamationRow = createReclamationContainer(reclamation);
    reclamationsContainer.add(reclamationRow);
}

// Add the container to the form
        add(reclamationsContainer);

    }

    private void filterReclamations(String searchQuery) {
        reclamationsContainer.removeAll();
        filteredReclamations.clear();

        if (searchQuery.isEmpty()) {
            filteredReclamations.addAll(allReclamations);
        } else {
            // Filter reclamations based on the search query
            for (Reclamation reclamation : allReclamations) {
                if (reclamationMatchesQuery(reclamation, searchQuery)) {
                    filteredReclamations.add(reclamation);
                }
            }
        }
        for (Reclamation user : filteredReclamations) {
            Container reclamationRow = createReclamationContainer(user);
            reclamationsContainer.add(reclamationRow);
        }

        revalidate();
        repaint();
    }

    private Container createReclamationContainer(Reclamation reclamation) {
        Container reclamationRow = new Container(new BorderLayout());
        reclamationRow.setUIID("ReclamationBox");

        // Create labels to display the reclamation's information
        Label idLabel = new Label("ID: " + reclamation.getId());
        idLabel.setUIID("ReclamationLabel");
        Label causeLabel = new Label("Cause: " + reclamation.getCause());
        causeLabel.setUIID("ReclamationLabel");
        // Add more labels for other reclamation information

        // Create a container to hold the labels
        Container labelsContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        labelsContainer.add(idLabel);
        labelsContainer.add(causeLabel);
        // Add more labels to the container

        // Add the labels container to the reclamation row
        reclamationRow.add(BorderLayout.CENTER, labelsContainer);

        return reclamationRow;
    }

    private boolean reclamationMatchesQuery(Reclamation reclamation, String searchQuery) {
        String lowercaseCause = reclamation.getCause().toLowerCase();
        // Add more attributes to compare if necessary

        return lowercaseCause.contains(searchQuery);
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
