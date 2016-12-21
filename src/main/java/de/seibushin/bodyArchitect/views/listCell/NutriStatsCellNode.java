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
import de.seibushin.bodyArchitect.model.nutrition.BANutritionUnit;
import de.seibushin.bodyArchitect.model.nutrition.NutriStats;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class NutriStatsCellNode<T extends NutriStats> {
    @FXML
    public ListTile tile;
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
    public Label lbl_date;

    private T item;

    public NutriStatsCellNode() {
        FXMLLoader fxmlLoader = new FXMLLoader(NutriStatsCellNode.class.getResource("nutriStatsCellNode.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(Utils.getBundle());
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public T getItem() {
        return item;
    }

    public void refresh() {
        update(item);
    }

    public void update(T item) {
        this.item = item;

        lbl_date.setText(item.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
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
