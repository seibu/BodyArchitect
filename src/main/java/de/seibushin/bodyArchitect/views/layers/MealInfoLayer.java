/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views.layers;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.CharmListView;
import com.gluonhq.charm.glisten.control.Icon;
import com.gluonhq.charm.glisten.layout.Layer;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.helper.Utils;
import de.seibushin.bodyArchitect.model.nutrition.BAFoodPortion;
import de.seibushin.bodyArchitect.model.nutrition.BAMeal;
import de.seibushin.bodyArchitect.model.nutrition.BAMealPortion;
import de.seibushin.bodyArchitect.model.nutrition.BANutritionUnit;
import de.seibushin.bodyArchitect.views.listCell.NutritionUnitCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MealInfoLayer extends Layer {
    @FXML
    private VBox root;
    @FXML
    CharmListView<BAFoodPortion, String> lv_foods;
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
    private BANutritionUnit meal;
    private BANutritionUnit active;

    public MealInfoLayer() {
        FXMLLoader fxmlLoader = new FXMLLoader(MealInfoLayer.class.getResource("mealInfo.fxml"));
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
        //MobileApplication.getInstance().getGlassPane().setBackgroundFade(GlassPane.DEFAULT_BACKGROUND_FADE_LEVEL);
        prepareMealInfo();
        super.show();
    }

    private void prepareMealInfo() {
        if (!meal.equals(active)) {
            // set the header
            lbl_name.setText(meal.getName());
            lbl_type.setText(meal.getSubText());
            lbl_kcal.setText(Service.getInstance().getDf().format(meal.getKcal()) + " " + Utils.getString("meal.info.kcal"));

            lbl_fat.setText(Service.getInstance().getDf().format(meal.getFat()));
            lbl_carbs.setText(Service.getInstance().getDf().format(meal.getCarbs()));
            lbl_sugar.setText(Service.getInstance().getDf().format(meal.getSugar()));
            lbl_protein.setText(Service.getInstance().getDf().format(meal.getProtein()));

            // set the foods
            //@todo solve this not ugly pls :D
            if (BAMeal.class.isInstance(meal)) {
                lv_foods.setItems(((BAMeal)meal).getFood());
            } else if (BAMealPortion.class.isInstance(meal)) {
                lv_foods.setItems(((BAMealPortion)meal).getMeal().getFood());
            } else {
                System.out.println("ERROR NOT A MEAL");
            }

            active = meal;
        }
    }

    @Override
    public void hide() {
        //MobileApplication.getInstance().getGlassPane().setBackgroundFade(0.0);
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

    public void setMeal(BANutritionUnit meal) {
        this.meal = meal;
    }

    @FXML
    public void initialize() {
        lv_foods.setCellFactory(c -> new NutritionUnitCell<>());
        i_close.setOnMouseClicked(e -> {
            hide();
        });
    }
}
