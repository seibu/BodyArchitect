/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BAMeal implements BANutritionUnit {
    private int id;
    private String name;
    private Type type;
    private final ObservableList<BAFoodPortion> food = FXCollections.observableArrayList();

    private double kcal;
    private double protein;
    private double fat;
    private double carbs;
    private double sugar;

    public BAMeal(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public BAMeal(int id, String name, Type type) {
        this(name, type);
        this.id = id;
    }

    public void addFood(BAFoodPortion foodPortion) {
        food.add(foodPortion);

        // add Stats to the meal Stats
        kcal += foodPortion.getKcal();
        protein += foodPortion.getProtein();
        fat += foodPortion.getFat();
        carbs += foodPortion.getCarbs();
        sugar += foodPortion.getSugar();
    }

    public void removeFood(BAFoodPortion foodPortion) {
        if (food.remove(foodPortion)) {
            // remove Stats from the meal Stats
            kcal -= foodPortion.getKcal();
            protein -= foodPortion.getProtein();
            fat -= foodPortion.getFat();
            carbs -= foodPortion.getCarbs();
            sugar -= foodPortion.getSugar();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public double getKcal() {
        return kcal;
    }

    @Override
    public double getProtein() {
        return protein;
    }

    @Override
    public double getFat() {
        return fat;
    }

    @Override
    public double getCarbs() {
        return carbs;
    }

    @Override
    public double getSugar() {
        return sugar;
    }
}
