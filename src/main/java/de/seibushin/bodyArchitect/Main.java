package de.seibushin.bodyArchitect;

import com.gluonhq.charm.down.common.JavaFXPlatform;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Item;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
import com.gluonhq.charm.glisten.layout.layer.SnackbarPopupView;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import de.seibushin.bodyArchitect.views.NutritionPlansView;
import de.seibushin.bodyArchitect.views.SettingsView;
import de.seibushin.bodyArchitect.views.layers.MealInfoLayer;
import de.seibushin.bodyArchitect.views.HomeView;
import de.seibushin.bodyArchitect.views.NutritionView;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends MobileApplication {

    // Views and Layers
    public static final String HOME_VIEW = "home";
    public static final String NUTRITION_VIEW = "Nutrition";
    public static final String NUTRITION_PLANS_VIEW = "Nutrition Plans";
    public static final String WORKOUT_VIEW = "Workout";
    public static final String MENU_LAYER = "Side Menu";
    public static final String SETTINGS_VIEW = "Settings";
    public static final String FILTER_FOOD = "Filter Food";

    @Override
    public void init() {
        // @todo switch to ORMLite instead of Hibernate for better mobile support

        // maybe try this for the settings first
        // http://gluonhq.com/learn-to-build-gluon-charm-apps-with-three-new-charm-samples/

        // init BodyArchitect
        Service.init();

        // Views
        addViewFactory(HOME_VIEW, () -> new HomeView(HOME_VIEW).getView());
        addViewFactory(NUTRITION_VIEW, () -> new NutritionView(NUTRITION_VIEW).getView());
        addViewFactory(NUTRITION_PLANS_VIEW, () -> new NutritionPlansView(NUTRITION_PLANS_VIEW).getView());
        //addViewFactory(WORKOUT_VIEW, () -> new WorkoutView(WORKOUT_VIEW).getView());
        addViewFactory(SETTINGS_VIEW, () -> new SettingsView(SETTINGS_VIEW).getView());

        // side Navigation
        NavigationDrawer drawer = new NavigationDrawer();

        // set Navigation Header
        NavigationDrawer.Header header = new NavigationDrawer.Header("BodyArchitect",
                "Nutrition Edition",
                new Avatar(21, new Image(Main.class.getResourceAsStream("/icon.png"))));
        drawer.setHeader(header);

        Item home = new NavigationDrawer.Item(HOME_VIEW, MaterialDesignIcon.HOME.graphic());
        home.setSelected(true);
        Item nutrition = new NavigationDrawer.Item(NUTRITION_VIEW, MaterialDesignIcon.LOCAL_DINING.graphic());
        Item nutritionPlans = new NavigationDrawer.Item(NUTRITION_PLANS_VIEW, MaterialDesignIcon.ASSIGNMENT.graphic());
        Item workout = new NavigationDrawer.Item(WORKOUT_VIEW, MaterialDesignIcon.FITNESS_CENTER.graphic());
        Item setting = new NavigationDrawer.Item(SETTINGS_VIEW, MaterialDesignIcon.TODAY.graphic());

        // add the Items to the menu
        drawer.getItems().addAll(home, nutrition, nutritionPlans, workout, setting);

        // add Listener for the menu to change the View
        drawer.selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            hideLayer(MENU_LAYER);
            switch (((Item)newItem).getTitle()) {
                case HOME_VIEW:
                    switchView(HOME_VIEW);
                    break;
                case NUTRITION_VIEW:
                    switchView(NUTRITION_VIEW);
                    break;
                case NUTRITION_PLANS_VIEW:
                    switchView(NUTRITION_PLANS_VIEW);
                    break;
                case WORKOUT_VIEW:
                    switchView(WORKOUT_VIEW);
                    break;
                case SETTINGS_VIEW:
                    switchView(SETTINGS_VIEW);
            }
        });

        viewProperty().addListener(e -> {
            System.out.println("test this");
        });

        // add sidemenu
        addLayerFactory(MENU_LAYER, () -> new SidePopupView(drawer));
    }

    @Override
    public void postInit(Scene scene) {
        Service.getInstance().setMealInfoLayer(new MealInfoLayer());

        Swatch.GREY.assignTo(scene);

        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
        if (JavaFXPlatform.isDesktop()) {
            scene.getWindow().setWidth(400);
        }
    }
}
