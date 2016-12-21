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
import de.seibushin.bodyArchitect.model.nutrition.BANutritionUnit;
import de.seibushin.bodyArchitect.model.nutrition.plan.Plan;
import de.seibushin.bodyArchitect.model.nutrition.plan.PlanDay;
import de.seibushin.bodyArchitect.views.listCell.NutritionUnitCell;
import de.seibushin.bodyArchitect.views.listCell.PlanCell;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;

public class NutritionPlansController {
    public static final String PLAN_ADD_VIEW = "Add Plan";

    @FXML
    private View plans;
    @FXML
    private CharmListView<Plan, String> lv_plans;
    @FXML
    private CharmListView<BANutritionUnit, String> lv_meals;
    @FXML
    HBox hb_days;
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

    private final BooleanProperty sliding = new SimpleBooleanProperty();

    private Plan selectedPlan = null;
    private static PlanDay selectedPlanDay = null;

    public static PlanDay getSelectedPlanDay() {
        return selectedPlanDay;
    }

    private void addPlan() {
        plans.getApplication().switchView(PLAN_ADD_VIEW);
    }

    @FXML
    private void addDay() {
        PlanDay pd = selectedPlan.addNewDay();
        // add Day to database
        Service.getInstance().addPlanDay(pd);
        Button b = new Button(pd.getOrder() + "");
        b.setOnAction(e -> lv_meals.setItems(pd.getMeals()));
        hb_days.getChildren().add(b);
    }

    @FXML
    private void addMeal() {
        MealDayController.setForPlan(true);
        plans.getApplication().switchView(Main.MEAL_DAY_VIEW);
    }

    private void printAll() {
        Service.getInstance().getPlans().forEach(p -> {
            try {
                System.out.println("Plan: " + p.getName() + " " + p.getId() + " " + p.isSelected());
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
    }

    @FXML
    public void initialize() {
        MobileApplication.getInstance().addViewFactory(PLAN_ADD_VIEW, () -> new NutritionPlanView(PLAN_ADD_VIEW).getView());

        plans.showingProperty().addListener((obs, oldValue, newValue) -> {
            // update appBar
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("Nutrition Plans");
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Main.MENU_LAYER)));

                appBar.getActionItems().add(MaterialDesignIcon.ADD.button(e -> addPlan()));
            }
        });

        lv_plans.setCellFactory(cell -> {
            final PlanCell planCell = new PlanCell(
                    c -> {
                        System.out.println("left");
                    },
                    c -> {
                        System.out.println("right");
                    });
            sliding.bind(planCell.slidingProperty());
            return planCell;
        });

        // prevent the list from scrolling while sliding
        lv_plans.addEventFilter(ScrollEvent.ANY, e -> {
            if (sliding.get() && e.getDeltaY() != 0) {
                e.consume();
            }
        });

        lv_plans.setItems(Service.getInstance().getPlans());

        lv_meals.setCellFactory(cell -> new NutritionUnitCell<>(e -> {
                    Service.getInstance().deletePlanDayMeal(e, selectedPlanDay.getId());
                    selectedPlanDay.removeMeal(e);
                })
        );

        // order by name
        // @todo order by time or smth
        lv_meals.setComparator(((o1, o2) -> o1.getName().compareTo(o2.getName())));

        Platform.runLater(() -> {
            ((ListView<Plan>) lv_plans.lookup(".list-view")).getSelectionModel().selectedItemProperty().addListener(
                    (obs, ov, nv) -> {
                        if (nv != null) {
                            selectedPlan = nv;
                            hb_days.getChildren().clear();
                            boolean first = true;
                            for (PlanDay pd : nv.getPlanDays()) {
                                // set the meals to the first day of the plan if new plan selected
                                if (first) {
                                    lv_meals.setItems(pd.getMeals());
                                    selectedPlanDay = pd;
                                    first = false;
                                }
                                Button b = new Button(pd.getOrder() + "");
                                b.setOnAction(e -> {
                                    lv_meals.setItems(pd.getMeals());
                                    selectedPlanDay = pd;
                                    bindProperties();
                                });
                                hb_days.getChildren().add(b);
                            }

                        }
                    }
            );
        });
    }

    private void bindProperties() {
        // ?????????????????????????????
        lbl_kcal.textProperty().bind(Bindings.createStringBinding(() -> {
            String s = "";
            try {
                s = Service.getInstance().getDoubleNF().format(selectedPlanDay.getKcal());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return s;
        }));

        lbl_protein.textProperty().bind(Bindings.createStringBinding(() -> {
            String s = "";
            try {
                s = Service.getInstance().getDoubleNF().format(selectedPlanDay.getProtein());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return s;
        }));

        lbl_fat.textProperty().bind(Bindings.createStringBinding(() -> {
            String s = "";
            try {
                s = Service.getInstance().getDoubleNF().format(selectedPlanDay.getFat());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return s;
        }));

        lbl_carbs.textProperty().bind(Bindings.createStringBinding(() -> {
            String s = "";
            try {
                s = Service.getInstance().getDoubleNF().format(selectedPlanDay.getCarbs());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return s;
        }));

        lbl_sugar.textProperty().bind(Bindings.createStringBinding(() -> {
            String s = "";
            try {
                s = Service.getInstance().getDoubleNF().format(selectedPlanDay.getSugar());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return s;
        }));
    }
}
