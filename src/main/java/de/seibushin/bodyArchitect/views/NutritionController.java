/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.CharmListView;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import de.seibushin.bodyArchitect.Main;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.model.nutrition.Day;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import de.seibushin.bodyArchitect.model.nutrition.SimpleMeal;
import de.seibushin.bodyArchitect.views.layers.FilterFoodController;
import de.seibushin.bodyArchitect.views.layers.FilterView;
import de.seibushin.bodyArchitect.views.listCell.FoodCell;
import de.seibushin.bodyArchitect.views.listCell.MealCell;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.scene.control.gauge.linear.elements.PercentSegment;
import jfxtras.scene.control.gauge.linear.elements.Segment;
import org.omg.CORBA.MARSHAL;

public class NutritionController {
    public static final String MEAL_DAY_VIEW = "Day";
    public static final String MEAL_VIEW = "Meal";
    public static final String FOOD_VIEW = "Food";

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
    CharmListView lv_day_meals;
    @FXML
    CharmListView<SimpleMeal, String> lv_meals;
    @FXML
    CharmListView<Food, String> lv_food;

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
    private Button btn_date = MaterialDesignIcon.TODAY.button(e -> showLogBook());
    private Button btn_searchFood = MaterialDesignIcon.SEARCH.button(e -> showSearch());


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

    @FXML
    private void showLogBook() {
        // show LogBook
        tb_nutrition.getSelectionModel().select(3);
    }

    private void showSearch() {
        nutrition.getApplication().showLayer(Main.FILTER_FOOD);
    }

    private void updateDay() {
        Day selectedDay = Service.getInstance().getSelectedDayObject();
        //btn_date.setText(Service.getInstance().getSelectedDayString());
        MobileApplication.getInstance().getAppBar().setTitleText("Nutrition - " + Service.getInstance().getSelectedDayString());

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
            lv_day_meals.setItems(FXCollections.emptyObservableList());
            mag_kcal.setValue(0);
            mag_carbs.setValue(0);
            mag_fat.setValue(0);
            mag_protein.setValue(0);
        }
    }

    private void updateGauge(SimpleMetroArcGauge gauge, double nv, Label lbl) {
        nv = Math.ceil(nv);
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
                appBar.getActionItems().addAll(btn_addMealToDay, btn_date);

                updateDay();
            }
        });

        // =====================
        // day Tab
        // =====================

        lv_day_meals.setCellFactory(cell -> {
            final MealCell mealCell = new MealCell(
                    c -> {
                        Service.getInstance().deleteDayMeal(c);
                        // @todo we only need to update the gauges
                        // but we dont have a reference to the selectedDay present so we would need to get it again
                        // fix this ...
                        updateDay();
                    },
                    c -> {
                        System.out.println("right");
                    });
            // notify view that cell is sliding
            sliding.bind(mealCell.slidingProperty());

            return mealCell;
        });

        // prevent the list from scrolling while sliding
        lv_day_meals.addEventFilter(ScrollEvent.ANY, e -> {
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

        // order by name
        lv_meals.setComparator(((o1, o2) -> o1.getName().compareTo(o2.getName())));
        lv_meals.setItems(Service.getInstance().getMeals());

        // =====================
        // Food Tab
        // =====================

        lv_food.setCellFactory(cell -> {
            final FoodCell foodCell = new FoodCell(false);

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

        // filter for food
        nutrition.getApplication().addLayerFactory(Main.FILTER_FOOD, () -> {
            FilterView filterView = new FilterView("");
            SidePopupView sidePopupView = new SidePopupView(filterView.getView(), Side.TOP, true);
            FilterFoodController filterFoodController = filterView.getController();
            sidePopupView.showingProperty().addListener((obs, ov, nv) -> {
                if (ov && !nv) {
                    FilteredList<Food> filteredList = new FilteredList(Service.getInstance().getFoods());
                    filteredList.setPredicate(filterFoodController.getPredicate());
                    lv_food.setItems(filteredList);
                }
            });
            return sidePopupView;
        });

        lv_food.setItems(Service.getInstance().getFoods());
        lv_food.setComparator(((o1, o2) -> o1.getName().compareTo(o2.getName())));

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
            // search for a better way ._.
            System.out.println(newValue);
            switch (newValue.intValue()) {
                case 0:
                    // day
                    appBar.getActionItems().setAll(btn_addMealToDay, btn_date);
                    break;
                case 1:
                    // meal
                    appBar.getActionItems().setAll(btn_addMeal);
                    break;
                case 2:
                    // food
                    appBar.getActionItems().setAll(btn_addFood, btn_searchFood);
                    break;
                case 3:
                    // logs
                    appBar.getActionItems().clear();
                    break;
            }
        });
    }
}