/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.layout.layer.SnackbarPopupView;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.model.nutrition.*;
import de.seibushin.bodyArchitect.views.listCell.FoodCell;
import de.seibushin.bodyArchitect.views.listCell.MealCell;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.input.ScrollEvent;

public class MealDayController {
    @FXML
    private View meal_day;
    @FXML
    ListView<SimpleMeal> lv_meals;
    @FXML
    ListView<Food> lv_food;
    @FXML
    TabPane tp_simpleMeals;
    @FXML
    Slider slider_portion;

    private SnackbarPopupView snackbarPopupView = new SnackbarPopupView();
    private BooleanProperty sliding = new SimpleBooleanProperty();

    private void addMeal() {
        String result = "Meal could not be added, please try again";
        try {
            // get the selected meal
            SimpleMeal meal = lv_meals.getSelectionModel().getSelectedItem();
            if (meal != null) {
                DayMeal dayMeal = new DayMeal(meal);
                // get the selected date
                Day day = Service.getInstance().getSelectedDayObject();
                if (day == null) {
                    day = new Day();
                    day.setDate(Service.getInstance().getSelectedDay());
                }
                day.addMeal(dayMeal);
                // check this shit
                Service.getInstance().addDay(day);
                result = "Meal added to selected Date " + Service.getInstance().getSelectedDayString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MobileApplication.getInstance().showMessage(result);
    }

    private void addFood() {
        String result = "Food could not be added, please try again";
        try {
            // get the selected food
            Food food = lv_food.getSelectionModel().getSelectedItem();
            if (food != null) {
                DayFood dayFood = new DayFood(food, food.getPortion());
                // get the selected date
                Day day = Service.getInstance().getSelectedDayObject();
                if (day == null) {
                    day = new Day();
                    day.setDate(Service.getInstance().getSelectedDay());
                }
                day.addMeal(dayFood);
                // check this shit
                Service.getInstance().addDay(day);
                result = "Food added to selected Date " + Service.getInstance().getSelectedDayString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MobileApplication.getInstance().showMessage(result);
    }

    private void addSimpleMeal() {
        switch (tp_simpleMeals.getSelectionModel().getSelectedIndex()) {
            case 0:
                addMeal();
                break;
            case 1:
                addFood();
                break;
        }
    }

    @FXML
    public void initialize() {
        meal_day.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();

                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> {
                    MobileApplication.getInstance().switchToPreviousView();

                }));

                appBar.getActionItems().add(MaterialDesignIcon.ADD.button(e -> addSimpleMeal()));
                appBar.setTitleText("Add Meal to Day");
            }
        });

        // ================== meal listview ======================

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

        lv_meals.setItems(Service.getInstance().getMeals());

        // ======================= food listview =======================

        lv_food.setCellFactory(cell -> {
            final FoodCell foodCell = new FoodCell(true);
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

        lv_food.setItems(Service.getInstance().getFoods());

        // portion slider

        slider_portion.valueProperty().addListener(e -> {
            if (lv_food.getSelectionModel().getSelectedItem() != null) {
                lv_food.getSelectionModel().getSelectedItem().setPortion(Math.ceil(slider_portion.getValue()));
                // @todo search way to remove this
                lv_food.refresh();
            }
        });

        lv_food.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                slider_portion.setValue(newValue.getPortion());
            } else {
                slider_portion.setValue(0);
            }
        });
    }
}
