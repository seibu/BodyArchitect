/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Meal {
    private int id;
    private String name;
    private Type type;
    private final ObservableList<MealFood> mealFoods = FXCollections.observableArrayList();

    public Meal() {
    }

    public Meal(int id, String name, Type type) {
        this(name, type);
        this.id = id;
    }

    public Meal(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public void addMealFood(MealFood mealFood) {
        mealFoods.add(mealFood);
    }

    public void removeFood(MealFood mealFood) {
        mealFoods.remove(mealFood);
    }



    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public ObservableList<MealFood> getMealFoods() {
        return mealFoods;
    }

    public Double getKcal() {
        final Double[] d = {0.0};
        mealFoods.forEach(mf -> {
            d[0] += mf.getKcal();
        });

        return d[0];
    }

    public Double getProtein() {
        final Double[] d = {0.0};
        mealFoods.forEach(mf -> {
            d[0] += mf.getProtein();
        });

        return d[0];
    }

    public Double getFat() {
        final Double[] d = {0.0};
        mealFoods.forEach(mf -> {
            d[0] += mf.getFat();
        });

        return d[0];
    }

    public Double getCarbs() {
        final Double[] d = {0.0};
        mealFoods.forEach(mf -> {
            d[0] += mf.getCarbs();
        });

        return d[0];
    }

    public Double getSugar() {
        final Double[] d = {0.0};
        mealFoods.forEach(mf -> {
            d[0] += mf.getSugar();
        });

        return d[0];
    }

    public int getId() {
        return id;
    }
}
