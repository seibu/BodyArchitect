/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Settings {
    private final IntegerProperty targetKcal = new SimpleIntegerProperty(2000);
    private final IntegerProperty targetProtein = new SimpleIntegerProperty(180);
    private final IntegerProperty targetFat = new SimpleIntegerProperty(90);
    private final IntegerProperty targetCarbs = new SimpleIntegerProperty(220);

    public int getTargetKcal() {
        return targetKcal.get();
    }

    public IntegerProperty targetKcalProperty() {
        return targetKcal;
    }

    public void setTargetKcal(int targetKcal) {
        this.targetKcal.set(targetKcal);
    }

    public int getTargetProtein() {
        return targetProtein.get();
    }

    public IntegerProperty targetProteinProperty() {
        return targetProtein;
    }

    public void setTargetProtein(int targetProtein) {
        this.targetProtein.set(targetProtein);
    }

    public int getTargetFat() {
        return targetFat.get();
    }

    public IntegerProperty targetFatProperty() {
        return targetFat;
    }

    public void setTargetFat(int targetFat) {
        this.targetFat.set(targetFat);
    }

    public int getTargetCarbs() {
        return targetCarbs.get();
    }

    public IntegerProperty targetCarbsProperty() {
        return targetCarbs;
    }

    public void setTargetCarbs(int targetCarbs) {
        this.targetCarbs.set(targetCarbs);
    }
}
