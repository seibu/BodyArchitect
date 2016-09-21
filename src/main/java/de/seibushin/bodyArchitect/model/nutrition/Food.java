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
    private Double weight;
    private Double portion;
    private Double kcal;
    private Double protein;
    private Double fat;
    private Double carbs;
    private Double sugar;

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
    public Food(int id, String name, double weight, double portion, double kcal, double protein, double fat, double carbs, double sugar) {
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
    public Food(String name, double weight, double portion, double kcal, double protein, double fat, double carbs, double sugar) {
        this.name = name;
        this.kcal = kcal;
        this.protein = protein;
        this.carbs = carbs;
        this.sugar = sugar;
        this.fat = fat;
        this.weight = weight;
        this.portion = portion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getPortion() {
        return portion;
    }

    public void setPortion(Double portion) {
        this.portion = portion;
    }

    public Double getKcal() {
        return kcal;
    }

    public void setKcal(Double kcal) {
        this.kcal = kcal;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getCarbs() {
        return carbs;
    }

    public void setCarbs(Double carbs) {
        this.carbs = carbs;
    }

    public Double getSugar() {
        return sugar;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
