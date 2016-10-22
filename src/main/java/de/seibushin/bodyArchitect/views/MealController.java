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
import com.gluonhq.charm.glisten.control.ExceptionDialog;
import com.gluonhq.charm.glisten.control.Icon;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.helper.Utils;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import de.seibushin.bodyArchitect.model.nutrition.MealFood;
import de.seibushin.bodyArchitect.model.nutrition.Type;
import de.seibushin.bodyArchitect.model.nutrition.plan.Plan;
import de.seibushin.bodyArchitect.model.nutrition.plan.PlanDay;
import de.seibushin.bodyArchitect.views.listCell.FoodCell;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;

public class MealController {
    @FXML
    TextField tf_name;
    @FXML
    ComboBox<Type> cb_type;
    @FXML
    CharmListView<Food, String> lv_food;
    @FXML
    CharmListView<Food, String> lv_allFood;
    @FXML
    TextField tf_weight;
    @FXML
    Slider slider_portion;

    @FXML
    View meal;

    private final BooleanProperty sliding = new SimpleBooleanProperty();

    private Food selected = null;

    private void addMeal() {
        String result = "";
        try {
            String name = tf_name.getText();
            Type type = cb_type.getValue();

            Meal meal = new Meal(name, type);

            for (Food f : lv_food.itemsProperty().get()) {
                meal.addMealFood(new MealFood(f, f.getPortion()));
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
        //todo: empty all fields!!!

        cb_type.getSelectionModel().clearSelection();
        tf_name.clear();

        // both ways should be equal in this case
        //lv_food.setItems(null);

        // we work with the actual object for the food, so we need to restore the acurate portion
        for (Food f : lv_food.itemsProperty().get()) {
            f.resetPortion();
        }

        lv_food.setItems(FXCollections.emptyObservableList());
        lv_allFood.setItems(Service.getInstance().getFoodsClone());
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

        //lv_food.setPlaceholder(new Label(Utils.getString("meal.add.food.placeholder")));
        //lv_allFood.setPlaceholder(new Label(Utils.getString("meal.add.allFood.placeholder")));

        lv_allFood.setItems(Service.getInstance().getFoodsClone());
        lv_allFood.setComparator(((o1, o2) -> o1.getName().compareTo(o2.getName())));
        cb_type.getItems().setAll(Type.values());

        // ListView with the Foods for the new Meal
        lv_food.setCellFactory(cell -> {
            final FoodCell foodCell = new FoodCell(true,
                    c -> {
                        System.out.println("left");
                        lv_food.itemsProperty().remove(c);
                        lv_allFood.itemsProperty().add(c);
                    },
                    c -> {
                        System.out.println("right");

                    });
            // notify view that cell is sliding
            sliding.bind(foodCell.slidingProperty());

            return foodCell;
        });

        // prevent the list from scrolling while sliding
        lv_food.addEventFilter(ScrollEvent.ANY, e -> {
            if (sliding.get() && e.getDeltaY() != 0) {
                e.consume();
            }
        });

        // ListView with all the Foods available
        lv_allFood.setCellFactory(cell -> {
            final FoodCell foodCell = new FoodCell(false,
                    c -> {
                        System.out.println("left");
                        lv_allFood.itemsProperty().remove(c);
                        lv_food.itemsProperty().add(c);
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

        Platform.runLater(() -> {
            ((ListView<Food>) lv_food.lookup(".list-view")).getSelectionModel().selectedItemProperty().addListener(
                    (obs, ov, nv) -> {
                        if (nv != null) {
                            slider_portion.setValue(nv.getPortion());
                            selected = nv;
                        } else {
                            slider_portion.setValue(0);
                        }
                    }
            );
        });

        tf_weight.textProperty().addListener((obs, ov, nv) -> {
            if (nv != null && nv != "") {
                try {
                    double d = Double.parseDouble(nv);
                    selected.setPortion(d);
                    ((ListView<Food>) lv_food.lookup(".list-view")).refresh();
                } catch (Exception e){
                    //e.printStackTrace();
                }
            }
        });
    }
}
