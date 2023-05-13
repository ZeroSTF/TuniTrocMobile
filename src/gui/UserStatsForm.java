package gui;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PieChart;
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
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import entities.User;
import services.ServiceUser;

import java.util.ArrayList;
import java.util.Calendar;

public class UserStatsForm extends BaseForm {

    private static final String[] monthNames = {"January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"};

    private static final int[] colors = {ColorUtil.BLUE, ColorUtil.CYAN, ColorUtil.GREEN, ColorUtil.LTGRAY,
        ColorUtil.MAGENTA, ColorUtil.YELLOW,
        ColorUtil.rgb(255, 102, 0), ColorUtil.rgb(204, 204, 255), ColorUtil.rgb(153, 0, 102)};

    public UserStatsForm(Resources res) {
        super("User Signups per Month", new BorderLayout());
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        tb.setUIID("Container");
        getTitleArea().setUIID("Container");
        Form previous = Display.getInstance().getCurrent();
        getContentPane().setScrollVisible(false);
        addSideMenu(res);
        tb.setBackCommand("", e -> previous.showBack());
        setUIID("SignUp");

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
        createPieChart();
    }

    private void createPieChart() {
        // Get the current year
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        // Get the list of users
        ArrayList<User> userList = ServiceUser.getInstance().getAllUsers();

        // Create a dataset for the pie chart
        DefaultRenderer renderer = new DefaultRenderer();
        CategorySeries series = new CategorySeries("User Signups");

        // Iterate over the users and count signups per month
        int[] signupCounts = new int[12]; // Array to store signup counts for each month
        for (User user : userList) {
            Calendar userCalendar = Calendar.getInstance();
            userCalendar.setTime(user.getDate());

            int userYear = userCalendar.get(Calendar.YEAR);
            int userMonth = userCalendar.get(Calendar.MONTH);

            if (userYear == currentYear) {
                signupCounts[userMonth]++;
            }
        }

        // Add data to the series and create labels
        for (int i = 0; i < 9; i++) {
            series.add(monthNames[i], signupCounts[i]);
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[i]);
            renderer.addSeriesRenderer(r);
        }

        // Create the pie chart and set the renderer
        PieChart chart = new PieChart(series, renderer);
        ChartComponent chartComponent = new ChartComponent(chart);

        // Create a container to hold the chart and labels
        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));

        // Add labels to the container
        for (int i = 0; i < 9; i++) {
            Label label = new Label(monthNames[i] + ": " + signupCounts[i]);
            label.getAllStyles().setFgColor(colors[i]);
            container.add(label);
        }

        // Add the chart component to the container
        container.add(chartComponent);

        // Add the container to the form
        add(BorderLayout.CENTER, container);
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
