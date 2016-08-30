/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect;

import de.seibushin.bodyArchitect.helper.MsgUtil;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class FoodController {
    @FXML
    ListView<Food> lv_food;
    @FXML
    TextField tf_weight;

    ListView<Food> mealFoods;

    @FXML
    private void addFood(ActionEvent actionEvent) {
        Food food = lv_food.getSelectionModel().getSelectedItem();

        food.setPortion(Integer.parseInt(tf_weight.getText()));

        mealFoods.getItems().add(food);
    }

    public void setListView(ListView mealFoods) {
        this.mealFoods = mealFoods;
    }

    @FXML
    private void initialize() {
        BodyArchitect.getBa().refreshListView(lv_food, Food.class);
    }
}
