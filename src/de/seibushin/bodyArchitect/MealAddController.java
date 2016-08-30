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
import de.seibushin.bodyArchitect.model.nutrition.MealFood;
import de.seibushin.bodyArchitect.model.nutrition.Type;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MealAddController {
    @FXML
    TextField tf_name;
    @FXML
    ComboBox<Type> cb_type;
    @FXML
    ListView<Food> lv_food;

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
                mf.setWeight((int)f.getPortion());
                BodyArchitect.getBa().addEntry(mf);
            });

            lbl_result.setText(MsgUtil.getString("addMeal_success"));
        } catch (Exception e) {
            e.printStackTrace();
            lbl_result.setText(MsgUtil.getString("addMeal_error"));
        }
    }

    @FXML
    private void addFood(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(Config.FXML_FOOD));
            Stage stage = new Stage();
            stage.setTitle(MsgUtil.getString("title"));

            stage.setScene(new Scene(loader.load()));

            FoodController fC = loader.getController();
            fC.setListView(lv_food);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        cb_type.getItems().setAll(Type.values());
    }
}
