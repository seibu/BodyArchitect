/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect.model.nutrition;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.text.MessageFormat;

@Entity
@Table(name = "MEAL_FOODS")
public class MealFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEAL_FOODS_ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "MEAL_ID")
    private Meal meal;

    @ManyToOne
    @JoinColumn(name = "FOOD_ID")
    private Food food;

    double weight;

    public MealFood() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getKcal() {
        Double kcal = food.kcal / food.weight * weight;

        return new DecimalFormat("#.##").format(kcal);
    }

    public String getProtein() {
        Double protein = food.protein / food.weight * weight;

        return new DecimalFormat("#.##").format(protein);
    }

    public String getCarbs() {
        Double carbs = food.carbs / food.weight * weight;

        return new DecimalFormat("#.##").format(carbs);
    }

    public String getSugar() {
        Double sugar = food.sugar / food.weight * weight;

        return new DecimalFormat("#.##").format(sugar);
    }

    public String getFat() {
        Double fat = food.fat / food.weight * weight;

        return new DecimalFormat("#.##").format(fat);
    }

    @Override
    public String toString() {
        String s = MessageFormat.format("{0}: {1}g - {2}kcal [P {3} | C {4}({5}) | F {6}]", food.getName(), weight, getKcal(), getProtein(), getCarbs(), getSugar(), getFat());
        return s;
    }
}
