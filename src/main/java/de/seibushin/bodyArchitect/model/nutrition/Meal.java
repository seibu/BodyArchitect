/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Meal implements SimpleMeal {
    private int id;
    private String name;
    private Type type;
    private final ObservableList<MealFood> mealFoods = FXCollections.observableArrayList();

    private double kcal = 0.0;
    private double protein = 0.0;
    private double fat = 0.0;
    private double carbs = 0.0;
    private double sugar = 0.0;
    private boolean saved = false;

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

    @Override
    public boolean isMeal() {
        return true;
    }

    @Override
    public boolean isSaved() {
        return saved;
    }

    @Override
    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    @Override
    public Meal getMeal() {
        return this;
    }

    @Override
    public boolean isForPlan() {
        return false;
    }

    @Override
    public double getWeight() {
        return 0;
    }

    public ObservableList<MealFood> getMealFoods() {
        return mealFoods;
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
