/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.layout.layer.SnackbarPopupView;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.helper.FoodCell;
import de.seibushin.bodyArchitect.helper.Utils;
import de.seibushin.bodyArchitect.model.nutrition.Type;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import de.seibushin.bodyArchitect.model.nutrition.MealFood;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;

public class MealController {
    @FXML
    TextField tf_name;
    @FXML
    ComboBox<Type> cb_type;
    @FXML
    ListView<Food> lv_food;
    @FXML
    ListView<Food> lv_allFood;
    @FXML
    TextField tf_weight;
    @FXML
    Slider slider_portion;

    @FXML
    View meal;

    private SnackbarPopupView snackbarPopupView = new SnackbarPopupView();

    private final BooleanProperty sliding = new SimpleBooleanProperty();

    private void addMeal() {
        String result = "";
        try {
            String name = tf_name.getText();
            Type type = cb_type.getValue();

            Meal meal = new Meal(name, type);

            lv_food.getItems().stream().forEach(f -> {
                meal.addMealFood(new MealFood(f, slider_portion.getValue()));
            });

            Service.getInstance().addMeal(meal);

            // clear the fields and reset the listViews
            clear();

            result = Utils.getString("addMeal_success");
        } catch (Exception e) {
            e.printStackTrace();
            result = Utils.getString("addMeal_error");
        }

        snackbarPopupView.show(result);
    }

    private void clear() {
        //todo: empty all fields!!!

        cb_type.getSelectionModel().clearSelection();
        tf_name.clear();

        lv_food.getItems().clear();
        lv_allFood.setItems(Service.getInstance().getFoodsClone());
    }

    @FXML
    private void initialize() {
        meal.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();

                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> {
                    MobileApplication.getInstance().switchToPreviousView();
                    // clear maybe not used inserted values
                    clear();
                }));
                appBar.getActionItems().add(MaterialDesignIcon.ADD.button(e -> addMeal()));

                // set Title
                appBar.setTitleText("Add Meal");
            }
        });

        MobileApplication.getInstance().addLayerFactory("snackbar2", () -> snackbarPopupView);

        // ListView with the Foods for the new Meal
        lv_food.setCellFactory(cell -> {
            final FoodCell foodCell = new FoodCell(
                    c -> {
                        System.out.println("left");
                        lv_food.getItems().remove(c);
                        lv_allFood.getItems().add(c);
                    },
                    c -> {
                        System.out.println("right");

                    });
            // notify view that cell is sliding
            sliding.bind(foodCell.slidingProperty());

            return foodCell;
        });
        lv_food.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                slider_portion.setValue(newValue.getPortion());
            } else {
                slider_portion.setValue(0);
            }
        });

        // prevent the list from scrolling while sliding
        lv_food.addEventFilter(ScrollEvent.ANY, e -> {
            if (sliding.get() && e.getDeltaY() != 0) {
                e.consume();
            }
        });

        // ListView with all the Foods available
        lv_allFood.setCellFactory(cell -> {
            final FoodCell foodCell = new FoodCell(
                    c -> {
                        System.out.println("left");
                        lv_allFood.getItems().remove(c);
                        lv_food.getItems().add(c);
                    },
                    c -> {
                        System.out.println("right");
                    });
            // notify view that cell is sliding
            sliding.bind(foodCell.slidingProperty());

            return foodCell;
        });

        // prevent the list from scrolling while sliding
        lv_allFood.addEventFilter(ScrollEvent.ANY, e -> {
            if (sliding.get() && e.getDeltaY() != 0) {
                e.consume();
            }
        });

        lv_allFood.setItems(Service.getInstance().getFoodsClone());

        slider_portion.valueProperty().addListener(e -> {
            if (lv_food.getSelectionModel().getSelectedItem() != null) {
                lv_food.getSelectionModel().getSelectedItem().setPortion(Math.ceil(slider_portion.getValue()));
                lv_food.refresh();
            }
        });

        cb_type.getItems().setAll(Type.values());
    }
}
