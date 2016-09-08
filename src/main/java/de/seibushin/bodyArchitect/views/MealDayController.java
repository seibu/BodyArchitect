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
import de.seibushin.bodyArchitect.BodyArchitect;
import de.seibushin.bodyArchitect.helper.MealCell;
import de.seibushin.bodyArchitect.model.nutrition.Day;
import de.seibushin.bodyArchitect.model.nutrition.DayMeal;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.time.LocalDate;
import java.util.List;

public class MealDayController {

    @FXML
    private View meal_day;

    @FXML
    ListView<Meal> lv_meals;

    private SnackbarPopupView snackbarPopupView = new SnackbarPopupView();

    private void addMeal() {
        String result = "";
        try {
            System.out.println("addMeal " + lv_meals.getSelectionModel().getSelectedItem());

            // get the selected meal
            Meal meal = lv_meals.getSelectionModel().getSelectedItem();
            // get the selected date
            LocalDate date = BodyArchitect.getInstance().getSelectedDay();

            List<Day> days = BodyArchitect.getInstance().getEntry(Day.class, date);
            Day day;
            if (days.size() == 0) {
                day = new Day();
                day.setDate(date);
                BodyArchitect.getInstance().addEntry(day);
            } else {
                day = days.get(0);
            }

            DayMeal dm = new DayMeal();
            dm.setDay(day);
            dm.setMeal(meal);

            BodyArchitect.getInstance().addEntry(dm);
            BodyArchitect.getInstance().setUpdateDay(true);
            result = "Meal added to selected Date " + BodyArchitect.getInstance().getSelectedDayString();
        } catch (Exception e) {
            e.printStackTrace();
            result = "Meal could not be added, please try again";
        }

        //MobileApplication.getInstance().showLayer("snackbar");
        snackbarPopupView.show(result);
    }

    @FXML
    public void initialize() {
        meal_day.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();

                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e ->  {
                    System.out.println("show previous");
                    MobileApplication.getInstance().switchToPreviousView();

                }));
                appBar.getActionItems().add(MaterialDesignIcon.ADD.button(e -> addMeal()));

                appBar.setTitleText("Add Meal to Day");

                // @todo check if necessary
                BodyArchitect.getInstance().refreshListView(lv_meals, Meal.class);
            }
        });

        MobileApplication.getInstance().addLayerFactory("snackbar", () -> snackbarPopupView);

        lv_meals.setCellFactory(c -> new MealCell());
    }
}
