/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.layout.layer.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import de.seibushin.bodyArchitect.*;
import de.seibushin.bodyArchitect.helper.ColumnUtil;
import de.seibushin.bodyArchitect.helper.MealCell;
import de.seibushin.bodyArchitect.helper.MsgUtil;
import de.seibushin.bodyArchitect.model.nutrition.Day;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalDatePicker;
import org.hibernate.HibernateException;

import java.io.IOException;
import java.time.LocalDate;

public class NutritionController {
    public static final String DAY_VIEW = "Day";
    public static final String MEAL_VIEW = "Meal";
    public static final String FOOD_VIEW = "Food";

    @FXML
    View nutrition;

    @FXML
    TableView<Food> tv_food;

    @FXML
    TableView<Meal> tv_meal;

    @FXML
    TableColumn ID;
    @FXML
    TableColumn NAME;
    @FXML
    TableColumn KCAL;
    @FXML
    TableColumn CARBS;
    @FXML
    TableColumn SUGAR;
    @FXML
    TableColumn FAT;
    @FXML
    TableColumn PROTEIN;
    @FXML
    TableColumn WEIGHT;
    @FXML
    TableColumn PORTION;

    @FXML
    VBox vbox_nutrition;
    @FXML
    ListView lv_meals;

    LocalDatePicker cp;

    /**
     * Shows the food_add window for adding new food
     * onclose the content of the tableview is refreshed
     */
    @FXML
    private void showFoodAdd() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(Config.FXML_FOOD_ADD));
            Stage stage = new Stage();
            stage.setTitle(MsgUtil.getString("title"));

            // on close refresh the foods tv
            stage.setOnCloseRequest(event -> {
                // stop the propagation of the event
                BodyArchitect.getInstance().refreshTableView(tv_food, Food.class);

            });

            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the currently selected Food
     *
     * @todo mark the selected Food as to be deleted to give the opportunity to restore the data
     * @todo create garbage collector to finally delete the entry after a given time
     */
    @FXML
    public void deleteFood() {
        Food food = tv_food.getSelectionModel().getSelectedItem();

        try {
            BodyArchitect.getInstance().deleteEntry(Food.class, food.getId());
            tv_food.getItems().remove(food);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showMealAdd() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(Config.FXML_MEAL_ADD));
            Stage stage = new Stage();
            stage.setTitle(MsgUtil.getString("title"));

            // on close refresh the foods tv
            stage.setOnCloseRequest(event -> {
                // stop the propagation of the event
                BodyArchitect.getInstance().refreshTableView(tv_meal, Meal.class);

            });

            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteMeal() {
        Meal meal = tv_meal.getSelectionModel().getSelectedItem();

        try {
            BodyArchitect.getInstance().deleteEntry(Meal.class, meal.getId());
            tv_meal.getItems().remove(meal);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showAddMeal() {
        nutrition.getApplication().switchView(DAY_VIEW);

        /*
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(Config.FXML_ADD_MEAL));
            Stage stage = new Stage();
            stage.setTitle(MsgUtil.getString("title"));

            // on close refresh the foods tv
            stage.setOnCloseRequest(event -> {
                // stop the propagation of the event
                BodyArchitect.getInstance().refreshListView(lv_meals, Day.class);

            });

            stage.setScene(new Scene(loader.load()));
            ((AddMealController)loader.getController()).setDate(cp.getLocalDate());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    @FXML
    private void removeMeal() {

    }

    /**
     * Adds all annotated fields to the tableview and shows the content of the database
     */
    private void populateTableView() {
        // add columns for Food.class to the tableview
        ColumnUtil.addColumns(tv_food, Food.class);
        // refresh the data
        BodyArchitect.getInstance().refreshTableView(tv_food, Food.class);


        // add columns for Meal.class to the tableview
        ColumnUtil.addColumns(tv_meal, Meal.class);
        // refresh the data
        BodyArchitect.getInstance().refreshTableView(tv_meal, Meal.class);

        //bodyArchitect.getInstance().refreshListView(lv_meals, Day.class);
        lv_meals.setCellFactory(c -> new MealCell());
        lv_meals.getItems().setAll(BodyArchitect.getInstance().getMealsForDay(cp.getLocalDate()));
        System.out.println("test2");


        /*
        System.out.println("\n----\n");
        System.out.println(MessageFormat.format("Storing {0} foods in the database", foods.size()));
        for (final Food f : foods) {
            System.out.println(f);
        }
        System.out.println("\n----\n");
        */
    }

    @FXML
    private void showShit() {
        System.out.println(cp.getLocalDate());

        // Highlight
        cp.highlightedLocalDates().add(cp.getLocalDate());
    }

    @FXML
    private void initialize() {
        // add the views
        MobileApplication.getInstance().addViewFactory(DAY_VIEW, () -> new DayView(DAY_VIEW).getView());
        MobileApplication.getInstance().addViewFactory(MEAL_VIEW, () -> new MealView(MEAL_VIEW).getView());
        MobileApplication.getInstance().addViewFactory(FOOD_VIEW, () -> new FoodView(FOOD_VIEW).getView());

        // update appBar
        nutrition.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Main.MENU_LAYER)));
                appBar.setTitleText("Nutrition");
                appBar.getActionItems().add(MaterialDesignIcon.ADD.button(e -> showAddMeal()));
            }
        });

        // add Layers
        nutrition.getLayers().add(new FloatingActionButton(MaterialDesignIcon.INFO.text,
                e -> System.out.println("Info")));

        System.out.println("test1");

        // add the calendar / localdatepicker
        cp = new LocalDatePicker( LocalDate.now());
        // add listener so react on click of the calendar/days

        cp.localDateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                BodyArchitect.getInstance().setSelectedDate(newValue);
                lv_meals.getItems().setAll(BodyArchitect.getInstance().getMealsForDay(newValue));
            }
        });

        // Highlight Days with Meals
        // @todo throws npe
        //cp.highlightedLocalDates().addAll(BodyArchitect.getInstance().getColumn(Day.class, "date"));



        // add the DatePicker to the Pane
        vbox_nutrition.getChildren().add(0, cp);

        // populate Foods
        populateTableView();

        //NutritionTest.printMeal(bodyArchitect.getInstance().getSession());

    }

}