/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class MealFood {
    private ObjectProperty<Food> food = new SimpleObjectProperty<>();
    private DoubleProperty weight = new SimpleDoubleProperty();

    public MealFood() {

    }

    public MealFood(Food food, Double weight) {
        this.food.set(food);
        this.weight.set(weight);
    }

    public Food getFood() {
        return food.get();
    }

    public void setFood(Food food) {
        this.food.set(food);
    }

    public Double getWeight() {
        return weight.get();
    }

    public void setWeight(Double weight) {
        this.weight.set(weight);
    }

    public Double getKcal() {
        Double d = (food.get().getKcal() / food.get().getWeight()) * weight.get();
        return d;
    }

    public Double getFat() {
        Double d = (food.get().getFat() / food.get().getWeight()) * weight.get();
        return d;
    }

    public Double getProtein() {
        Double d = (food.get().getProtein() / food.get().getWeight()) * weight.get();
        return d;
    }

    public Double getCarbs() {
        Double d = (food.get().getCarbs() / food.get().getWeight()) * weight.get();
        return d;
    }

    public Double getSugar() {
        Double d = (food.get().getSugar() / food.get().getWeight()) * weight.get();
        return d;
    }

    public String getName() {
        return food.get().getName();
    }
}
