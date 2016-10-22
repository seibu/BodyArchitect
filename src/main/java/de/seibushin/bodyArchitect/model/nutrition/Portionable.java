/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

public interface Portionable {

    public double getPortion();
    public void setPortion(double portion);
    public void resetPortion();

    public double getPortionedKcal();
    public double getPortionedProtein();
    public double getPortionedFat();
    public double getPortionedCarbs();
    public double getPortionedSugar();
}
