/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition.plan;

import de.seibushin.bodyArchitect.model.nutrition.SimpleMeal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PlanDay {
    private Integer id = null;
    private int np_id;
    private int order;

    private ObservableList<SimpleMeal> meals = FXCollections.observableArrayList();

    public PlanDay() {

    }

    public PlanDay(int id, int np_id, int order) {
        this.id = id;
        this.np_id = np_id;
        this.order = order;
    }

    public void addMeal(SimpleMeal simpleMeal) {
        meals.add(simpleMeal);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNp_id() {
        return np_id;
    }

    public void setNp_id(int np_id) {
        this.np_id = np_id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public ObservableList<SimpleMeal> getMeals() {
        return meals;
    }

    public void setMeals(ObservableList<SimpleMeal> meals) {
        this.meals = meals;
    }
}
