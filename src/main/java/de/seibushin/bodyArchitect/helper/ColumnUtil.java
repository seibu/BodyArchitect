/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.helper;

import de.seibushin.bodyArchitect.BodyArchitect;
import de.seibushin.bodyArchitect.model.BAField;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.hibernate.Session;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ColumnUtil {

    /**
     * Adds all with BAField annotated Fields as Column to the Tableview
     *
     * @param tableView - tableview to get the Column
     * @param type - Class with the annotated Fields
     */
    public static void addColumns(TableView tableView, Class type) {
        // filter the fields for the annotaion
        List fields = Arrays.stream(type.getDeclaredFields()).filter(field -> field.isAnnotationPresent(BAField.class)).collect(Collectors.toList());

        // add all Columns to the TableView
        for (Field f : (List<Field>)fields) {
            TableColumn tc = new TableColumn();

            // set Column Text
            tc.setText(f.getName());
            // set CellValueFactory
            tc.setCellValueFactory(new PropertyValueFactory<>(f.getName()));
            // add the column to the tableview
            tableView.getColumns().add(tc);
        }
    }


    private void setOnEdit() {
        TableColumn NAME = null;


        NAME.setCellFactory(TextFieldTableCell.forTableColumn());
        NAME.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Food, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Food, String> t) {
                        Food f = t.getRowValue();
                        f.setName(t.getNewValue());
                        Session session = BodyArchitect.getInstance().getSession();
                        session.beginTransaction();

                        session.merge(f);

                        session.getTransaction().commit();
                    }
                }
        );
    }

}
