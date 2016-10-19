/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition.plan;

import de.seibushin.bodyArchitect.model.nutrition.SimpleMeal;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PlanDay {
    private Integer id = null;
    private int np_id;
    private int order;

    private double kcal = 0.0;
    private double protein = 0.0;
    private double fat = 0.0;
    private double carbs = 0.0;
    private double sugar = 0.0;

    private ObservableList<SimpleMeal> meals = FXCollections.observableArrayList();

    public PlanDay() {

    }

    public PlanDay(int np_id, int order) {
        this.np_id = np_id;
        this.order = order;
    }

    public PlanDay(int id, int np_id, int order) {
        this(np_id, order);
        this.id = id;
    }

    public void addMeal(SimpleMeal simpleMeal) {
        meals.add(simpleMeal);
        // add Stats to the meal Stats
        kcal += simpleMeal.getKcal();
        protein += simpleMeal.getProtein();
        fat += simpleMeal.getFat();
        carbs += simpleMeal.getCarbs();
        sugar += simpleMeal.getSugar();
    }

    public void removeMeal(SimpleMeal simpleMeal) {
        meals.remove(simpleMeal);
        // add Stats to the meal Stats
        kcal -= simpleMeal.getKcal();
        protein -= simpleMeal.getProtein();
        fat -= simpleMeal.getFat();
        carbs -= simpleMeal.getCarbs();
        sugar -= simpleMeal.getSugar();
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

    public double getKcal() {
        return kcal;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getSugar() {
        return sugar;
    }


}
