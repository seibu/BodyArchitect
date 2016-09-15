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
import de.seibushin.bodyArchitect.BodyArchitect;
import de.seibushin.bodyArchitect.Main;
import de.seibushin.bodyArchitect.helper.FoodCell;
import de.seibushin.bodyArchitect.helper.LogBook;
import de.seibushin.bodyArchitect.helper.MealCell;
import de.seibushin.bodyArchitect.model.nutrition.Day;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import de.seibushin.bodyArchitect.model.nutrition.MealFood;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.scene.control.gauge.linear.elements.PercentSegment;
import jfxtras.scene.control.gauge.linear.elements.Segment;

import java.nio.channels.Pipe;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

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
    ListView lv_day_meals;
    @FXML
    ListView lv_meals;
    @FXML
    ListView lv_food;
    @FXML
    Button btn_date;

    private Button btn_addMealToDay = MaterialDesignIcon.PLAYLIST_ADD.button(e -> showAddMealToDay());
    private Button btn_addMeal = MaterialDesignIcon.PLAYLIST_ADD.button(e -> showAddMeal());
    private Button btn_addFood = MaterialDesignIcon.PLAYLIST_ADD.button(e -> showAddFood());

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

    private void updateMeal() {
        // only update if needed
        if (BodyArchitect.getInstance().isUpdateMeal()) {
            System.out.println("updateMeal");
            // set UpdateMeal to false
            BodyArchitect.getInstance().setUpdateMeal(false);

            lv_meals.setItems(BodyArchitect.getInstance().getData(Meal.class));
        }
    }

    private void updateFood() {
        // only update if needed
        if (BodyArchitect.getInstance().isUpdateFood()) {
            System.out.println("updateFood");
            // set UpdateFood to false
            BodyArchitect.getInstance().setUpdateFood(false);

            // @todo add option to sort the Elements
            // keep in mind to implement the Comparable-Interface for the other classes
            lv_food.setItems(BodyArchitect.getInstance().getData(Food.class).sorted());
        }
    }

    private void updateDay() {
        // only update if needed
        if (BodyArchitect.getInstance().isUpdateDay()) {
            System.out.println("updateDay");
            // set UpdateDay to false
            BodyArchitect.getInstance().setUpdateDay(false);

            // update date Label
            btn_date.setText(BodyArchitect.getInstance().getSelectedDayString());

            // get selectedDay
            Day selectedDay = BodyArchitect.getInstance().getSelectedDayObject();

            if (selectedDay != null) {
                // udpate meals
                lv_day_meals.setItems(BodyArchitect.getInstance().getMealsForSelectedDay());

                // update gauges
                // @todo check for maxValue and change the maxValue accordingly
                mag_kcal.setValue(selectedDay.getKcal());
                mag_carbs.setValue(selectedDay.getCarbs());
                mag_fat.setValue(selectedDay.getFat());
                mag_protein.setValue(selectedDay.getProtein());
            } else {
                // clear the elements if day is null
                lv_day_meals.getItems().clear();
                mag_kcal.setValue(0);
                mag_carbs.setValue(0);
                mag_fat.setValue(0);
                mag_protein.setValue(0);
            }
        }
    }

    @FXML
    public void initialize() {
        // add the views
        MobileApplication.getInstance().addViewFactory(MEAL_DAY_VIEW, () -> new MealDayView(MEAL_DAY_VIEW).getView());
        MobileApplication.getInstance().addViewFactory(MEAL_VIEW, () -> new MealView(MEAL_VIEW).getView());
        MobileApplication.getInstance().addViewFactory(FOOD_VIEW, () -> new FoodView(FOOD_VIEW).getView());

        // create the logbook
        // NOTE: creating the LogBook directly in the bodyArchitect class would result in the icons not showing correctly
        LogBook logBook = new LogBook();
        BodyArchitect.getInstance().setLogBook(logBook);

        // update appBar
        nutrition.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Main.MENU_LAYER)));
                appBar.setTitleText("Nutrition");
                appBar.getActionItems().add(btn_addMealToDay);

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
                        System.out.println("left");
                        // do the delete
                        BodyArchitect.getInstance().setUpdateDay(true);
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
        lv_meals.addEventFilter(ScrollEvent.ANY, e -> {
            if (sliding.get() && e.getDeltaY() != 0) {
                e.consume();
            }
        });

        updateDay();

        // @todo set the correct Segments and search for a more convenient way this is bs...
        Segment seg1 = new PercentSegment(mag_kcal, 0, 25);
        Segment seg2 = new PercentSegment(mag_kcal, 25, 47.5);
        Segment seg3 = new PercentSegment(mag_kcal, 47.5, 52.5);
        Segment seg4 = new PercentSegment(mag_kcal, 52.5, 100);
        mag_kcal.segments().addAll(seg1, seg2, seg3, seg4);

        seg1 = new PercentSegment(mag_protein, 0, 25);
        seg2 = new PercentSegment(mag_protein, 25, 47.5);
        seg3 = new PercentSegment(mag_protein, 47.5, 52.5);
        seg4 = new PercentSegment(mag_protein, 52.5, 100);
        mag_protein.segments().addAll(seg1, seg2, seg3, seg4);

        seg1 = new PercentSegment(mag_carbs, 0, 25);
        seg2 = new PercentSegment(mag_carbs, 25, 47.5);
        seg3 = new PercentSegment(mag_carbs, 47.5, 52.5);
        seg4 = new PercentSegment(mag_carbs, 52.5, 100);
        mag_carbs.segments().addAll(seg1, seg2, seg3, seg4);

        seg1 = new PercentSegment(mag_fat, 0, 25);
        seg2 = new PercentSegment(mag_fat, 25, 47.5);
        seg3 = new PercentSegment(mag_fat, 47.5, 52.5);
        seg4 = new PercentSegment(mag_fat, 52.5, 100);
        mag_fat.segments().addAll(seg1, seg2, seg3, seg4);

        // =====================
        // Meal Tab
        // =====================
        lv_meals.setCellFactory(cell -> {
            final MealCell mealCell = new MealCell(
                    c -> {
                        System.out.println("left");
                        c.getMealFoods().forEach(mf -> {
                            BodyArchitect.getInstance().deleteEntry(MealFood.class, mf.getId());
                        });
                        BodyArchitect.getInstance().deleteEntry(Meal.class, c.getId());
                        BodyArchitect.getInstance().setUpdateMeal(true);
                        updateMeal();
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
                        try {
                            BodyArchitect.getInstance().deleteEntry(Food.class, c.getId());
                            BodyArchitect.getInstance().setUpdateFood(true);
                            foodCell.collapse();
                            updateFood();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        vbox_logs.getChildren().add(BodyArchitect.getInstance().getLogBook().getWrapper());

        // listener for the selectedDay
        BodyArchitect.getInstance().getLogBook().getSelectedDayProperty().addListener((obs, ov, nv) -> {
            // @todo only switch if the day has data
            if (nv != null) {
                BodyArchitect.getInstance().setUpdateDay(true);
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
                    appBar.getActionItems().setAll(btn_addMealToDay);
                    break;
                case 1:
                    // meal
                    appBar.getActionItems().setAll(btn_addMeal);
                    break;
                case 2:
                    // food
                    appBar.getActionItems().setAll(btn_addFood);
                    break;
                case 3:
                    // logs
                    appBar.getActionItems().clear();
                    break;
            }
        });
    }
}