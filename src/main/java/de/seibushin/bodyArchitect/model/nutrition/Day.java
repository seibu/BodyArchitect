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

public class Day {
    private int id;
    private LocalDate date;
    private ObservableList<Meal> meals = FXCollections.observableArrayList();
    // add Settings

    public Day() {
    }

    public Day(int id, LocalDate date) {
        this.id = id;
        this.date = date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public ObservableList<Meal> getMeals() {
        return meals;
    }

    public Double getKcal() {
        final Double[] d = {0.0};
        meals.forEach(m -> {

            d[0] += m.getKcal();
        });

        return d[0];
    }

    public Double getProtein() {
        final Double[] d = {0.0};
        meals.forEach(m -> {
            d[0] += m.getProtein();
        });

        return d[0];
    }

    public Double getFat() {
        final Double[] d = {0.0};
        meals.forEach(m -> {
            d[0] += m.getFat();
        });

        return d[0];
    }

    public Double getCarbs() {
        final Double[] d = {0.0};
        meals.forEach(m -> {
            d[0] += m.getCarbs();
        });

        return d[0];
    }

    public Double getSugar() {
        final Double[] d = {0.0};
        meals.forEach(m -> {
            d[0] += m.getSugar();
        });

        return d[0];
    }
}
