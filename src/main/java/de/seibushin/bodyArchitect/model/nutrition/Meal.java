/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Meal {
    private StringProperty name = new SimpleStringProperty();
    private ObjectProperty<Type> type = new SimpleObjectProperty<>();
    private ListProperty<MealFood> mealFoods = new SimpleListProperty<>(FXCollections.observableArrayList());

    public Meal() {
    }

    public Meal(String name, Type type) {
        this.name.set(name);
        this.type.set(type);
    }

    public Meal(String name, Type type, ObservableList<MealFood> mealFoods) {
        this.name.set(name);
        this.type.set(type);
        this.mealFoods.set(mealFoods);
    }

    public void addMealFood(MealFood mealFood) {
        mealFoods.get().add(mealFood);
    }

    public void removeFood(MealFood mealFood) {
        mealFoods.get().remove(mealFood);
    }

    public String getName() {
        return name.get();
    }

    public Type getType() {
        return type.get();
    }

    public ObservableList<MealFood> getMealFoods() {
        return mealFoods.get();
    }

    public Double getKcal() {
        final Double[] d = {0.0};
        mealFoods.get().forEach(mf -> {
            d[0] += mf.getKcal();
        });

        return d[0];
    }

    public Double getProtein() {
        final Double[] d = {0.0};
        mealFoods.get().forEach(mf -> {
            d[0] += mf.getProtein();
        });

        return d[0];
    }

    public Double getFat() {
        final Double[] d = {0.0};
        mealFoods.get().forEach(mf -> {
            d[0] += mf.getFat();
        });

        return d[0];
    }

    public Double getCarbs() {
        final Double[] d = {0.0};
        mealFoods.get().forEach(mf -> {
            d[0] += mf.getCarbs();
        });

        return d[0];
    }

    public Double getSugar() {
        final Double[] d = {0.0};
        mealFoods.get().forEach(mf -> {
            d[0] += mf.getSugar();
        });

        return d[0];
    }
}
