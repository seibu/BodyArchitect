/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.helper;

import de.seibushin.bodyArchitect.model.nutrition.Meal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MealCellNode {
    @FXML
    public VBox vbox;
    @FXML
    public Label lbl_name;
    @FXML
    public Label lbl_kcal;
    @FXML
    public Label lbl_fat;
    @FXML
    public Label lbl_carbs;
    @FXML
    public Label lbl_sugar;
    @FXML
    public Label lbl_protein;
    @FXML
    VBox vb_foods;

    public MealCellNode() {
        FXMLLoader fxmlLoader = new FXMLLoader(MealCellNode.class.getResource("MealCellNode.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(Utils.getBundle());
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        // hide the foods
        vbox.getChildren().remove(vb_foods);
    }

    public void update(Meal item) {
        lbl_name.setText(item.getName() + " - " + item.getType());
        lbl_kcal.setText(item.getKcal());
        lbl_fat.setText(item.getFat());
        lbl_carbs.setText(item.getCarbs());
        lbl_sugar.setText(item.getSugar());
        lbl_protein.setText(item.getProtein());

        item.getMealFoods().forEach(mf -> {
            vb_foods.getChildren().add(new Label(mf.getFood().getName() + " - " + mf.getWeight()));
        });
    }

    public VBox getNode() {
        return vbox;
    }
}
