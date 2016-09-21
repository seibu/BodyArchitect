/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import de.seibushin.bodyArchitect.Main;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.views.listCell.FoodCell;
import de.seibushin.bodyArchitect.views.listCell.MealCell;
import de.seibushin.bodyArchitect.model.nutrition.Day;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.scene.control.gauge.linear.elements.PercentSegment;
import jfxtras.scene.control.gauge.linear.elements.Segment;

public class NutritionController {
    public static final String MEAL_DAY_VIEW = "Day";
    public static final String MEAL_VIEW = "Meal";
    public static final String FOOD_VIEW = "Food";
    public static final String SETTINGS_VIEW = "Settings";

    @FXML
    View nutrition;
    @FXML
    SimpleMetroArcGauge mag_kcal;
    @FXML
    SimpleMetroArcGauge mag_protein;
    @FXML
    SimpleMetroArcGauge mag_fat;
    @FXML
    SimpleMetroArcGauge mag_carbs;
    @FXML
    VBox vbox_day;
    @FXML
    VBox vbox_logs;
    @FXML
    TabPane tb_nutrition;
    @FXML
    ListView lv_day_meals;
    @FXML
    ListView lv_meals;
    @FXML
    ListView lv_food;
    @FXML
    Button btn_date;

    @FXML
    Label lbl_kcal_add;
    @FXML
    Label lbl_fat_add;
    @FXML
    Label lbl_carbs_add;
    @FXML
    Label lbl_protein_add;

    private Button btn_addMealToDay = MaterialDesignIcon.PLAYLIST_ADD.button(e -> showAddMealToDay());
    private Button btn_addMeal = MaterialDesignIcon.PLAYLIST_ADD.button(e -> showAddMeal());
    private Button btn_addFood = MaterialDesignIcon.PLAYLIST_ADD.button(e -> showAddFood());
    private Button btn_settings = MaterialDesignIcon.SETTINGS.button(e -> showSettings());

    private double currKcal;
    private double currFat;
    private double currProtein;
    private double currCarbs;

    private final BooleanProperty sliding = new SimpleBooleanProperty();

    private void showAddMealToDay() {
        nutrition.getApplication().switchView(MEAL_DAY_VIEW);
    }

    private void showAddFood() {
        nutrition.getApplication().switchView(FOOD_VIEW);
    }

    private void showAddMeal() {
        nutrition.getApplication().switchView(MEAL_VIEW);
    }

    private void showSettings() {
        nutrition.getApplication().switchView(SETTINGS_VIEW);
    }

    @FXML
    private void showLogBook() {
        // show LogBook
        tb_nutrition.getSelectionModel().select(3);
    }

    private void updateMeal() {
        lv_meals.setItems(Service.getInstance().getMeals());
    }

    private void updateFood() {
        lv_food.setItems(Service.getInstance().getFoods());
    }

    private void updateDay() {
        System.out.println("update Day");
        Day selectedDay = Service.getInstance().getSelectedDayObject();
        System.out.println(selectedDay);
        btn_date.setText(Service.getInstance().getSelectedDayString());

        if (selectedDay != null) {
            lv_day_meals.setItems(Service.getInstance().getMealsForSelectedDay());

            // update gauges
            currKcal = selectedDay.getKcal();
            currCarbs = selectedDay.getCarbs();
            currProtein = selectedDay.getProtein();
            currFat = selectedDay.getFat();

            updateGauge(mag_kcal, currKcal, lbl_kcal_add);
            updateGauge(mag_carbs, currCarbs, lbl_carbs_add);
            updateGauge(mag_fat, currFat, lbl_fat_add);
            updateGauge(mag_protein, currProtein, lbl_protein_add);
        } else {
            lv_day_meals.setItems(null);
            mag_kcal.setValue(0);
            mag_carbs.setValue(0);
            mag_fat.setValue(0);
            mag_protein.setValue(0);
        }
    }

    private void updateGauge(SimpleMetroArcGauge gauge, double nv, Label lbl) {
        if (nv > gauge.getMaxValue()) {
            double diff = nv - gauge.getMaxValue();
            lbl.setText("+" + diff);
            gauge.setValue(gauge.getMaxValue());
        } else {
            gauge.setValue(nv);
            lbl.setText("");
        }
    }

