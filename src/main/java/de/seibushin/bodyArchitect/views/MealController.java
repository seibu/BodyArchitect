/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.CharmListView;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.helper.Utils;
import de.seibushin.bodyArchitect.model.nutrition.BAFood;
import de.seibushin.bodyArchitect.model.nutrition.BAFoodPortion;
import de.seibushin.bodyArchitect.model.nutrition.BAMeal;
import de.seibushin.bodyArchitect.model.nutrition.Type;
import de.seibushin.bodyArchitect.views.listCell.NutritionUnitCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class MealController {
    @FXML
    View meal;
    @FXML
    TextField tf_name;
    @FXML
    ComboBox<Type> cb_type;
    @FXML
    CharmListView<BAFoodPortion, String> lv_food;
    @FXML
    CharmListView<BAFood, String> lv_allFood;
    @FXML
    TextField tf_portion;
    @FXML
    TextField tf_search;

    FilteredList<BAFood> filtered = new FilteredList(Service.getInstance().getFoods());
    ObservableList<BAFoodPortion> selectedList = FXCollections.observableArrayList();
    FilteredList<BAFoodPortion> selected = new FilteredList(selectedList);

    private void addMeal() {
        String result = "";
        try {
            String name = tf_name.getText();
            String type = cb_type.getValue().toString();

            BAMeal meal = new BAMeal(name, type);

            for (BAFoodPortion f : selected) {
                meal.addFood(f);
            }

            Service.getInstance().addMeal(meal);

            // clear the fields and reset the listViews
            clear();

            result = Utils.getString("addMeal_success");
        } catch (Exception e) {
            e.printStackTrace();
            result = Utils.getString("addMeal_error");
        }

        MobileApplication.getInstance().showMessage(result);
    }

    private void clear() {
        cb_type.getSelectionModel().clearSelection();
        tf_name.clear();
        selectedList.clear();
    }

    @FXML
    private void addToMeal() {
        BAFood food = lv_allFood.getSelectedItem();
        BAFoodPortion portion = new BAFoodPortion(food, food.getPortion());
        selectedList.add(portion);

        food.resetPortion();
    }

    @FXML
    private void search() {
        filtered.setPredicate(n -> n.getName().toLowerCase().contains(tf_search.getText().toLowerCase()));
    }

    @FXML
    private void initialize() {
        meal.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();

                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> {
                    MobileApplication.getInstance().switchToPreviousView();
                    // clear the maybe not used inserted values of the textfields
                    clear();
                }));
                appBar.getActionItems().add(MaterialDesignIcon.ADD.button(e -> addMeal()));

                // set Title
                appBar.setTitleText("Add Meal");
            }
        });

        lv_allFood.setItems(filtered);
        lv_allFood.setComparator(((o1, o2) -> o1.getName().compareTo(o2.getName())));
        lv_allFood.setCellFactory(cell -> new NutritionUnitCell<>());

        cb_type.getItems().setAll(Type.values());

        // ListView with the Foods for the new Meal
        lv_food.setItems(selected);
        lv_food.setCellFactory(cell -> new NutritionUnitCell<>());


        tf_portion.textProperty().addListener((obs, ov, nv) -> {
            if (nv != null && nv != "") {
                try {
                    double d = Double.parseDouble(nv);
                    lv_allFood.getSelectedItem().setPortion(d);
                    lv_allFood.refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
