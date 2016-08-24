/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect.model.nutrition;

import javax.persistence.*;

@Entity
@Table(name = "MEAL_FOODS")
public class MealFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEAL_FOODS_ID")
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MEAL_ID")
    private Meal meal;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FOOD_ID")
    private Food food;

    private int weight;

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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
