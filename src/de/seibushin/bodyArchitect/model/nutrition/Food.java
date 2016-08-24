package de.seibushin.bodyArchitect.model.nutrition;

import javax.persistence.*;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * POJO (=Plain Old Java Object)
 *
 * @author Sebastian Meyer (Seibushin)
 */

@Entity
@Table(name = "FOODS")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FOOD_ID")
    private int id;

    private String name;
    private int kcal;
    private int protein;
    private int carbs;
    private int sugar;
    private int fat;
    private int weight;
    private int portion;

    @OneToMany(mappedBy = "food")
    private Set<MealFood> mealFoods = new HashSet<MealFood>();

    /**
     * no argument constructor required for hibernate
     */
    public Food() {

    }

    /**
     * constructor without id, id is generated by the database
     *
     * @param name
     * @param kcal
     * @param protein
     * @param carbs
     * @param sugar
     * @param fat
     * @param weight
     * @param portion
     */
    public Food(String name, int kcal, int protein, int carbs, int sugar, int fat, int weight, int portion) {
        this.name = name;
        this.kcal = kcal;
        this.protein = protein;
        this.carbs = carbs;
        this.sugar = sugar;
        this.fat = fat;
        this.weight = weight;
        this.portion = portion;
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

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getPortion() {
        return portion;
    }

    public void setPortion(int portion) {
        this.portion = portion;
    }

    public Set<MealFood> getMealFoods() {
        return mealFoods;
    }

    public void setMealFoods(Set<MealFood> mealFoods) {
        this.mealFoods = mealFoods;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{8} {0}: {1}kcal - {2}g (C {3}({4}), P {5}, F {6}) -> {7}g", this.name, this.kcal, this.weight, this.carbs, this.sugar, this.protein, this.fat, this.portion, this.id);
    }
}
