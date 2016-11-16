/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

import de.seibushin.bodyArchitect.helper.Utils;

public class BAFoodPortion implements BANutritionUnit, Synchronizable {
    private int id;
    private BAFood food;
    private double portion;
    private boolean saved = false;

    public BAFoodPortion(BAFood food, double portion) {
        this.food = food;
        this.portion = portion;
    }

    @Override
    public String getName() {
        return food.getName();
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
        return portioning(food.kcal);
    }

    @Override
    public double getProtein() {
        return portioning(food.protein);
    }

    @Override
    public double getFat() {
        return portioning(food.fat);
    }

    @Override
    public double getCarbs() {
        return portioning(food.carbs);
    }

    @Override
    public double getSugar() {
        return portioning(food.sugar);
    }

    @Override
    public String getSubText() {
        return portion + Utils.getString("food.cell.weight.unit");
    }

    @Override
    public boolean isMeal() {
        return false;
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
    public double getPortion() {
        return portion;
    }

    @Override
    public void setPortion(double portion) {
        this.portion = portion;
    }

    @Override
    public void resetPortion() {
        // to nothing kapapapapapap
    }

    @Override
    public BANutritionUnit getNutritionUnit() {
        return food;
    }

    @Override
    public int getNutritionUnitId() {
        return food.getId();
    }

    /**
     * Used for portioning a specific value
     * @param value
     * @return
     */
    private double portioning(double value) {
        return value / food.getWeight() * portion;
    }
}
