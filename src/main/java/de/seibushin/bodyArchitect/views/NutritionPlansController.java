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
import de.seibushin.bodyArchitect.Main;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.model.nutrition.SimpleMeal;
import de.seibushin.bodyArchitect.model.nutrition.plan.Plan;
import de.seibushin.bodyArchitect.model.nutrition.plan.PlanDay;
import de.seibushin.bodyArchitect.views.listCell.MealCell;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.ScrollEvent;

public class NutritionPlansController {
    @FXML
    private View plans;
    @FXML
    private CharmListView<Plan, String> lv_plans;
    @FXML
    private CharmListView<PlanDay, String> lv_days;
    @FXML
    private CharmListView<SimpleMeal, String> lv_meals;

    private final BooleanProperty sliding = new SimpleBooleanProperty();

    @FXML
    public void initialize() {
        // update appBar
        plans.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Main.MENU_LAYER)));
            }
        });

        Service.getInstance().getPlans().forEach(p -> {
            try {
                System.out.println("Plan: " + p.getName() + " " + p.getId());
                p.getPlanDays().forEach(d -> {
                    try {
                        System.out.println("Day: " + d.getId() + " order " + d.getOrder());
                        d.getMeals().forEach(m -> {
                            try {
                                System.out.println("Meal: " + m.getId() + " " + m.getName());
                            } catch (Exception e) {
                                System.out.println("Meal: null");
                            }
                        });
                    } catch (Exception e) {
                        System.out.println("Day: null");
                    }


                });
            } catch (Exception e) {
                System.out.println("Plan: null");
            }


        });

        lv_plans.setItems(Service.getInstance().getPlans());

        lv_meals.setCellFactory(cell -> {
            final MealCell mealCell = new MealCell(
                    c -> {
                        System.out.println("left");
                    },
                    c -> {
                        System.out.println("right");
                    });
            // notify view that cell is sliding
            sliding.bind(mealCell.slidingProperty());

            return mealCell;
        });

        // prevent the list from scrolling while sliding
        lv_meals.addEventFilter(ScrollEvent.ANY, e -> {
            if (sliding.get() && e.getDeltaY() != 0) {
                e.consume();
            }
        });

        // order by name
        lv_meals.setComparator(((o1, o2) -> o1.getName().compareTo(o2.getName())));

        Platform.runLater(() -> {
            ((ListView<Plan>) lv_plans.lookup(".list-view")).getSelectionModel().selectedItemProperty().addListener(
                    (obs, ov, nv) -> {
                        if (nv != null) {
                            lv_days.setItems(nv.getPlanDays());
                        }
                    }
            );

            ((ListView<PlanDay>) lv_days.lookup(".list-view")).getSelectionModel().selectedItemProperty().addListener(
                    (obs, ov, nv) -> {
                        if (nv != null) {
                            lv_meals.setItems(nv.getMeals());
                        }
                    }
            );
        });
    }
}
