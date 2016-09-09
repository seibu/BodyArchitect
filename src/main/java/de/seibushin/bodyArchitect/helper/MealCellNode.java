/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.helper;

import com.gluonhq.charm.glisten.control.Icon;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import de.seibushin.bodyArchitect.model.nutrition.MealFood;
import de.seibushin.bodyArchitect.model.nutrition.Type;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Set;

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
    Icon i_more_less;
    @FXML
    GridPane gp_foods;
    @FXML
    Button btn_delete;

    public MealCellNode() {
        FXMLLoader fxmlLoader = new FXMLLoader(MealCellNode.class.getResource("MealCellNode.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(MsgUtil.getBundle());
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MealCellNode(int type) {
        this();

        switch (type) {
            case 0:
                btn_delete.setVisible(false);
                break;
        }
    }

    @FXML
    public void initialize() {
        // hide the foods
        vbox.getChildren().remove(gp_foods);
    }

    public void update(Meal item) {
        lbl_name.setText(item.getName() + " - " + item.getType());
        lbl_kcal.setText(item.getKcal());
        lbl_fat.setText(item.getFat());
        lbl_carbs.setText(item.getCarbs());
        lbl_sugar.setText(item.getSugar());
        lbl_protein.setText(item.getProtein());

        final int[] i = {1};
        item.getMealFoods().forEach(mf -> {
            gp_foods.add(new Label(mf.getFood().getName() + " - " + mf.getWeight()), 0, i[0], 6, 1);
            i[0]++;
            gp_foods.addRow(i[0], new Label(mf.getKcal()), new Label(mf.getProtein()), new Label(mf.getFat()), new Label(mf.getCarbs()), new Label(mf.getSugar()));
            i[0]++;
        });
    }

    @FXML
    private void deleteMeal() {
        // @todo differanciate between delte meal from day and delete meal for real
        System.out.println("delete");
    }

    @FXML
    private void moreLess() {
        if (i_more_less.getContent() == MaterialDesignIcon.EXPAND_MORE) {
            i_more_less.setContent(MaterialDesignIcon.EXPAND_LESS);
            vbox.getChildren().add(gp_foods);
        } else {
            i_more_less.setContent(MaterialDesignIcon.EXPAND_MORE);
            vbox.getChildren().remove(gp_foods);
        }
    }

    public VBox getNode() {
        return vbox;
    }
}
