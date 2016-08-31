/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect.model.nutrition;

import de.seibushin.bodyArchitect.model.BAField;

import javax.persistence.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "DAYS")
public class Day {
    @BAField
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DAY_ID")
    private int id;

    @BAField
    private LocalDate date;

    @OneToMany(mappedBy = "day")
    private Set<DayMeal> dayMeals = new HashSet<>();

    public Day() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<DayMeal> getDayMeals() {
        return dayMeals;
    }

    public void setDayMeals(Set<DayMeal> dayMeals) {
        this.dayMeals = dayMeals;
    }

    @Override
    public String toString() {
        String s = MessageFormat.format("{0}: {1}\n", this.id, this.date);
        for (DayMeal dm : this.dayMeals) {
            s += dm.getId() + ": " + dm.getMeal() + "\n";
        }
        return s;
    }

}
