/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.nutrition;

import java.time.LocalDate;

public interface NutriStats {
    double getKcal();
    double getProtein();
    double getFat();
    double getCarbs();
    double getSugar();
    LocalDate getDate();
}
