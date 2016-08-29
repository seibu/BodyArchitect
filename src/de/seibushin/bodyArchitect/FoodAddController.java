/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect;

import de.seibushin.bodyArchitect.helper.ColumnUtil;
import de.seibushin.bodyArchitect.helper.MsgUtil;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class FoodAddController {
    @FXML
    TextField tf_name;
    @FXML
    TextField tf_portion;
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
    Label lbl_energy;
    @FXML
    Label lbl_fat;
    @FXML
    Label lbl_carbs;
    @FXML
    Label lbl_sugar;
    @FXML
    Label lbl_protein;
    @FXML
    Label lbl_result;

    @FXML
    private void addFood(ActionEvent actionEvent) {
        try {
            String name = tf_name.getText();
            Double portion = Double.valueOf(tf_portion.getText());
            Double energy = Double.valueOf(tf_energy.getText());
            Double fat = Double.valueOf(tf_fat.getText());
            Double carbs = Double.valueOf(tf_carbs.getText());
            Double sugar = Double.valueOf(tf_sugar.getText());
            Double protein = Double.valueOf(tf_protein.getText());

            Food food = new Food(name, energy, fat, carbs, sugar, protein, 100, portion);

            BodyArchitect.getBa().addEntry(food);
            lbl_result.setText(MsgUtil.getString("addFood_success"));
        } catch (Exception e) {
            e.printStackTrace();
            lbl_result.setText(MsgUtil.getString("addFood_error"));
        }
    }

}
