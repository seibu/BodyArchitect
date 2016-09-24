/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

public interface SimpleMeal {

    public String getName();

    public int getId();

    public void setId(int id);

    public double getKcal();

    public double getProtein();

    public double getFat();

    public double getCarbs();

    public double getSugar();

    public Type getType();

    public boolean isMeal();

    public boolean isSaved();

    public void setSaved(boolean saved);
}
