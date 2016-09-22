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

    private Double kcal = 0.0;
    private Double protein = 0.0;
    private Double fat = 0.0;
    private Double carbs = 0.0;
    private Double sugar = 0.0;

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

        // add Stats to the meal Stats
        kcal += mealFood.getKcal();
        protein += mealFood.getProtein();
        fat += mealFood.getFat();
        carbs += mealFood.getCarbs();
        sugar += mealFood.getSugar();
    }

    public void removeFood(MealFood mealFood) {
        if (mealFoods.remove(mealFood)) {
            // remove Stats from the meal Stats
            kcal -= mealFood.getKcal();
            protein -= mealFood.getProtein();
            fat -= mealFood.getFat();
            carbs -= mealFood.getCarbs();
            sugar -= mealFood.getSugar();
        }
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getKcal() {
        return kcal;
    }

    public Double getProtein() {
        return protein;
    }

    public Double getFat() {
        return fat;
    }

    public Double getCarbs() {
        return carbs;
    }

    public Double getSugar() {
        return sugar;
    }


}
