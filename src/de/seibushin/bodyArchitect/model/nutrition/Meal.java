/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect.model.nutrition;

import de.seibushin.bodyArchitect.model.BAField;

import javax.persistence.*;
import java.text.DecimalFormat;
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

    @BAField
    @OneToMany(mappedBy = "meal")
    private Set<MealFood> mealFoods = new HashSet<>();

    @OneToMany(mappedBy = "meal")
    private Set<DayMeal> dayMeals = new HashSet<>();

    @Transient
    @BAField
    double kcal;

    @Transient
    @BAField
    double protein;

    @Transient
    @BAField
    double carbs;

    @Transient
    @BAField
    double sugar;

    @Transient
    @BAField
    double fat;

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

    public String getKcal() {
        if (kcal == 0) {
            mealFoods.stream().forEach(mf -> {
                kcal += mf.getFood().getKcal() / mf.getFood().getWeight() * mf.getWeight();
            });
        }

        return new DecimalFormat("#.##").format(kcal);
    }

    public String getProtein() {
        if (protein == 0) {
            mealFoods.stream().forEach(mf -> {
                protein += mf.getFood().getProtein() / mf.getFood().getWeight() * mf.getWeight();
            });
        }

        return new DecimalFormat("#.##").format(protein);
    }

    public String getCarbs() {
        if (carbs == 0) {
            mealFoods.stream().forEach(mf -> {
                carbs += mf.getFood().getCarbs() / mf.getFood().getWeight() * mf.getWeight();
            });
        }

        return new DecimalFormat("#.##").format(carbs);
    }

    public String getSugar() {
        if (sugar == 0) {
            mealFoods.stream().forEach(mf -> {
                sugar += mf.getFood().getSugar() / mf.getFood().getWeight() * mf.getWeight();
            });
        }

        return new DecimalFormat("#.##").format(sugar);
    }

    public String getFat() {
        if (fat == 0) {
            mealFoods.stream().forEach(mf -> {
                fat += mf.getFood().getFat() / mf.getFood().getWeight() * mf.getWeight();
            });
        }

        return new DecimalFormat("#.##").format(fat);
    }

    @Override
    public String toString() {
        /*String s = MessageFormat.format("{0}: {1}({2})\n", this.id, this.getName(), this.getType());
        for (MealFood mf : this.getMealFoods()) {
            s += mf.getWeight() + "g -> " + mf.getFood().toString() + "\n";
        }*/

        String s = MessageFormat.format("{0} ({1})\n{2}kcal [P {3} | C {4}({5}) | F {6}]\n", this.name, this.type, getKcal(), getProtein(), getCarbs(), getSugar(), getFat());
        for (MealFood mf : this.mealFoods) {
            s += MessageFormat.format("{0}\n", mf);
        }

        return s;
    }
}

