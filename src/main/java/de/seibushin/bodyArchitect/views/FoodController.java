/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.AutoCompleteTextField;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.layout.layer.SnackbarPopupView;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.helper.Utils;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import javafx.fxml.FXML;

public class FoodController {
    @FXML
    TextField tf_name;
    @FXML
    TextField tf_weight;
    @FXML
    TextField tf_portion;
    @FXML
    TextField tf_kcal;
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
            Double portion = Utils.getNumberFormat().parse(tf_portion.getText()).doubleValue();
            Double kcal = Utils.getNumberFormat().parse(tf_kcal.getText()).doubleValue();
            Double fat = Utils.getNumberFormat().parse(tf_fat.getText()).doubleValue();
            Double carbs = Utils.getNumberFormat().parse(tf_carbs.getText()).doubleValue();
            Double sugar = Utils.getNumberFormat().parse(tf_sugar.getText()).doubleValue();
            Double protein = Utils.getNumberFormat().parse(tf_protein.getText()).doubleValue();

            // add the food
            Food food = new Food(name, weight, portion, kcal, protein, fat, carbs, sugar);
            Service.getInstance().addFood(food);

            // clear the fields
            clear();

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
        tf_kcal.setText("");
        tf_fat.setText("");
        tf_carbs.setText("");
        tf_sugar.setText("");
        tf_protein.setText("");
        tf_weight.setText("");
        tf_portion.setText("");
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

        /*// this wont work for mobile since javafxports is not supporting streams
        atf_name.setCompleter(s -> Service.getInstance().getFoods().filtered(f ->  f.getName().toLowerCase().startsWith(s.toLowerCase())));

        atf_name.valueProperty().addListener((obs, ov, nv) -> {
            atf_name.setText(nv.getName());
        });*/

        MobileApplication.getInstance().addLayerFactory("snackbar3", () -> snackbarPopupView);
    }
}
