/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.refactoring;

import com.gluonhq.connect.ConnectState;
import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.gluoncloud.GluonClient;
import com.gluonhq.connect.gluoncloud.GluonClientBuilder;
import com.gluonhq.connect.gluoncloud.OperationMode;
import com.gluonhq.connect.provider.DataProvider;
import de.seibushin.bodyArchitect.model.nutrition.Settings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

public class Service {
    private GluonClient gluonClient;

    private final String FOODS = "foods v0";
    private final ListProperty<Food> foods = new SimpleListProperty<>(FXCollections.<Food>observableArrayList());


    public Service() {
        gluonClient = GluonClientBuilder.create().operationMode(OperationMode.LOCAL_ONLY).build();
    }

    public void loadRelevant() {
        retrieveFoods();
        retrieveMeals();
    }

    private void retrieveFoods() {
        GluonObservableList<Food> tmp = DataProvider.<Food>retrieveList(gluonClient.createListDataReader(FOODS, Food.class));
        tmp.stateProperty().addListener((obs, ov, nv) -> {
            if (ConnectState.SUCCEEDED.equals(nv)) {
                foods.set(tmp);
            }
        });
    }

    public ListProperty<Food> foodsProperty() {
        return foods;
    }

    private void retrieveMeals() {
        
    }

    public void addFood(Food food) {
        //foods.add(food);
        // @todo check difference
        foods.get().add(food);
    }
}
