/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views.listCell;

import com.gluonhq.charm.glisten.control.Icon;
import com.gluonhq.charm.glisten.control.ListTile;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.helper.Utils;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import de.seibushin.bodyArchitect.model.nutrition.plan.Plan;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PlanCellNode {
    @FXML
    ListTile tile;
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
    Icon i_selected;
    @FXML
    Button btn_select;

    @FXML
    public void initialize() {
    }

    @FXML
    public void selectPlan(Event event) {
        System.out.println(event.getSource());
        System.out.println(event.getTarget());
    }

    public PlanCellNode() {
        FXMLLoader fxmlLoader = new FXMLLoader(PlanCellNode.class.getResource("planCellNode.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(Utils.getBundle());
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Plan item) {
        lbl_name.setText(item.getName());
        lbl_kcal.setText(Service.getInstance().getDf().format(item.getKcal()) + " " + Utils.getString("meal.cell.kcal"));
        lbl_fat.setText(Service.getInstance().getDf().format(item.getFat()));
        lbl_carbs.setText(Service.getInstance().getDf().format(item.getCarbs()));
        lbl_sugar.setText(Service.getInstance().getDf().format(item.getSugar()));
        lbl_protein.setText(Service.getInstance().getDf().format(item.getProtein()));

        if (item.isSelected()) {
            i_selected.setContent(MaterialDesignIcon.CHECK_CIRCLE);
        } else {
            i_selected.setContent(MaterialDesignIcon.DONE);
        }
    }
}
