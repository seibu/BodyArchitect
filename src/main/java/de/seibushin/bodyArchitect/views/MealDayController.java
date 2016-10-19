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
import com.gluonhq.charm.glisten.layout.layer.SnackbarPopupView;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.model.nutrition.*;
import de.seibushin.bodyArchitect.model.nutrition.plan.Plan;
import de.seibushin.bodyArchitect.model.nutrition.plan.PlanDay;
import de.seibushin.bodyArchitect.views.listCell.FoodCell;
import de.seibushin.bodyArchitect.views.listCell.MealCell;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.input.ScrollEvent;

public class MealDayController {
    private static boolean forPlan = false;

    @FXML
    private View meal_day;
    @FXML
    CharmListView<SimpleMeal, String> lv_meals;
    @FXML
    CharmListView<Food, String> lv_food;
    @FXML
    TabPane tp_simpleMeals;
    @FXML
    Slider slider_portion;

    private BooleanProperty sliding = new SimpleBooleanProperty();

    public static void setForPlan(boolean nv) {
        forPlan = nv;
    }


    private void addMeal() {
        String result = "Meal could not be added, please try again";
        try {
            // get the selected meal
            //SimpleMeal meal = lv_meals.getSelectionModel().getSelectedItem();
            SimpleMeal meal = ((ListView<SimpleMeal>) lv_meals.lookup(".list-view")).getSelectionModel().getSelectedItem();

            if (meal != null) {
                DayMeal dayMeal = new DayMeal(meal);
                // get the selected date

                //@todo check if we are using this for a plan or for a day
                if (forPlan) {

                    //@todo get planId of selectedPlan !?
                    NutritionPlansController.getSelectedPlanDay().addMeal(dayMeal);
                    Service.getInstance().addSimpleMealToPlanDay(dayMeal, NutritionPlansController.getSelectedPlanDay().getId());
                    result = "Meal added to selected Day of Plan";
                } else {
                    Day day = Service.getInstance().getSelectedDayObject();
                    if (day == null) {
                        day = new Day();
                        day.setDate(Service.getInstance().getSelectedDay());
                        Service.getInstance().addDay(day);
                    }
                    //@todo dont use day.getId() it might be for a plan and not for the active Day!!!
                    day.addMeal(dayMeal);
                    Service.getInstance().addSimpleMealToDay(dayMeal, day.getId());
                    result = "Meal added to selected Date " + Service.getInstance().getSelectedDayString();
                }
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
            Food food = ((ListView<Food>) lv_food.lookup(".list-view")).getSelectionModel().getSelectedItem();
            if (food != null) {
                DayFood dayFood = new DayFood(food, food.getPortion());

                //@todo check if we are using this for a plan or for a day
                if (forPlan) {

                    NutritionPlansController.getSelectedPlanDay().addMeal(dayFood);
                    Service.getInstance().addSimpleMealToPlanDay(dayFood, NutritionPlansController.getSelectedPlanDay().getId());
                    result = "Meal added to selected Day of Plan";
                } else {
                    // get the selected date
                    Day day = Service.getInstance().getSelectedDayObject();
                    if (day == null) {
                        day = new Day();
                        day.setDate(Service.getInstance().getSelectedDay());
                        Service.getInstance().addDay(day);
                    }
                    day.addMeal(dayFood);
                    Service.getInstance().addSimpleMealToDay(dayFood, day.getId());
                    // check this shit
                    //Service.getInstance().addDay(day);
                    result = "Food added to selected Date " + Service.getInstance().getSelectedDayString();
                }
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
        lv_meals.setComparator(((o1, o2) -> o1.getName().compareTo(o2.getName())));

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

        // use a filterdList to only show Food which is a Snack in the list
        // if the wrappedList is updated the changes are although shown in the filteredList
        lv_food.setComparator(((o1, o2) -> o1.getName().compareTo(o2.getName())));
        FilteredList<Food> filteredList = new FilteredList(Service.getInstance().getFoods());
        filteredList.setPredicate(n -> n.isSnack());
        lv_food.setItems(filteredList);

        // delay enough to be able to get the list-view of the charmlistview
        Platform.runLater(() -> {
            // portion slider
            slider_portion.valueProperty().addListener(e -> {
                if (((ListView<Food>) lv_food.lookup(".list-view")).getSelectionModel().getSelectedItem() != null) {
                    ((ListView<Food>) lv_food.lookup(".list-view")).getSelectionModel().getSelectedItem().setPortion(Math.ceil(slider_portion.getValue()));
                    // @todo remove for better solution
                    ((ListView<Food>) lv_food.lookup(".list-view")).refresh();
                }
            });

            ((ListView<Food>) lv_food.lookup(".list-view")).getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue != null) {
                    slider_portion.setValue(newValue.getPortion());
                } else {
                    slider_portion.setValue(0);
                }
            });
        });
    }
}
