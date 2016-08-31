/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect;

import de.seibushin.bodyArchitect.helper.MsgUtil;
import de.seibushin.bodyArchitect.model.nutrition.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MealAddController {
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
    Label lbl_result;

    @FXML
    private void addMeal(ActionEvent actionEvent) {
        try {
            String name = tf_name.getText();
            Type type = cb_type.getValue();

            Meal meal = new Meal(name, type);
            BodyArchitect.getBa().addEntry(meal);

            lv_food.getItems().stream().forEach(f -> {
                MealFood mf = new MealFood();
                mf.setMeal(meal);
                mf.setFood(f);
                mf.setWeight(f.getPortion());
                BodyArchitect.getBa().addEntry(mf);
            });

            //todo: empty all fields!!!

            lbl_result.setText(MsgUtil.getString("addMeal_success"));
        } catch (Exception e) {
            e.printStackTrace();
            lbl_result.setText(MsgUtil.getString("addMeal_error"));
        }
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
        cb_type.getItems().setAll(Type.values());
        BodyArchitect.getBa().refreshListView(lv_allFood, Food.class);
    }
}
