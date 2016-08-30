/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect;

import de.seibushin.bodyArchitect.helper.ColumnUtil;
import de.seibushin.bodyArchitect.helper.MsgUtil;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import de.seibushin.bodyArchitect.model.nutrition.NutritionTest;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.LocalDatePicker;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NutritionController {

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
    AnchorPane day;

    LocalDatePicker cp;

    /**
     * Shows the food_add window for adding new food
     * onclose the content of the tableview is refreshed
     */
    @FXML
    private void showFoodAdd() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(Config.FXML_FOOD_ADD));
            Stage stage = new Stage();
            stage.setTitle(MsgUtil.getString("title"));

            // on close refresh the foods tv
            stage.setOnCloseRequest(event -> {
                // stop the propagation of the event
                BodyArchitect.getBa().refreshTableView(tv_food, Food.class);

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
            BodyArchitect.getBa().deleteEntry(Food.class, food.getId());
            tv_food.getItems().remove(food);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showMealAdd() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(Config.FXML_MEAL_ADD));
            Stage stage = new Stage();
            stage.setTitle(MsgUtil.getString("title"));

            // on close refresh the foods tv
            stage.setOnCloseRequest(event -> {
                // stop the propagation of the event
                BodyArchitect.getBa().refreshTableView(tv_meal, Meal.class);

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
            BodyArchitect.getBa().deleteEntry(Meal.class, meal.getId());
            tv_meal.getItems().remove(meal);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds all annotated fields to the tableview and shows the content of the database
     */
    private void populateTableView() {
        // add columns for Food.class to the tableview
        ColumnUtil.addColumns(tv_food, Food.class);
        // refresh the data
        BodyArchitect.getBa().refreshTableView(tv_food, Food.class);


        // add columns for Meal.class to the tableview
        ColumnUtil.addColumns(tv_meal, Meal.class);
        // refresh the data
        BodyArchitect.getBa().refreshTableView(tv_meal, Meal.class);

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
        cp = new LocalDatePicker( LocalDate.now());
        cp.localDateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println(newValue);
                // show currentDay Day with Meals
            }
        });

        // Highlight Days with Meals
        // cp.highlightedLocalDates().add(cp.getLocalDate());

        // add the DatePicker to the Pane
        day.getChildren().add(cp);

        // populate Foods
        populateTableView();

        //NutritionTest.printMeal(BodyArchitect.getBa().getSession());
    }

}