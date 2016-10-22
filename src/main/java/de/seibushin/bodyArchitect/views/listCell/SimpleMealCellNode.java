/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views.listCell;

import com.gluonhq.charm.glisten.control.Icon;
import com.gluonhq.charm.glisten.control.ListTile;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.helper.Utils;
import de.seibushin.bodyArchitect.model.nutrition.DayFood;
import de.seibushin.bodyArchitect.model.nutrition.SimpleMeal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import java.io.IOException;

public class SimpleMealCellNode {
    @FXML
    public ListTile tile;
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

    private SimpleMeal item;

    public SimpleMealCellNode() {
        FXMLLoader fxmlLoader = new FXMLLoader(SimpleMealCellNode.class.getResource("simpleMealCellNode.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(Utils.getBundle());
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SimpleMeal getItem() {
        return item;
    }

    public void refresh() {
        System.out.println("refresh");
        update(item);
    }

    public void update(SimpleMeal item) {
        this.item = item;

        if (item.isMeal()) {
            lbl_type.setText(item.getType().toString());
        } else {
            lbl_type.setText(item.getWeight() + " " + Utils.getString("food.cell.weight.unit"));
            i_info.setDisable(true);
        }
        lbl_name.setText(item.getName());


        lbl_kcal.setText(Service.getInstance().getDf().format(item.getKcal()) + " " + Utils.getString("meal.cell.kcal"));
        lbl_fat.setText(Service.getInstance().getDf().format(item.getFat()));
        lbl_carbs.setText(Service.getInstance().getDf().format(item.getCarbs()));
        lbl_sugar.setText(Service.getInstance().getDf().format(item.getSugar()));
        lbl_protein.setText(Service.getInstance().getDf().format(item.getProtein()));
    }

    public ListTile getNode() {
        return tile;
    }
}
