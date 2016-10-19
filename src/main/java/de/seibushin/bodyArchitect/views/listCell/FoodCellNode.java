/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views.listCell;

import com.gluonhq.charm.glisten.control.ListTile;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.helper.Utils;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import java.io.IOException;

public class FoodCellNode {
    @FXML
    public ListTile tile;
    @FXML
    Label lbl_name;
    @FXML
    Label lbl_kcal;
    @FXML
    Label lbl_type;
    @FXML
    Label lbl_fat;
    @FXML
    Label lbl_carbs;
    @FXML
    Label lbl_sugar;
    @FXML
    Label lbl_protein;

    private boolean usePortion;

    public FoodCellNode(boolean usePortion) {
        this.usePortion = usePortion;
        FXMLLoader fxmlLoader = new FXMLLoader(FoodCellNode.class.getResource("simpleMealCellNode.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(Utils.getBundle());
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateStats(Food item) {
        lbl_kcal.setText(Service.getInstance().getDf().format(item.getKcal()) + " " + Utils.getString("food.cell.kcal"));
        lbl_type.setText(Service.getInstance().getDf().format(item.getWeight()) + Utils.getString("food.cell.weight.unit"));

        lbl_fat.setText(Service.getInstance().getDf().format(item.getFat()));
        lbl_carbs.setText(Service.getInstance().getDf().format(item.getCarbs()));
        lbl_sugar.setText(Service.getInstance().getDf().format(item.getSugar()));
        lbl_protein.setText(Service.getInstance().getDf().format(item.getProtein()));
    }

    private void updateStatsWithPortion(Food item) {
        lbl_kcal.setText(Service.getInstance().getDf().format(item.getPortionKcal()) + " " + Utils.getString("food.cell.kcal"));
        lbl_type.setText(Service.getInstance().getDf().format(item.getPortion()) + Utils.getString("food.cell.weight.unit"));

        lbl_fat.setText(Service.getInstance().getDf().format(item.getPortionFat()));
        lbl_carbs.setText(Service.getInstance().getDf().format(item.getPortionCarbs()));
        lbl_sugar.setText(Service.getInstance().getDf().format(item.getPortionSugar()));
        lbl_protein.setText(Service.getInstance().getDf().format(item.getPortionProtein()));
    }

    public void update(Food item) {
        lbl_name.setText(item.getName());
        if (usePortion) {
            updateStatsWithPortion(item);
        } else {
            updateStats(item);
        }
    }

    public ListTile getNode() {
        return tile;
    }
}
