/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

public class Settings {
    private int targetKcal;
    private int targetProtein;
    private int targetFat;
    private int targetCarbs;

    public Settings() {

    }

    public Settings(int targetKcal, int targetProtein, int targetFat, int targetCarbs) {
        this.targetKcal = targetKcal;
        this.targetProtein = targetProtein;
        this.targetFat = targetFat;
        this.targetCarbs = targetCarbs;
    }

    public int getTargetKcal() {
        return targetKcal;
    }

    public void setTargetKcal(int targetKcal) {
        this.targetKcal = targetKcal;
    }

    public int getTargetProtein() {
        return targetProtein;
    }

    public void setTargetProtein(int targetProtein) {
        this.targetProtein = targetProtein;
    }

    public int getTargetFat() {
        return targetFat;
    }

    public void setTargetFat(int targetFat) {
        this.targetFat = targetFat;
    }

    public int getTargetCarbs() {
        return targetCarbs;
    }

    public void setTargetCarbs(int targetCarbs) {
        this.targetCarbs = targetCarbs;
    }
}
