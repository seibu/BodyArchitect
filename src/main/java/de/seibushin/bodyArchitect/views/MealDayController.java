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
import de.seibushin.bodyArchitect.model.nutrition.*;
import de.seibushin.bodyArchitect.views.listCell.NutritionUnitCell;
import de.seibushin.bodyArchitect.views.listCell.NutritionUnitCellNode;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class MealDayController {
    private static boolean forPlan = false;

    @FXML
    private View meal_day;
    @FXML
    CharmListView<BANutritionUnit, String> lv_items;
    @FXML
    TextField tf_test;
    @FXML
    StackPane c_item;
    @FXML
    TextField tf_portion;

    private BooleanProperty sliding = new SimpleBooleanProperty();

    FilteredList<BANutritionUnit> filtered = new FilteredList(Service.getInstance().getAllNutritionUnits());

    private NutritionUnitCellNode selected = new NutritionUnitCellNode();

    public static void setForPlan(boolean nv) {
        forPlan = nv;
    }

    @FXML
    public void search() {
        filtered.setPredicate(n -> n.getName().toLowerCase().contains(tf_test.getText().toLowerCase()));
    }

    @FXML
    public void clearSearch() {
        tf_test.clear();
        search();
    }

    @FXML
    public void add() {
        String result = "XXX could not be added, please try again";
        try {
            BANutritionUnit nunit = lv_items.getSelectedItem();

            if (nunit.isMeal()) {
                BAMealPortion portion = new BAMealPortion((BAMeal) nunit, nunit.getPortion());
                if (forPlan) {
                    NutritionPlansController.getSelectedPlanDay().addMeal(portion);
                    Service.getInstance().addSimpleMealToPlanDay(portion, NutritionPlansController.getSelectedPlanDay().getId());
                    result = portion.getName() + " added to selected day of plan";
                } else {
                    Day day = Service.getInstance().getSelectedDayObject();
                    if (day == null) {
                        day = new Day();
                        day.setDate(Service.getInstance().getSelectedDay());
                        Service.getInstance().addDay(day);
                    }
                    //@todo dont use day.getId() it might be for a plan and not for the active Day!!!
                    day.addMeal(portion);
                    Service.getInstance().addSimpleMealToDay(portion, day.getId());
                    result = portion.getName() + " added to selected Date " + Service.getInstance().getSelectedDayString();
                }
            } else {
                BAFoodPortion portion = new BAFoodPortion((BAFood) nunit, nunit.getPortion());
                if (forPlan) {
                    NutritionPlansController.getSelectedPlanDay().addMeal(portion);
                    Service.getInstance().addSimpleMealToPlanDay(portion, NutritionPlansController.getSelectedPlanDay().getId());
                    result = portion.getName() + " added to selected day of plan";
                } else {
                    Day day = Service.getInstance().getSelectedDayObject();
                    if (day == null) {
                        day = new Day();
                        day.setDate(Service.getInstance().getSelectedDay());
                        Service.getInstance().addDay(day);
                    }
                    day.addMeal(portion);
                    Service.getInstance().addSimpleMealToDay(portion, day.getId());
                    result = portion.getName() + " added to selected date " + Service.getInstance().getSelectedDayString();
                }
            }
            // reset the portion
            nunit.resetPortion();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Service.getInstance().getInfoLayer().show(result);
        MobileApplication.getInstance().showLayer("InfoLayer");
    }

    @FXML
    public void initialize() {
        meal_day.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();

                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> {
                    MobileApplication.getInstance().switchToPreviousView();
                }));

                appBar.getActionItems().add(MaterialDesignIcon.ADD.button(e -> add()));
                appBar.setTitleText("Add meal/food to day");
            }
        });

        tf_portion.textProperty().addListener((obs, ov, nv) -> {
            if (nv != null && nv != "") {
                try {
                    double d = Double.parseDouble(nv);
                    selected.getItem().setPortion(d);
                    selected.refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        c_item.getChildren().add(selected.getNode());

        lv_items.setCellFactory(param -> new NutritionUnitCell<>());
        lv_items.setComparator(((o1, o2) -> o1.getName().compareTo(o2.getName())));
        lv_items.selectedItemProperty().addListener(((obs, ov, nv) -> {
            if (nv != null) {
                selected.update(nv);
            }
        }));
        lv_items.setItems(filtered);
    }
}
