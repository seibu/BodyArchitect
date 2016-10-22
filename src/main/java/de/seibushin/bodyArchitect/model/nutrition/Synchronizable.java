/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

public interface Synchronizable extends BANutritionUnit {
    public boolean isSaved();
    public void setSaved(boolean saved);

    /**
     * this might be either the amount of Portions or the weight of the Food
     * @return
     */
    public double getPortionSize();

    public BANutritionUnit getNutritionUnit();

    public int getNutritionUnitId();

}
