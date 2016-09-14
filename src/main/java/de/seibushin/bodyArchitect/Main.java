package de.seibushin.bodyArchitect;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.ListTile;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Item;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
import com.gluonhq.charm.glisten.layout.layer.SnackbarPopupView;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import de.seibushin.bodyArchitect.views.HomeView;
import de.seibushin.bodyArchitect.views.NutritionView;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends MobileApplication {

    // Views and Layers
    public static final String HOME_VIEW = "home";
    public static final String NUTRITION_VIEW = "Nutrition";
    public static final String WORKOUT_VIEW = "Workout";
    public static final String MENU_LAYER = "Side Menu";
    public static final String SNACK_LAYER = "Snackbar Bottom";


    @Override
    public void init() {
        // @todo switch to ORMLite instead of Hibernate for better mobile support
        // @todo switch to SQLite instead of h2 for better mobile support

        // @todo try saving the data in local storage as Objects
        // maybe try this for the settings first
        // http://gluonhq.com/learn-to-build-gluon-charm-apps-with-three-new-charm-samples/

        // init BodyArchitect
        BodyArchitect.init();

        // Views

        addViewFactory(HOME_VIEW, () -> new HomeView(HOME_VIEW).getView());
        addViewFactory(NUTRITION_VIEW, () -> new NutritionView(NUTRITION_VIEW).getView());
        //addViewFactory(WORKOUT_VIEW, () -> new WorkoutView(WORKOUT_VIEW).getView());

        // side Navigation
        NavigationDrawer drawer = new NavigationDrawer();

        // set Navigation Header
        NavigationDrawer.Header header = new NavigationDrawer.Header("BodyArchitect",
                "Nutrition Edition",
                new Avatar(21, new Image(Main.class.getResourceAsStream("/icon.png"))));
        drawer.setHeader(header);

        // add the Items to the menu
        drawer.getItems().addAll(BodyArchitect.getInstance().getDrawerItems());

        // add Listener for the menu to change the View
        drawer.selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            hideLayer(MENU_LAYER);
            switch (((Item)newItem).getTitle()) {
                case "Home":
                    switchView(HOME_VIEW);
                    break;
                case "Nutrition":
                    switchView(NUTRITION_VIEW);
                    break;
                case "Workout":
                    switchView(WORKOUT_VIEW);
                    break;
            }
        });

        viewProperty().addListener(e -> {
            System.out.println(e);
        });

        // add sidemenu
        addLayerFactory(MENU_LAYER, () -> new SidePopupView(drawer));
        addLayerFactory(SNACK_LAYER, () -> new SnackbarPopupView());
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.GREY.assignTo(scene);

        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
    }
}
