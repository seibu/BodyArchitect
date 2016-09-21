/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views.listCell;

import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.helper.Utils;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class FoodCellNode {
    @FXML
    public VBox vbox;
    @FXML
    Label lbl_name;
    @FXML
    Label lbl_kcal;
    @FXML
    Label lbl_weight;
    @FXML
    Label lbl_fat;
    @FXML
    Label lbl_carbs;
    @FXML
    Label lbl_sugar;
    @FXML
    Label lbl_protein;


    public FoodCellNode() {
        FXMLLoader fxmlLoader = new FXMLLoader(FoodCellNode.class.getResource("mealFoodCellNode.fxml"));
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
        //
    }

    private void updateStats(Food item) {
        lbl_kcal.setText(Service.getInstance().getDf().format(item.getKcal()) + " " + Utils.getString("food.cell.kcal"));
        lbl_weight.setText(Service.getInstance().getDf().format(item.getWeight()) + Utils.getString("food.cell.weight.unit"));

        lbl_fat.setText(Service.getInstance().getDf().format(item.getFat()));
        lbl_carbs.setText(Service.getInstance().getDf().format(item.getCarbs()));
        lbl_sugar.setText(Service.getInstance().getDf().format(item.getSugar()));
        lbl_protein.setText(Service.getInstance().getDf().format(item.getProtein()));
    }

    public void update(Food item) {
        lbl_name.setText(item.getName());
        updateStats(item);
    }

    @FXML
    private void addFood() {
        // @todo check possibility to move this to the other listview
        System.out.println("addFood");

    }

    public VBox getNode() {
        return vbox;
    }
}
