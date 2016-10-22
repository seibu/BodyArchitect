/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

public class BAFood implements BANutritionUnit, Portionable {
    private int id;
    private String name;
    private double weight;
    private double portion;
    private double kcal;
    private double protein;
    private double fat;
    private double carbs;
    private double sugar;

    // for resetting the portion
    private double tmpPortion;

    /**
     * no argument constructor
     */
    public BAFood() {

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
    public BAFood(int id, String name, double weight, double portion, double kcal, double protein, double fat, double carbs, double sugar) {
        this(name, weight, portion, kcal, protein, fat, carbs, sugar);
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
    public BAFood(String name, double weight, double portion, double kcal, double protein, double fat, double carbs, double sugar) {
        this.name = name;
        this.kcal = kcal;
        this.protein = protein;
        this.carbs = carbs;
        this.sugar = sugar;
        this.fat = fat;
        this.weight = weight;
        this.portion = portion;
        this.tmpPortion = portion;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String getName() {
        return name;
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
        return kcal;
    }

    @Override
    public double getProtein() {
        return protein;
    }

    @Override
    public double getFat() {
        return fat;
    }

    @Override
    public double getCarbs() {
        return carbs;
    }

    @Override
    public double getSugar() {
        return sugar;
    }

    @Override
    public double getPortion() {
        return portion;
    }

    @Override
    public void setPortion(double portion) {
        this.portion = portion;
    }

    @Override
    public void resetPortion() {
        portion = tmpPortion;
    }

    @Override
    public double getPortionedKcal() {
        return portioning(kcal);
    }

    @Override
    public double getPortionedProtein() {
        return portioning(protein);
    }

    @Override
    public double getPortionedFat() {
        return portioning(fat);
    }

    @Override
    public double getPortionedCarbs() {
        return portioning(carbs);
    }

    @Override
    public double getPortionedSugar() {
        return portioning(sugar);
    }

    /**
     * Used for portioning a specific value
     * @param value
     * @return
     */
    private double portioning(double value) {
        return value / weight * portion;
    }
}
