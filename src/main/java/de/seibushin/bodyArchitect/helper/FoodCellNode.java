/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.helper;

import com.gluonhq.charm.glisten.control.Icon;
import de.seibushin.bodyArchitect.BodyArchitect;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
    Label lbl_fat;
    @FXML
    Label lbl_carbs;
    @FXML
    Label lbl_sugar;
    @FXML
    Label lbl_protein;
    @FXML
    Label lbl_weight;

    public FoodCellNode() {
        FXMLLoader fxmlLoader = new FXMLLoader(FoodCellNode.class.getResource("FoodCellNode.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(MsgUtil.getBundle());
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
        lbl_kcal.setText(BodyArchitect.getInstance().getDf().format(item.getKcal() / item.getWeight() * item.getPortion()));
        lbl_fat.setText(BodyArchitect.getInstance().getDf().format(item.getFat() / item.getWeight() * item.getPortion()));
        lbl_carbs.setText(BodyArchitect.getInstance().getDf().format(item.getCarbs() / item.getWeight() * item.getPortion()));
        lbl_sugar.setText(BodyArchitect.getInstance().getDf().format(item.getSugar() / item.getWeight() * item.getPortion()));
        lbl_protein.setText(BodyArchitect.getInstance().getDf().format(item.getProtein() / item.getWeight() * item.getPortion()));
        lbl_weight.setText(BodyArchitect.getInstance().getDf().format(item.getPortion()));
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
