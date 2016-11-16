package de.seibushin.bodyArchitect;

import com.gluonhq.charm.down.Platform;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Item;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import de.seibushin.bodyArchitect.views.*;
import de.seibushin.bodyArchitect.views.layers.MealInfoLayer;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends MobileApplication {
    // Views and Layers
    public static final String BA_VIEW = HOME_VIEW;
    public static final String NUTRITION_VIEW = "Nutrition";
    public static final String NUTRITION_PLANS_VIEW = "Nutrition Plans";
    public static final String WORKOUT_VIEW = "Workout";
    public static final String MENU_LAYER = "Side Menu";
    public static final String SETTINGS_VIEW = "Settings";
    public static final String FILTER_FOOD = "Filter Food";
    public static final String MEAL_DAY_VIEW = "Meal Day";

    @Override
    public void init() {
        // maybe try this for the settings first
        // http://gluonhq.com/learn-to-build-gluon-charm-apps-with-three-new-charm-samples/

        // init BodyArchitect
        Service.init();

        // Views
        addViewFactory(BA_VIEW, () -> new HomeView(BA_VIEW).getView());
        addViewFactory(NUTRITION_VIEW, () -> new NutritionView(NUTRITION_VIEW).getView());
        addViewFactory(NUTRITION_PLANS_VIEW, () -> new NutritionPlansView(NUTRITION_PLANS_VIEW).getView());
        //addViewFactory(WORKOUT_VIEW, () -> new WorkoutView(WORKOUT_VIEW).getView());
        addViewFactory(SETTINGS_VIEW, () -> new SettingsView(SETTINGS_VIEW).getView());
        addViewFactory(MEAL_DAY_VIEW, () -> new MealDayView(MEAL_DAY_VIEW).getView());

        // side Navigation
        NavigationDrawer drawer = new NavigationDrawer();
        // set Navigation Header
        NavigationDrawer.Header header = new NavigationDrawer.Header("BodyArchitect",
                "Nutrition Edition",
                new Avatar(21, new Image(Main.class.getResourceAsStream("/icon.png"))));
        drawer.setHeader(header);

        final Item home = new Item(BA_VIEW, MaterialDesignIcon.HOME.graphic());
        final Item nutrition = new Item(NUTRITION_VIEW, MaterialDesignIcon.LOCAL_DINING.graphic());
        final Item nutritionPlans = new Item(NUTRITION_PLANS_VIEW, MaterialDesignIcon.ASSIGNMENT.graphic());
        final Item workout = new Item(WORKOUT_VIEW, MaterialDesignIcon.FITNESS_CENTER.graphic());
        final Item setting = new Item(SETTINGS_VIEW, MaterialDesignIcon.TODAY.graphic());

        // @todo not yet implemented
        workout.setDisable(true);

        // add the Items to the menu
        drawer.getItems().addAll(home, nutrition, nutritionPlans, workout, setting);
        // add sidemenu
        addLayerFactory(MENU_LAYER, () -> new SidePopupView(drawer));

        home.setSelected(true);
        // if a new item in the drawer is selected we need to switchView
        drawer.selectedItemProperty().addListener((obs, ov, nv) -> {
            if (nv != null) {
                hideLayer(MENU_LAYER);
                Item item = (Item) nv;
                switchView(item.getTitle());
            }
        });

        // react on a change of the view
        viewProperty().addListener((obs, ov, nv) -> {
            if (nv != null) {
                // iterate through all drawer items so determine the selected view
                // if the view is not a element of the menu every item is deselected keep this in mind
                // this should not be a problem normally since these views dont use the menu instead they get a
                // return button
                for (Node node : drawer.getItems()) {
                    Item item = (Item) node;
                    if (nv.getName().equals(item.getTitle())) {
                        // select the new menu item
                        drawer.setSelectedItem(node);
                        item.setSelected(true);
                    } else {
                        // deselect all other items
                        item.setSelected(false);
                    }
                }
            }
        });

        // add the mealInfoLayer (popup)
        addLayerFactory("MealInfo", () -> Service.getInstance().getMealInfoLayer());
    }

    @Override
    public void postInit(Scene scene) {
        Service.getInstance().setMealInfoLayer(new MealInfoLayer());

        Swatch.GREY.assignTo(scene);

        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));

        if (Platform.isDesktop()) {
            scene.getWindow().setWidth(400);
        }
    }
}
