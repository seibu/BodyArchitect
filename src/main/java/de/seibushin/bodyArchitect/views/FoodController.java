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
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import de.seibushin.bodyArchitect.BodyArchitect;
import de.seibushin.bodyArchitect.helper.Utils;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import javafx.fxml.FXML;


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
            Double weight = Utils.getNumberFormat().parse(tf_weight.getText()).doubleValue();
            Double energy = Utils.getNumberFormat().parse(tf_energy.getText()).doubleValue();
            Double fat = Utils.getNumberFormat().parse(tf_fat.getText()).doubleValue();
            Double carbs = Utils.getNumberFormat().parse(tf_carbs.getText()).doubleValue();
            Double sugar = Utils.getNumberFormat().parse(tf_sugar.getText()).doubleValue();
            Double protein = Utils.getNumberFormat().parse(tf_protein.getText()).doubleValue();
            // @todo check if we need portion in any real way or if this is actually fine
            Double portion = weight;

            // add the food
            Food food = new Food(name, energy, fat, carbs, sugar, protein, weight, portion);
            BodyArchitect.getInstance().addEntry(food);

            // clear the fields
            clear();

            // we need to update the FoodView
            BodyArchitect.getInstance().setUpdateFood(true);
            result = Utils.getString("addFood_success");
        } catch (Exception e) {
            e.printStackTrace();
            result = Utils.getString("addFood_error");
        }

        snackbarPopupView.show(result);
    }

    private void clear() {
        // clear the fields for a new food to be added
        tf_name.setText("");
        tf_carbs.setText("");
        tf_energy.setText("");
        tf_fat.setText("");
        tf_carbs.setText("");
        tf_sugar.setText("");
        tf_protein.setText("");
        tf_weight.setText("");
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
