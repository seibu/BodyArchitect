/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect.model.nutrition;

import de.seibushin.bodyArchitect.model.BAField;

import javax.persistence.*;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MEALS")
public class Meal {
    @BAField
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEAL_ID")
    private int id;

    @BAField
    private String name;
    @BAField
    private Type type;

    @OneToMany(mappedBy = "meal")
    private Set<MealFood> mealFoods = new HashSet<>();

    @OneToMany(mappedBy = "meal")
    private Set<DayMeal> dayMeals = new HashSet<>();

    public Meal() {

    }

    public Meal(int id) {
        this.id = id;
    }

    public Meal(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Set<MealFood> getMealFoods() {
        return mealFoods;
    }

    public void setMealFoods(Set<MealFood> mealFoods) {
        this.mealFoods = mealFoods;
    }

    @Override
    public String toString() {
        String s = MessageFormat.format("{0}: {1}({2})\n", this.id, this.getName(), this.getType());
        for (MealFood mf : this.getMealFoods()) {
            s += mf.getWeight() + "g -> " + mf.getFood().toString() + "\n";
        }
        return s;
    }
}

