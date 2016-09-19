/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.model.refactoring;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Day {
    private LocalDate date;
    private List<Meal> meals;
    // add Settings

    public Day() {
        meals = new ArrayList<>();
    }

    public Day(LocalDate date, List<Meal> meals) {
        this.date = date;
        this.meals = meals;
    }

}
