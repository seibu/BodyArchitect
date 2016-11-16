/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

public interface BANutritionUnit {
    public String getName();

    public int getId();

    public void setId(int id);

    public double getKcal();

    public double getProtein();

    public double getFat();

    public double getCarbs();

    public double getSugar();

    public String getSubText();

    public boolean isMeal();

    /**
     * this might be either the amount of Portions or the weight of the Food
     *
     * @return
     */
    public double getPortion();

    public void setPortion(double portion);

    public void resetPortion();
}
