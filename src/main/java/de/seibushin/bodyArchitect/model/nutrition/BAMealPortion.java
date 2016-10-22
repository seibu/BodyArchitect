/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

public class BAMealPortion implements BANutritionUnit, Synchronizable {
    private int id;
    private BAMeal meal;
    private double portionSize;
    private boolean saved = false;

    public BAMealPortion(BAMeal meal, double portionSize) {
        this.meal = meal;
        this.portionSize = portionSize;
    }

    @Override
    public String getName() {
        return meal.getName();
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
        return portioning(meal.getKcal());
    }

    @Override
    public double getProtein() {
        return portioning(meal.getProtein());
    }

    @Override
    public double getFat() {
        return portioning(meal.getFat());
    }

    @Override
    public double getCarbs() {
        return portioning(meal.getCarbs());
    }

    @Override
    public double getSugar() {
        return portioning(meal.getSugar());
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
        return meal;
    }

    @Override
    public int getNutritionUnitId() {
        return meal.getId();
    }

    /**
     * Used for portioning a specific value
     * @param value
     * @return
     */
    private double portioning(double value) {
        return value * portionSize;
    }
}
