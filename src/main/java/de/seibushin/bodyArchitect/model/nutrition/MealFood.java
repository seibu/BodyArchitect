/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

public class MealFood {
    private int id;
    private int f_id;
    private int m_id;
    private double weight;
    private Food food;

    public MealFood() {

    }

    public MealFood(int id, int f_id, int m_id, double weight, Food food) {
        this.id = id;
        this.f_id = f_id;
        this.m_id = m_id;
        this.weight = weight;
        this.food = food;
    }

    public MealFood(Food food, double weight) {
        this.weight = weight;
        this.food = food;
        this.f_id = f_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getF_id() {
        return f_id;
    }

    public void setF_id(int f_id) {
        this.f_id = f_id;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }


    public Double getKcal() {
        Double d = (food.getKcal() / food.getWeight()) * weight;
        return d;
    }

    public Double getFat() {
        Double d = (food.getFat() / food.getWeight()) * weight;
        return d;
    }

    public Double getProtein() {
        Double d = (food.getProtein() / food.getWeight()) * weight;
        return d;
    }

    public Double getCarbs() {
        Double d = (food.getCarbs() / food.getWeight()) * weight;
        return d;
    }

    public Double getSugar() {
        Double d = (food.getSugar() / food.getWeight()) * weight;
        return d;
    }

    public String getName() {
        return food.getName();
    }
}
