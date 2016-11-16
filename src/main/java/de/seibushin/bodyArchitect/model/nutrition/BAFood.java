/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

import de.seibushin.bodyArchitect.helper.Utils;

public class BAFood implements BANutritionUnit {
    private int id;
    private String name;
    double weight;
    private double portion;
    double kcal;
    double protein;
    double fat;
    double carbs;
    double sugar;

    // for resetting the portion
    private double tmpPortion;

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
        return portioning(kcal);
    }

    @Override
    public double getProtein() {
        return portioning(protein);
    }

    @Override
    public double getFat() {
        return portioning(fat);
    }

    @Override
    public double getCarbs() {
        return portioning(carbs);
    }

    @Override
    public double getSugar() {
        return portioning(sugar);
    }

    @Override
    public String getSubText() {
        //@todo remove PortionGetter und allways output the portionised value!?
        return portion + Utils.getString("food.cell.weight.unit");
    }

    @Override
    public boolean isMeal() {
        return false;
    }

    @Override
    public double getPortion() {
        return portion;
    }

    @Override
    public void setPortion(double portion) {
        this.portion = portion;
    }

    public void resetPortion() {
        portion = tmpPortion;
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
