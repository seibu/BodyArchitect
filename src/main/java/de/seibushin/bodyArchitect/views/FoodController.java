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
import de.seibushin.bodyArchitect.helper.MsgUtil;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class FoodController {
    @FXML
    TextField tf_name;
    @FXML
    TextField tf_weight;
    @FXML
    TextField tf_energy;
    @FXML
    TextField tf_fat;
    @FXML
    TextField tf_carbs;
    @FXML
    TextField tf_sugar;
    @FXML
    TextField tf_protein;

    @FXML
    View food;

    private SnackbarPopupView snackbarPopupView = new SnackbarPopupView();

    private void addFood() {
        String result = "";
        try {
            String name = tf_name.getText();
            Double weight = Double.valueOf(tf_weight.getText());
            Double energy = Double.valueOf(tf_energy.getText());
            Double fat = Double.valueOf(tf_fat.getText());
            Double carbs = Double.valueOf(tf_carbs.getText());
            Double sugar = Double.valueOf(tf_sugar.getText());
            Double protein = Double.valueOf(tf_protein.getText());
            // @todo check if we need portion in any real way or if this is actually fine
            Double portion = weight;

            // add the food
            Food food = new Food(name, energy, fat, carbs, sugar, protein, weight, portion);
            BodyArchitect.getInstance().addEntry(food);

            // clear the fields
            clear();

            // we need to update the FoodView
            BodyArchitect.getInstance().setUpdateFood(true);
            result = MsgUtil.getString("addFood_success");
        } catch (Exception e) {
            e.printStackTrace();
            result = MsgUtil.getString("addFood_error");
        }

        snackbarPopupView.show(result);
    }

    private void clear() {
        // clear the fields for a new food to be added
        tf_name.clear();
        tf_carbs.clear();
        tf_energy.clear();
        tf_fat.clear();
        tf_carbs.clear();
        tf_sugar.clear();
        tf_protein.clear();
        tf_weight.clear();
    }

    @FXML
    private void initialize() {
        food.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();

                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> {
                    MobileApplication.getInstance().switchToPreviousView();
                }));
                appBar.getActionItems().add(MaterialDesignIcon.ADD.button(e -> addFood()));

                // set Title
                appBar.setTitleText("Add Food");
            }
        });



        MobileApplication.getInstance().addLayerFactory("snackbar3", () -> snackbarPopupView);
    }
}
