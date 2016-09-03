/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.helper;

import de.seibushin.bodyArchitect.model.nutrition.Type;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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

    public MealCellNode() {
        FXMLLoader fxmlLoader = new FXMLLoader(MealCellNode.class.getResource("MealCellNode.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String name, Type type, String Food, String kcal, String fat, String carbs, String sugar, String protein) {
        lbl_name.setText(name + " - " + type);
        lbl_kcal.setText(kcal);
        lbl_fat.setText(fat);
        lbl_carbs.setText(carbs);
        lbl_sugar.setText(sugar);
        lbl_protein.setText(protein);
    }

    public VBox getNode() {
        return vbox;
    }
}
