/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.helper;

import com.gluonhq.charm.glisten.application.GlassPane;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Icon;
import com.gluonhq.charm.glisten.layout.Layer;
import de.seibushin.bodyArchitect.BodyArchitect;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MealInfoLayer extends Layer {
    @FXML
    private VBox root;
    @FXML
    ListView lv_foods;
    @FXML
    Label lbl_name;
    @FXML
    Label lbl_type;
    @FXML
    Label lbl_kcal;
    @FXML
    Label lbl_protein;
    @FXML
    Label lbl_fat;
    @FXML
    Label lbl_carbs;
    @FXML
    Label lbl_sugar;
    @FXML
    Icon i_close;
    private Meal meal;
    private Meal active;

    public MealInfoLayer() {
        FXMLLoader fxmlLoader = new FXMLLoader(MealCellNode.class.getResource("mealInfo.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(Utils.getBundle());
        try {
            root = fxmlLoader.load();
            getChildren().add(root);
            MobileApplication.getInstance().getGlassPane().getLayers().add(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void show() {
        MobileApplication.getInstance().getGlassPane().setBackgroundFade(GlassPane.DEFAULT_BACKGROUND_FADE_LEVEL);
        prepareMealInfo();
        super.show();
    }

    private void prepareMealInfo() {
        if (!meal.equals(active)) {
            System.out.println("new Meal");

            // set the header
            lbl_name.setText(meal.getName());
            lbl_type.setText(meal.getType().toString());
            lbl_kcal.setText(BodyArchitect.getInstance().getDf().format(meal.getKcal()) + " " + Utils.getString("meal.info.kcal"));

            lbl_fat.setText(BodyArchitect.getInstance().getDf().format(meal.getFat()));
            lbl_carbs.setText(BodyArchitect.getInstance().getDf().format(meal.getCarbs()));
            lbl_sugar.setText(BodyArchitect.getInstance().getDf().format(meal.getSugar()));
            lbl_protein.setText(BodyArchitect.getInstance().getDf().format(meal.getProtein()));

            // set the foods
            ObservableList ol = FXCollections.observableArrayList();
            ol.addAll(meal.getMealFoods());
            lv_foods.setItems(ol);

            active = meal;
        }
    }

    @Override
    public void hide() {
        MobileApplication.getInstance().getGlassPane().setBackgroundFade(0.0);
        super.hide();
    }

    @Override
    public void layoutChildren() {
        root.setVisible(isShowing());
        if (!isShowing()) {
            return;
        }
        root.autosize();
        resizeRelocate((MobileApplication.getInstance().getGlassPane().getWidth() - root.getWidth()) / 2, (MobileApplication.getInstance().getGlassPane().getHeight() - root.getHeight()) / 2, root.getWidth(), root.getHeight());
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    @FXML
    public void initialize() {
        lv_foods.setCellFactory(param -> new MealFoodCell());
        i_close.setOnMouseClicked(e -> {
            hide();
        });
    }
}
