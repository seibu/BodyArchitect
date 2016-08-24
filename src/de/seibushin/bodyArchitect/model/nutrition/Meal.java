package de.seibushin.bodyArchitect.model.nutrition;

import javax.persistence.*;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MEALS")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEAL_ID")
    private int id;

    private String name;
    private Type type;

    @OneToMany(mappedBy = "meal")
    private Set<MealFood> mealFoods = new HashSet<MealFood>();

    public Meal() {

    }

    public Meal(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Set<MealFood> getMealFoods() {
        return mealFoods;
    }

    public void setMealFoods(Set<MealFood> mealFoods) {
        this.mealFoods = mealFoods;
    }

    @Override
    public String toString() {
        String s = MessageFormat.format("{0}: {1}({2})\n", this.id, this.getName(), this.getType());
        for (MealFood mf : this.getMealFoods()) {
            s += mf.getWeight() + "g -> " + mf.getFood().toString() + "\n";
        }
        return s;
    }
}

