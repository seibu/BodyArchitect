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
    //@todo extend to tag list
    private String tag;
    private final ObservableList<BAFoodPortion> food = FXCollections.observableArrayList();

    double kcal;
    double protein;
    double fat;
    double carbs;
    double sugar;
    private double portion = 1;

    public BAMeal(String name, String tag) {
        this.name = name;
        this.tag = tag;
    }

    public BAMeal(int id, String name, String tag) {
        this(name, tag);
        this.id = id;
    }

    public ObservableList<BAFoodPortion> getFood() {
        return food;
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

    public String getTag() {
        return tag;
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
        return portioning(kcal);
    }

    @Override
    public double getProtein() {
        return portioning(protein);
    }

    @Override
    public double getFat() {
        return portioning(fat);
    }

    @Override
    public double getCarbs() {
        return portioning(carbs);
    }

    @Override
    public double getSugar() {
        return portioning(sugar);
    }

    @Override
    public String getSubText() {
        return tag + "|" + portion;
    }

    @Override
    public boolean isMeal() {
        return true;
    }

    @Override
    public double getPortion() {
        return portion;
    }

    @Override
    public void setPortion(double portion) {
        this.portion = portion;
    }

    @Override
    public void resetPortion() {
        this.portion = 1;
    }

    /**
     * Used for portioning a specific value
     * @param value
     * @return
     */
    private double portioning(double value) {
        return value * portion;
    }

    public BAMeal getMeal() {
        return this;
    }
}
