/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect;

import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.connect.ConnectState;
import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.gluoncloud.GluonClient;
import com.gluonhq.connect.gluoncloud.GluonClientBuilder;
import com.gluonhq.connect.gluoncloud.OperationMode;
import com.gluonhq.connect.provider.DataProvider;
import de.seibushin.bodyArchitect.helper.LogBook;
import de.seibushin.bodyArchitect.helper.MealInfoLayer;
import de.seibushin.bodyArchitect.model.nutrition.Settings;
import de.seibushin.bodyArchitect.model.nutrition.Day;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
    private static Service service;

    private GluonClient gluonClient;

    private LogBook logBook;

    private ObjectProperty<Settings> settings = new SimpleObjectProperty<>(new Settings());
    private static final String NUTRITION_SETTINGS = "settings-v0";

    private final String FOODS = "foods-v0";
    private final ListProperty<Food> foods = new SimpleListProperty<>(FXCollections.<Food>observableArrayList());

    private final String MEALS = "meals-v0";
    private final ListProperty<Meal> meals = new SimpleListProperty<>(FXCollections.<Meal>observableArrayList());

    private final String DAYS = "days-v0";
    private final ListProperty<Day> days = new SimpleListProperty<>(FXCollections.<Day>observableArrayList());

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E dd.MM.yyyy");

    private NavigationDrawer.Item home;
    private NavigationDrawer.Item nutrition;
    private NavigationDrawer.Item workout;

    private final DecimalFormat df = new DecimalFormat("#.#");
    private MealInfoLayer mealInfoLayer;

    public Service() {
        // create Menu Items
        home = new NavigationDrawer.Item("Home", MaterialDesignIcon.HOME.graphic());
        home.setSelected(true);
        nutrition = new NavigationDrawer.Item("Nutrition", MaterialDesignIcon.LOCAL_DINING.graphic());
        workout = new NavigationDrawer.Item("Workout", MaterialDesignIcon.FITNESS_CENTER.graphic());

        gluonClient = GluonClientBuilder.create().operationMode(OperationMode.LOCAL_ONLY).build();

        foods.addListener((obs, ov, nv) -> {
            System.out.println("foods changed");
            System.out.println(nv);
            if (nv != null) {
                saveFoods();
            }
        });

        // load data
        loadRelevant();
    }

    public static void init() {
        if (service == null) {
            service = new Service();
        }
    }

    public static Service getInstance() {
        return service;
    }

    public Day getDay(LocalDate date) {
        Day day = null;
        List<Day> filtered = daysProperty().stream().filter(d -> d.getDate().equals(date)).collect(Collectors.toList());
        if (filtered.size() > 0) {
            day = filtered.get(0);
        }
        return day;
    }

    public Day getSelectedDayObject() {
        return getDay(logBook.getSelectedDay());
    }

    public ObservableList<Meal> getMealsForSelectedDay() {
        Day d = getSelectedDayObject();
        return d.getMeals();
    }

    public LocalDate getSelectedDay() {
        return logBook.getSelectedDay();
    }

    public LogBook getLogBook() {
        return logBook;
    }

    public void setLogBook(LogBook logBook) {
        this.logBook = logBook;
    }

    public String getSelectedDayString() {
        return getSelectedDay().format(dtf);
    }

    public DecimalFormat getDf() {
        return df;
    }

    public List<NavigationDrawer.Item> getDrawerItems() {
        List items = new ArrayList();
        items.add(home);
        items.add(nutrition);
        items.add(workout);
        return items;
    }

    public MealInfoLayer getMealInfoLayer() {
        return mealInfoLayer;
    }

    public void setMealInfoLayer(MealInfoLayer mealInfoLayer) {
        this.mealInfoLayer = mealInfoLayer;
    }

    public void loadRelevant() {
        //retrieveFoods();
        loadFoods();
        retrieveMeals();
        retrieveDays();
        retrieveSettings();
    }

    // =================== Settings ==========================

    private void retrieveSettings() {
        GluonObservableObject<Settings> settingsTmp = DataProvider.<Settings>retrieveObject(
                gluonClient.createObjectDataReader(NUTRITION_SETTINGS, Settings.class));
        settingsTmp.stateProperty().addListener((obs, ov, nv) -> {
            if (ConnectState.SUCCEEDED.equals(nv) && settingsTmp.get() != null) {
                settings.set(settingsTmp.get());
            }
        });
    }

    public void storeSettings() {
        DataProvider.<Settings>storeObject(settings.get(), gluonClient.createObjectDataWriter(NUTRITION_SETTINGS, Settings.class));
    }

    public ObjectProperty<Settings> settingsProperty() {
        return settings;
    }


    // =================== Food ==========================

    private void retrieveFoods() {
        GluonObservableList<Food> tmp = DataProvider.retrieveList(gluonClient.createListDataReader(FOODS, Food.class));
        tmp.stateProperty().addListener((obs, ov, nv) -> {
            if (ConnectState.SUCCEEDED.equals(nv)) {
                ObservableList<Food> loaded = FXCollections.observableArrayList();
                loaded.addAll(tmp);
                foods.set(loaded);
            }
        });
    }

    public ListProperty<Food> foodsProperty() {
        return foods;
    }

    public void addFood(Food food) {
        //foods.add(food);
        // @todo check difference
        foods.get().add(food);
    }

    public ObservableList<Food> getFoodsClone() {
        ObservableList<Food> clone = FXCollections.observableArrayList();
        foods.get().forEach(f -> {
            clone.add(f.clone());
        });
        return clone;
    }

    // =================== Meal ==========================

    private void retrieveMeals() {
        GluonObservableList<Meal> tmp = DataProvider.retrieveList(gluonClient.createListDataReader(MEALS, Meal.class));
        tmp.stateProperty().addListener((obs, ov, nv) -> {
            if (ConnectState.SUCCEEDED.equals(nv)) {
                meals.set(tmp);
            }
        });
    }

    public ListProperty<Meal> mealsProperty() {
        return meals;
    }

    public void addMeal(Meal meal) {
        meals.get().add(meal);
    }

    // =================== Day ==========================

    private void retrieveDays() {
        GluonObservableList<Day> tmp = DataProvider.retrieveList(gluonClient.createListDataReader(DAYS, Day.class));
        tmp.stateProperty().addListener((obs, ov, nv) -> {
            if (ConnectState.SUCCEEDED.equals(nv)) {
                days.set(tmp);
            }
        });
    }

    public ListProperty<Day> daysProperty() {
        return days;
    }

    public void addDay(Day day) {
        days.get().add(day);
    }


    // --------------------------------

    private void loadFoods() {
        String location = "./foods.dat";
        try {
            FileInputStream in = new FileInputStream(location);
            ObjectInputStream ois = new ObjectInputStream(in);
            ObservableList<Food> fs = FXCollections.observableArrayList();
            fs.addAll((ArrayList<Food>) ois.readObject());
            foods.set(fs);
            ois.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void saveFoods() {
        String location = "./foods.dat";
        try {
            FileOutputStream outs = new FileOutputStream(location);
            ObjectOutputStream oos = new ObjectOutputStream(outs);
            ArrayList<Food> fs = new ArrayList<>();
            fs.addAll(foods.get());
            oos.writeObject(fs);
            oos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
