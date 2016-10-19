/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

public class Food {
    private int id;
    private String name;
    private double weight;
    private double portion;
    private double kcal;
    private double protein;
    private double fat;
    private double carbs;
    private double sugar;
    private boolean snack;

    // for resetting the portion
    private double tmpPortion;

    /**
     * no argument constructor
     */
    public Food() {

    }

    /**
     * constructor with all fields
     *
     * @param id      - id
     * @param name    - name
     * @param kcal    - kcal
     * @param protein - proteins
     * @param carbs   - carbs
     * @param sugar   - sugar
     * @param fat     - fat
     * @param weight  - weight
     * @param portion - portion
     */
    public Food(int id, String name, double weight, double portion, double kcal, double protein, double fat, double carbs, double sugar, boolean snack) {
        this(name, weight, portion, kcal, protein, fat, carbs, sugar, snack);
        this.id = id;
    }

    /**
     * constructor with all fields
     *
     * @param name    - name
     * @param kcal    - kcal
     * @param protein - proteins
     * @param carbs   - carbs
     * @param sugar   - sugar
     * @param fat     - fat
     * @param weight  - weight
     * @param portion - portion
     */
    public Food(String name, double weight, double portion, double kcal, double protein, double fat, double carbs, double sugar, boolean snack) {
        this.name = name;
        this.kcal = kcal;
        this.protein = protein;
        this.carbs = carbs;
        this.sugar = sugar;
        this.fat = fat;
        this.weight = weight;
        this.portion = portion;
        this.tmpPortion = portion;
        this.snack = snack;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public double getPortion() {
        return portion;
    }

    public double getKcal() {
        return kcal;
    }

    public double getPortionKcal() {
        return kcal / weight * portion;
    }

    public double getProtein() {
        return protein;
    }

    public double getPortionProtein() {
        return protein / weight * portion;
    }

    public void setPortion(Double portion) {
        this.portion = portion;
    }

    public double getFat() {
        return fat;
    }

    public double getPortionFat() {
        return fat / weight * portion;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getPortionCarbs() {
        return carbs / weight * portion;
    }

    public double getSugar() {
        return sugar;
    }

    public double getPortionSugar() {
        return sugar / weight * portion;
    }

    public boolean isSnack() {
        return snack;
    }

    @Override
    public String toString() {
        return name;
    }

    public void resetPortion() {
        portion = tmpPortion;
    }
}
