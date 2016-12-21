/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

import java.time.LocalDate;

public class Stats implements NutriStats {
    private double kcal = 0.0;
    private double protein = 0.0;
    private double fat = 0.0;
    private double carbs = 0.0;
    private double sugar = 0.0;
    private int days = 0;

    public Stats() {

    }

    public Stats(double kcal, double protein, double fat, double carbs, double sugar, int days) {
        this.kcal = kcal;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.sugar = sugar;
        this.days = days;
    }

    public double getKcalTotal() {
        return kcal;
    }

    public double getKcal() {
        return kcal / days;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public void addKcal(double kcal) {
        this.kcal += kcal;
    }

    public double getProteinTotal() {
        return protein;
    }

    public double getProtein() {
        return protein / days;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public void addProtein(double protein) {
        this.protein += protein;
    }

    public double getFatTotal() {
        return fat;
    }

    public double getFat() {
        return fat / days;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public void addFat(double fat) {
        this.fat += fat;
    }

    public double getCarbsTotal() {
        return carbs;
    }

    public double getCarbs() {
        return carbs / days;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public void addCarbs(double carbs) {
        this.carbs += carbs;
    }

    public double getSugarTotal() {
        return sugar;
    }

    public double getSugar() {
        return sugar / days;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public void addSugar(double sugar) {
        this.sugar += sugar;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void addDay() {
        this.days += 1;
    }

    @Override
    public LocalDate getDate() {
        return LocalDate.now();
    }
}
