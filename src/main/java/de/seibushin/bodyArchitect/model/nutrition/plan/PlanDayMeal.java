/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition.plan;

import de.seibushin.bodyArchitect.model.nutrition.Meal;
import de.seibushin.bodyArchitect.model.nutrition.SimpleMeal;
import de.seibushin.bodyArchitect.model.nutrition.Type;

public class PlanDayMeal implements SimpleMeal {

    private int id;
    private SimpleMeal meal;
    private boolean saved = false;

    public PlanDayMeal() {

    }

    public PlanDayMeal(SimpleMeal meal) {
        this.meal = meal;
        this.id = meal.getId();
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
        return meal.getKcal();
    }

    @Override
    public double getFat() {
        return meal.getFat();
    }

    @Override
    public double getProtein() {
        return meal.getProtein();
    }

    @Override
    public double getCarbs() {
        return meal.getCarbs();
    }

    @Override
    public double getSugar() {
        return meal.getSugar();
    }

    public int getMealId() {
        return meal.getId();
    }

    @Override
    public Type getType() {
        return meal.getType();
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
    public Meal getMeal() {
        return (Meal)meal;
    }
}
