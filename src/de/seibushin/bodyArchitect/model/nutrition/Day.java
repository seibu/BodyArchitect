/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect.model.nutrition;

import de.seibushin.bodyArchitect.model.BAField;

import javax.persistence.*;
import java.text.DecimalFormat;
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

    public String getKcal() {
        final Double[] kcal = {0.0};
        if (kcal[0] == 0) {
            dayMeals.stream().forEach(dm -> {
                dm.getMeal().getMealFoods().stream().forEach(mf -> {
                    kcal[0] += mf.getFood().kcal / mf.getFood().weight * mf.weight;
                });
            });
        }

        return new DecimalFormat("#.##").format(kcal[0]);
    }

    public String getProtein() {
        final Double[] protein = {0.0};
        if (protein[0] == 0) {
            dayMeals.stream().forEach(dm -> {
                dm.getMeal().getMealFoods().stream().forEach(mf -> {
                    protein[0] += mf.getFood().protein / mf.getFood().weight * mf.weight;
                });
            });
        }

        return new DecimalFormat("#.##").format(protein[0]);
    }

    public String getCarbs() {
        final Double[] carbs = {0.0};
        if (carbs[0] == 0) {
            dayMeals.stream().forEach(dm -> {
                dm.getMeal().getMealFoods().stream().forEach(mf -> {
                    carbs[0] += mf.getFood().carbs / mf.getFood().weight * mf.weight;
                });
            });
        }

        return new DecimalFormat("#.##").format(carbs[0]);
    }

    public String getSugar() {
        final Double[] sugar = {0.0};
        if (sugar[0] == 0) {
            dayMeals.stream().forEach(dm -> {
                dm.getMeal().getMealFoods().stream().forEach(mf -> {
                    sugar[0] += mf.getFood().sugar / mf.getFood().weight * mf.weight;
                });
            });
        }

        return new DecimalFormat("#.##").format(sugar[0]);
    }

    public String getFat() {
        final Double[] fat = {0.0};
        if (fat[0] == 0) {
            dayMeals.stream().forEach(dm -> {
                dm.getMeal().getMealFoods().stream().forEach(mf -> {
                    fat[0] += mf.getFood().fat / mf.getFood().weight * mf.weight;
                });
            });
        }

        return new DecimalFormat("#.##").format(fat[0]);
    }

    @Override
    public String toString() {
        /*
        String s = MessageFormat.format("{0}: {1}\n", this.id, this.date);
        for (DayMeal dm : this.dayMeals) {
            s += dm.getId() + ": " + dm.getMeal() + "\n";
        }
        */

        String s = MessageFormat.format("{0}kcal [P {1} | C {2}({3}) | F {4}]\n", getKcal(), getProtein(), getCarbs(), getSugar(), getFat());
        for (DayMeal dm : this.dayMeals) {
            s += dm.getMeal() + "\n";
        }

        return s;
    }

}
