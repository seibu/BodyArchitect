/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect.views;

import de.seibushin.bodyArchitect.BodyArchitect;
import de.seibushin.bodyArchitect.helper.MsgUtil;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FoodAddController {
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
    Label lbl_result;

    @FXML
    private void addFood(ActionEvent actionEvent) {
        try {
            String name = tf_name.getText();
            Double weight = Double.valueOf(tf_weight.getText());
            Double energy = Double.valueOf(tf_energy.getText());
            Double fat = Double.valueOf(tf_fat.getText());
            Double carbs = Double.valueOf(tf_carbs.getText());
            Double sugar = Double.valueOf(tf_sugar.getText());
            Double protein = Double.valueOf(tf_protein.getText());

            Food food = new Food(name, energy, fat, carbs, sugar, protein, weight, 0);

            BodyArchitect.getInstance().addEntry(food);
            lbl_result.setText(MsgUtil.getString("addFood_success"));
        } catch (Exception e) {
            e.printStackTrace();
            lbl_result.setText(MsgUtil.getString("addFood_error"));
        }
    }
}
