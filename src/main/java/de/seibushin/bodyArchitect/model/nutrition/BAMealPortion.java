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
    private double portion;
    private boolean saved = false;

    public BAMealPortion(BAMeal meal, double portion) {
        this.meal = meal;
        this.portion = portion;
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
        return portioning(meal.kcal);
    }

    @Override
    public double getProtein() {
        return portioning(meal.protein);
    }

    @Override
    public double getFat() {
        return portioning(meal.fat);
    }

    @Override
    public double getCarbs() {
        return portioning(meal.carbs);
    }

    @Override
    public double getSugar() {
        return portioning(meal.sugar);
    }

    @Override
    public String getSubText() {
        return "x" + portion + "|" + meal.getTag();
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
    public double getPortion() {
        return portion;
    }

    @Override
    public void setPortion(double portion) {
        this.portion = portion;
    }

    @Override
    public void resetPortion() {
        // do nothing kapapapapap
    }

    @Override
    public BANutritionUnit getNutritionUnit() {
        return meal;
    }

    @Override
    public int getNutritionUnitId() {
        return meal.getId();
    }

    public BAMeal getMeal() {
        return meal;
    }

    /**
     * Used for portioning a specific value
     * @param value
     * @return
     */
    private double portioning(double value) {
        return value * portion;
    }
}
