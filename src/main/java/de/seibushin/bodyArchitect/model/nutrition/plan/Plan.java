/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition.plan;

import de.seibushin.bodyArchitect.model.nutrition.SimpleMeal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Plan {
    private Integer id = null;
    private String name;
    private double kcal = 0.0;
    private double protein = 0.0;
    private double fat = 0.0;
    private double carbs = 0.0;
    private double sugar = 0.0;
    private boolean selected = false;

    private ObservableList<PlanDay> planDays = FXCollections.observableArrayList();

    public Plan() {

    }

    public Plan(int id, String name, double kcal, double protein, double fat, double carbs, double sugar, boolean selected) {
        this.name = name;
        this.id = id;
        this.kcal = kcal;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.sugar = sugar;
        this.selected = selected;
    }

    public void addDay(PlanDay planDay) {
        planDays.add(planDay);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ObservableList<PlanDay> getPlanDays() {
        return planDays;
    }

    public void setPlanDays(ObservableList<PlanDay> planDays) {
        this.planDays = planDays;
    }
}
