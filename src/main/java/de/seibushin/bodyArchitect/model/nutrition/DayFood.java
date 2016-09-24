/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

public class DayFood implements SimpleMeal {

    private int id;
    private Food food;
    private double weight;
    private boolean saved = false;

    public DayFood() {

    }

    public DayFood(Food food, double weight) {
        this.food = food;
        this.weight = weight;
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
        double d = (food.getKcal() / food.getWeight()) * weight;
        return d;
    }

    @Override
    public double getFat() {
        double d = (food.getFat() / food.getWeight()) * weight;
        return d;
    }

    @Override
    public double getProtein() {
        double d = (food.getProtein() / food.getWeight()) * weight;
        return d;
    }

    @Override
    public double getCarbs() {
        double d = (food.getCarbs() / food.getWeight()) * weight;
        return d;
    }

    @Override
    public double getSugar() {
        double d = (food.getSugar() / food.getWeight()) * weight;
        return d;
    }

    public double getWeight() {
        return weight;
    }

    public int getFoodId() {
        return food.getId();
    }

    @Override
    public Type getType() {
        return Type.SNACK;
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
}
