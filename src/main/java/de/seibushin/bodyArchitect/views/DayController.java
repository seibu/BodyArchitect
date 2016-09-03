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
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class DayController {

    @FXML
    private View day;

    @FXML
    ListView<Meal> lv_day;

    SnackbarPopupView snackbarPopupView = new SnackbarPopupView();

    private void addMeal() {
        System.out.println("addMeal " + lv_day.getSelectionModel().getSelectedItem());
    }

    public void initialize() {
        day.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();

                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> MobileApplication.getInstance().switchToPreviousView()));
                appBar.getActionItems().add(MaterialDesignIcon.ADD.button(e -> addMeal()));

                appBar.setTitleText("Add Meal to Day");
            }
        });

        day.getLayers().add(snackbarPopupView);

        lv_day.setCellFactory(c -> new MealCell());

        BodyArchitect.getBa().refreshListView(lv_day, Meal.class);
    }
}
