/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

public class BAFoodPortion implements BANutritionUnit, Synchronizable {
    private int id;
    private BAFood food;
    private double portionSize;
    private boolean saved = false;

    public BAFoodPortion(BAFood food, double portionSize) {
        this.food = food;
        this.portionSize = portionSize;
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
        return portioning(food.getKcal());
    }

    @Override
    public double getProtein() {
        return portioning(food.getProtein());
    }

    @Override
    public double getFat() {
        return portioning(food.getFat());
    }

    @Override
    public double getCarbs() {
        return portioning(food.getCarbs());
    }

    @Override
    public double getSugar() {
        return portioning(food.getSugar());
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
    public double getPortionSize() {
        return portionSize;
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
        return value / food.getWeight() * portionSize;
    }
}
