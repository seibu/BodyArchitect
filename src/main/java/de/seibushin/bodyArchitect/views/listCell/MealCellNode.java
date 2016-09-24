/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views.listCell;

import com.gluonhq.charm.glisten.control.Icon;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.helper.Utils;
import de.seibushin.bodyArchitect.model.nutrition.DayFood;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import de.seibushin.bodyArchitect.model.nutrition.SimpleMeal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MealCellNode {
    @FXML
    public VBox mealCell;
    @FXML
    public Label lbl_name;
    @FXML
    private Label lbl_type;
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
    Icon i_info;

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

    public void update(SimpleMeal item) {
        if (item.isMeal()) {
            lbl_type.setText(item.getType().toString());
        } else {
            lbl_type.setText(((DayFood)item).getWeight() + " " + Utils.getString("food.cell.weight.unit"));
            i_info.setDisable(true);
        }
        lbl_name.setText(item.getName());


        lbl_kcal.setText(Service.getInstance().getDf().format(item.getKcal()) + " " + Utils.getString("meal.cell.kcal"));
        lbl_fat.setText(Service.getInstance().getDf().format(item.getFat()));
        lbl_carbs.setText(Service.getInstance().getDf().format(item.getCarbs()));
        lbl_sugar.setText(Service.getInstance().getDf().format(item.getSugar()));
        lbl_protein.setText(Service.getInstance().getDf().format(item.getProtein()));
    }

    public VBox getNode() {
        return mealCell;
    }
}
