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
import de.seibushin.bodyArchitect.views.listCell.MealCell;
import de.seibushin.bodyArchitect.model.nutrition.Day;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.ScrollEvent;

public class MealDayController {

    @FXML
    private View meal_day;

    @FXML
    ListView<Meal> lv_meals;

    private SnackbarPopupView snackbarPopupView = new SnackbarPopupView();
    private BooleanProperty sliding = new SimpleBooleanProperty();

    private void addMeal() {
        String result = "";
        try {
            // get the selected meal
            Meal meal = lv_meals.getSelectionModel().getSelectedItem();
            // get the selected date
            Day day = Service.getInstance().getSelectedDayObject();
            if (day == null) {
                day = new Day();
                day.setDate(Service.getInstance().getSelectedDay());
            }
            day.addMeal(meal);
            Service.getInstance().addDay(day);

            result = "Meal added to selected Date " + Service.getInstance().getSelectedDayString();
        } catch (Exception e) {
            e.printStackTrace();
            result = "Meal could not be added, please try again";
        }

        //MobileApplication.getInstance().showLayer("snackbar");
        snackbarPopupView.show(result);
        MobileApplication.getInstance().showLayer("snackbar");
    }

    @FXML
    public void initialize() {
        meal_day.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();

                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e ->  {
                    MobileApplication.getInstance().switchToPreviousView();

                }));

                appBar.getActionItems().add(MaterialDesignIcon.ADD.button(e -> addMeal()));
                appBar.setTitleText("Add Meal to Day");
            }
        });

        MobileApplication.getInstance().addLayerFactory("snackbar", () -> snackbarPopupView);

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
    }
}
