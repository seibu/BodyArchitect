/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Day implements NutriStats {
    private Integer id = null;
    private LocalDate date;
    private String notice;

    private ObservableList<BANutritionUnit> meals = FXCollections.observableArrayList();
    // add Settings

    private double kcal = 0.0;
    private double protein = 0.0;
    private double fat = 0.0;
    private double carbs = 0.0;
    private double sugar = 0.0;

    private boolean saved = false;

    public Day() {
    }

    public Day(int id, LocalDate date, String notice) {
        this.id = id;
        this.date = date;
        this.notice = notice;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void addMeal(BANutritionUnit meal) {
        meals.add(meal);
        // add Stats to the meal Stats
        kcal += meal.getKcal();
        protein += meal.getProtein();
        fat += meal.getFat();
        carbs += meal.getCarbs();
        sugar += meal.getSugar();
    }

    public void removeMeal(BANutritionUnit meal) {
        if (meals.remove(meal)) {
            // remove Stats from the meal Stats
            kcal -= meal.getKcal();
            protein -= meal.getProtein();
            fat -= meal.getFat();
            carbs -= meal.getCarbs();
            sugar -= meal.getSugar();
        }
    }

    public ObservableList<BANutritionUnit> getMeals() {
        return meals;
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

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotice() {
        return notice;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
