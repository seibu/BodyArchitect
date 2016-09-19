/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.refactoring;

import de.seibushin.bodyArchitect.model.nutrition.Type;

import java.util.ArrayList;
import java.util.List;

public class Meal {
    private String name;
    private Type type;
    private List<Food> foods;

    public Meal() {
        this.foods = new ArrayList<>();
    }

    public Meal(String name, Type type, List<Food> foods) {
        this.name = name;
        this.type = type;
        this.foods = foods;
    }

    public void addFood(Food food) {
        foods.add(food);
    }

    public void removeFood(Food food) {
        foods.remove(food);
    }


}
