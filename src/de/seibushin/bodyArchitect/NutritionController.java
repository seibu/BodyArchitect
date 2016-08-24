/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect;

import de.seibushin.bodyArchitect.model.nutrition.Food;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

public class NutritionController {

    @FXML
    TableView<Food> tv_food;

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

    public void showHome(ActionEvent actionEvent) {
    }

    private void populateTableView() {
        // set all the CellValueFactories
        ID.setCellValueFactory(new PropertyValueFactory<Food, Integer>("id"));
        NAME.setCellValueFactory(new PropertyValueFactory<Food, String>("name"));
        KCAL.setCellValueFactory(new PropertyValueFactory<Food, Double>("kcal"));
        CARBS.setCellValueFactory(new PropertyValueFactory<Food, Double>("carbs"));
        SUGAR.setCellValueFactory(new PropertyValueFactory<Food, Double>("sugar"));
        FAT.setCellValueFactory(new PropertyValueFactory<Food, Double>("fat"));
        PROTEIN.setCellValueFactory(new PropertyValueFactory<Food, Double>("protein"));
        WEIGHT.setCellValueFactory(new PropertyValueFactory<Food, Integer>("weight"));
        PORTION.setCellValueFactory(new PropertyValueFactory<Food, Integer>("portion"));

        NAME.setCellFactory(TextFieldTableCell.forTableColumn());
        NAME.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Food, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Food, String> t) {
                        Food f = t.getRowValue();
                        f.setName(t.getNewValue());
                        Session session = BodyArchitect.getBa().getSession();
                        session.beginTransaction();

                        session.merge(f);

                        session.getTransaction().commit();
                        session.close();
                    }
                }
        );

        ObservableList<Food> data = FXCollections.observableArrayList();

        Session session = BodyArchitect.getBa().getSession();

        session.beginTransaction();


        session.getTransaction().commit();
        session.close();



        List<Food> foods = BodyArchitect.getBa().getEntity(Food.class);

        System.out.println("\n----\n");
        System.out.println(MessageFormat.format("Storing {0} foods in the database", foods.size()));
        for (final Food f : foods) {
            System.out.println(f);
        }
        System.out.println("\n----\n");

        for(Food f : foods) {
            data.add(f);
        }

        try {
            tv_food.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void initialize() {
        System.out.println("test");
        populateTableView();
    }
}