    @FXML
    public void initialize() {
        // add the views
        MobileApplication.getInstance().addViewFactory(MEAL_DAY_VIEW, () -> new MealDayView(MEAL_DAY_VIEW).getView());
        MobileApplication.getInstance().addViewFactory(MEAL_VIEW, () -> new MealView(MEAL_VIEW).getView());
        MobileApplication.getInstance().addViewFactory(FOOD_VIEW, () -> new FoodView(FOOD_VIEW).getView());
        MobileApplication.getInstance().addViewFactory(SETTINGS_VIEW, () -> new SettingsView(SETTINGS_VIEW).getView());

        // add the mealInfoLayer (popup)
        MobileApplication.getInstance().addLayerFactory("MealInfo", () -> Service.getInstance().getMealInfoLayer());

        // create the logbook
        // NOTE: creating the LogBook directly in the bodyArchitect class would result in the icons not showing correctly
        LogBook logBook = new LogBook();
        Service.getInstance().setLogBook(logBook);

        // update appBar
        nutrition.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Main.MENU_LAYER)));
                appBar.setTitleText("Nutrition");
                appBar.getActionItems().addAll(btn_addMealToDay, btn_settings);

                // refresh Day if something was updated
                updateDay();
                updateMeal();
                updateFood();
            }
        });

        // =====================
        // day Tab
        // =====================
        // set the cellFactory and show the data for the current Day no need to clear the list here!
        lv_day_meals.setCellFactory(cell -> {
            final MealCell mealCell = new MealCell(
                    c -> {
                        System.out.println("left -> delete");
                    },
                    c -> {
                        System.out.println("right");
                    });
            // notify view that cell is sliding
            sliding.bind(mealCell.slidingProperty());

            return mealCell;
        });

        // prevent the list from scrolling while sliding
        lv_meals.addEventFilter(ScrollEvent.ANY, e -> {
            if (sliding.get() && e.getDeltaY() != 0) {
                e.consume();
            }
        });

        //mag_kcal.maxValueProperty().bind(Service.getInstance().settingsProperty().get().targetKcalProperty());
        Segment seg1 = new PercentSegment(mag_kcal, 0, 100);
        Segment seg2 = new PercentSegment(mag_kcal, 95, 100);
        mag_kcal.segments().addAll(seg1, seg2);
        mag_kcal.maxValueProperty().addListener(e -> {
            updateGauge(mag_kcal, currKcal, lbl_kcal_add);
        });

        //mag_protein.maxValueProperty().bind(Service.getInstance().settingsProperty().get().getTargetProtein());
        seg1 = new PercentSegment(mag_protein, 0, 100);
        seg2 = new PercentSegment(mag_protein, 95, 100);
        mag_protein.segments().addAll(seg1, seg2);
        mag_protein.maxValueProperty().addListener(e -> {
            updateGauge(mag_protein, currProtein, lbl_protein_add);
        });

        //mag_carbs.maxValueProperty().bind(Service.getInstance().settingsProperty().get().targetCarbsProperty());
        seg1 = new PercentSegment(mag_carbs, 0, 100);
        seg2 = new PercentSegment(mag_carbs, 95, 100);
        mag_carbs.segments().addAll(seg1, seg2);
        mag_carbs.maxValueProperty().addListener(e -> {
            updateGauge(mag_carbs, currCarbs, lbl_carbs_add);
        });

        //mag_fat.maxValueProperty().bind(Service.getInstance().settingsProperty().get().targetFatProperty());
        seg1 = new PercentSegment(mag_fat, 0, 100);
        seg2 = new PercentSegment(mag_fat, 95, 100);
        mag_fat.segments().addAll(seg1, seg2);
        mag_fat.maxValueProperty().addListener(e -> {
            updateGauge(mag_fat, currFat, lbl_fat_add);
        });

        // =====================
        // Meal Tab
        // =====================
        lv_meals.setCellFactory(cell -> {
            final MealCell mealCell = new MealCell(
                    c -> {
                        System.out.println("left");
                    },
                    c -> {
                        System.out.println("right");
                    });
            // notify view that cell is sliding
            sliding.bind(mealCell.slidingProperty());

            return mealCell;
        });

        // prevent the list from scrolling while sliding
        lv_meals.addEventFilter(ScrollEvent.ANY, e -> {
            if (sliding.get() && e.getDeltaY() != 0) {
                e.consume();
            }
        });

        updateMeal();

        // =====================
        // Food Tab
        // =====================

        lv_food.setCellFactory(cell -> {
            final FoodCell foodCell = new FoodCell();

            foodCell.setConsumer(
                    c -> {
                        System.out.println("left");

                    },
                    c -> {
                        System.out.println("right");
                    });

            // notify view that cell is sliding
            sliding.bind(foodCell.slidingProperty());

            return foodCell;
        });

        // prevent the list from scrolling while sliding
        lv_food.addEventFilter(ScrollEvent.ANY, e -> {
            if (sliding.get() && e.getDeltaY() != 0) {
                e.consume();
            }
        });

        updateFood();

        // =====================
        // Logs Tab
        // =====================
        // add the logBook to the Pane
        vbox_logs.getChildren().add(Service.getInstance().getLogBook().getWrapper());

        // listener for the selectedDay
        Service.getInstance().getLogBook().getSelectedDayProperty().addListener((obs, ov, nv) -> {
            // @todo only switch if the day has data
            if (nv != null) {
                updateDay();
                // switch to Day tab
                tb_nutrition.getSelectionModel().select(0);
            }
        });


        // react on tab selection
        tb_nutrition.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            AppBar appBar = MobileApplication.getInstance().getAppBar();
            System.out.println(newValue);

            switch (newValue.intValue()) {
                case 0:
                    // day
                    appBar.getActionItems().setAll(btn_addMealToDay, btn_settings);
                    break;
                case 1:
                    // meal
                    appBar.getActionItems().setAll(btn_addMeal, btn_settings);
                    break;
                case 2:
                    // food
                    appBar.getActionItems().setAll(btn_addFood, btn_settings);
                    break;
                case 3:
                    // logs
                    appBar.getActionItems().setAll(btn_settings);
                    break;
            }
        });
    }
}