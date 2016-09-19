/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Day {
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private ListProperty<Meal> meals = new SimpleListProperty<>(FXCollections.observableArrayList());
    // add Settings

    public Day() {
    }

    public Day(LocalDate date, ObservableList<Meal> meals) {
        this.date.set(date);
        this.meals.set(meals);
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void addMeal(Meal meal) {
        meals.get().add(meal);
    }

    public ObservableList<Meal> getMeals() {
        return meals.get();
    }

    public Double getKcal() {
        final Double[] d = {0.0};
        meals.get().forEach(m -> {
            d[0] += m.getKcal();
        });

        return d[0];
    }

    public Double getProtein() {
        final Double[] d = {0.0};
        meals.get().forEach(m -> {
            d[0] += m.getProtein();
        });

        return d[0];
    }

    public Double getFat() {
        final Double[] d = {0.0};
        meals.get().forEach(m -> {
            d[0] += m.getFat();
        });

        return d[0];
    }

    public Double getCarbs() {
        final Double[] d = {0.0};
        meals.get().forEach(m -> {
            d[0] += m.getCarbs();
        });

        return d[0];
    }

    public Double getSugar() {
        final Double[] d = {0.0};
        meals.get().forEach(m -> {
            d[0] += m.getSugar();
        });

        return d[0];
    }
}
