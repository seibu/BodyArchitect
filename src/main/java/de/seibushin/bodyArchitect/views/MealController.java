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
import de.seibushin.bodyArchitect.Main;
import de.seibushin.bodyArchitect.helper.MsgUtil;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import de.seibushin.bodyArchitect.model.nutrition.MealFood;
import de.seibushin.bodyArchitect.model.nutrition.Type;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MealController {
    @FXML
    TextField tf_name;
    @FXML
    ComboBox<Type> cb_type;
    @FXML
    ListView<Food> lv_food;
    @FXML
    ListView<Food> lv_allFood;
    @FXML
    TextField tf_weight;

    @FXML
    View meal;

    private SnackbarPopupView snackbarPopupView = new SnackbarPopupView();

    private void addMeal() {
        String result = "";
        try {
            String name = tf_name.getText();
            Type type = cb_type.getValue();

            Meal meal = new Meal(name, type);
            BodyArchitect.getInstance().addEntry(meal);

            lv_food.getItems().stream().forEach(f -> {
                MealFood mf = new MealFood();
                mf.setMeal(meal);
                mf.setFood(f);
                mf.setWeight(f.getPortion());
                BodyArchitect.getInstance().addEntry(mf);
            });

            //todo: empty all fields!!!

            result = MsgUtil.getString("addMeal_success");
        } catch (Exception e) {
            e.printStackTrace();
            result = MsgUtil.getString("addMeal_error");
        }

        snackbarPopupView.show(result);
    }

    @FXML
    private void addFood(ActionEvent actionEvent) {
        Food food = lv_allFood.getSelectionModel().getSelectedItem();

        if (food != null) {
            food.setPortion(Integer.parseInt(tf_weight.getText()));

            lv_allFood.getItems().remove(food);
            lv_food.getItems().add(food);
        }
    }

    @FXML
    private void removeFood(ActionEvent actionEvent) {
        Food food = lv_food.getSelectionModel().getSelectedItem();

        if (food != null) {
            lv_food.getItems().remove(food);
            lv_allFood.getItems().add(food);
        }
    }

    @FXML
    private void initialize() {
        meal.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();

                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e ->  {
                    MobileApplication.getInstance().switchToPreviousView();
                }));
                appBar.getActionItems().add(MaterialDesignIcon.ADD.button(e -> addMeal()));

                // set Title
                appBar.setTitleText("Add Meal");
            }
        });

        MobileApplication.getInstance().addLayerFactory("snackbar2", () -> snackbarPopupView);

        cb_type.getItems().setAll(Type.values());
        BodyArchitect.getInstance().refreshListView(lv_allFood, Food.class);
    }

}
