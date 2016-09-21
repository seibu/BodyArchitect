/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect;

import com.gluonhq.charm.down.common.JavaFXPlatform;
import com.gluonhq.charm.down.common.PlatformFactory;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import de.seibushin.bodyArchitect.model.nutrition.*;
import de.seibushin.bodyArchitect.views.LogBook;
import de.seibushin.bodyArchitect.views.layers.MealInfoLayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class Service {
    private static Service service;

    private LogBook logBook;

    private final Settings settings = new Settings();
    private final ObservableList<Food> foods = FXCollections.observableArrayList();
    private final ObservableList<Meal> meals = FXCollections.observableArrayList();
    private final ObservableList<Day> days = FXCollections.observableArrayList();

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E dd.MM.yyyy");

    private NavigationDrawer.Item home;
    private NavigationDrawer.Item nutrition;
    private NavigationDrawer.Item workout;

    private final DecimalFormat df = new DecimalFormat("#.#");
    private MealInfoLayer mealInfoLayer;

    private final static String DB_NAME = "ba.sb";
    private final static int QUERY_TIMEOUT = 30;

    private final static String FOOD_TABLE = "FOOD";
    private final static String MEAL_TABLE = "MEAL";
    private final static String MEAL_FOOD_TABLE = "MEAL_FOOD";
    private final static String DAYS_TABLE = "DAYS";
    private final static String DAY_MEAL_TABLE = "DAY_MEAL";

    private Connection connection = null;
    private Statement stmt;
    private ResultSet rs;

    private String dbUrl = "jdbc:sqlite:";

    private final DateTimeFormatter dtfDb = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final ResourceBundle stmts;

    public Service() {
        // create Menu Items
        home = new NavigationDrawer.Item("Home", MaterialDesignIcon.HOME.graphic());
        home.setSelected(true);
        nutrition = new NavigationDrawer.Item("Nutrition", MaterialDesignIcon.LOCAL_DINING.graphic());
        workout = new NavigationDrawer.Item("Workout", MaterialDesignIcon.FITNESS_CENTER.graphic());

        // Database Connector Object
        try {
            Class c = null;
            if (JavaFXPlatform.isAndroid()) {
                c = Class.forName("org.sqldroid.SQLDroidDriver");
            } else if (JavaFXPlatform.isIOS()) {
                c = Class.forName("SQLite.JDBCDriver");
            } else if (JavaFXPlatform.isDesktop()) {
                c = Class.forName("org.sqlite.JDBC");
            }
            /* embedded
            else if (System.getProperty("os.arch").toUpperCase().contains("ARM")) {
                c = Class.forName("org.sqlite.JDBC");
            }
             */
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // set Url to db
        try {
            File db = new File(PlatformFactory.getPlatform().getPrivateStorage(), DB_NAME);
            dbUrl = dbUrl + db.getAbsolutePath();
            // establish connection
            connection = DriverManager.getConnection(dbUrl);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        stmts = getBundle();

        createDB();

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

    private ResourceBundle getBundle() {
        try {
            ResourceBundle rb = ResourceBundle.getBundle("de.seibushin.bodyArchitect.i18n.DbBundle");
            return rb;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createDB() {
        ResultSet res = null;
        boolean hasTables = false;
        try {
            // check if the database has tables
            DatabaseMetaData meta = connection.getMetaData();
            res = meta.getTables(null, null, "FOOD", new String[] {"TABLE"});

            while (res.next()) {
                hasTables = true;
            }

            // if the database has no tables create them
            if (!hasTables) {
                stmt = connection.createStatement();
                stmt.setQueryTimeout(QUERY_TIMEOUT);

                // execute every statement in the propertyFile
                List<String> keys = Collections.list(stmts.getKeys());
                Collections.sort(keys);

                for (String key : keys) {
                    System.out.println(key);
                    stmt.executeUpdate(stmts.getString(key));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (!hasTables) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Day getDay(LocalDate date) {
        Day day = null;

        // stream / forEach not supported by javafxports
        for (Day d : days) {
            if (d.getDate().equals(date)) {
                day = d;
            }
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
        // load settings

        // load foods
        retriveFoods();

        // load meals
        retrieveMeals();

        // load days
        retrieveDays();

    }

    // =================== Settings ==========================

    public void storeSettings() {
        // move to database
    }


    // =================== Food ==========================

    private void retriveFoods() {
        try {
            stmt = connection.createStatement();
            stmt.setQueryTimeout(QUERY_TIMEOUT);

            rs = stmt.executeQuery("select * from FOOD");
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                Double weight = rs.getDouble(3);
                Double portion = rs.getDouble(4);
                Double kcal = rs.getDouble(5);
                Double protein = rs.getDouble(6);
                Double fat = rs.getDouble(7);
                Double carbs = rs.getDouble(8);
                Double sugar = rs.getDouble(9);
                foods.add(new Food(id, name, weight, portion, kcal, protein, fat, carbs, sugar));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Food> getFoods() {
        return foods;
    }

    public void addFood(Food food) {
        foods.add(food);
        // @todo add to db aswell
    }

    public ObservableList<Food> getFoodsClone() {
        ObservableList<Food> clone = FXCollections.observableArrayList();
        clone.addAll(foods);
        return clone;
    }

    // =================== Meal ==========================

    private void retrieveMeals() {
        try {
            PreparedStatement ps = connection.prepareStatement("select * from MEAL_FOOD WHERE m_id = ?");
            ps.setQueryTimeout(QUERY_TIMEOUT);

            stmt = connection.createStatement();
            stmt.setQueryTimeout(QUERY_TIMEOUT);

            rs = stmt.executeQuery("select * from MEAL");
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                Type type = Type.valueOf(rs.getString(3));
                Meal meal = new Meal(id, name, type);

                // execute prepared Statement to get all the food for the meal
                ps.setInt(1, id);
                ResultSet res = ps.executeQuery();
                while (res.next()) {
                    int mf_id = res.getInt(1);
                    int f_id = res.getInt(2);
                    int m_id = res.getInt(3);
                    Double weight = res.getDouble(4);

                    // streams not supported by fxports :///
                    Food f = null;
                    for (Food tmp : foods) {
                        if (tmp.getId() == f_id) {
                            f = tmp;
                            break;
                        }
                    }

                    MealFood mf = new MealFood(mf_id, f_id, m_id, weight, f);
                    meal.addMealFood(mf);
                }

                meals.add(meal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Meal> getMeals() {
        return meals;
    }

    public void addMeal(Meal meal) {
        meals.add(meal);
        // @todo add to db aswell
    }

    // =================== Day ==========================

    private void retrieveDays() {
        try {
            PreparedStatement ps = connection.prepareStatement("select * from DAY_MEAL WHERE d_id = ?");
            ps.setQueryTimeout(QUERY_TIMEOUT);

            stmt = connection.createStatement();
            stmt.setQueryTimeout(QUERY_TIMEOUT);

            rs = stmt.executeQuery("select * from DAYS");
            while (rs.next()) {
                int id = rs.getInt(1);
                LocalDate date = LocalDate.parse(rs.getString(2), dtfDb);
                Day day = new Day(id, date);

                // execute prepared Statement to get all the food for the meal
                ps.setInt(1, id);
                ResultSet res = ps.executeQuery();
                while (res.next()) {
                    int dm_id = res.getInt(1);
                    int d_id = res.getInt(2);
                    int m_id = res.getInt(3);

                    // streams not supported by fxports :///
                    Meal m = null;
                    for (Meal tmp : meals) {
                        if (tmp.getId() == m_id) {
                            m = tmp;
                            break;
                        }
                    }
                    day.addMeal(m);
                }

                days.add(day);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Day> getDays() {
        return days;
    }

    public void addDay(Day day) {
        days.add(day);
        // @todo add to db aswell
    }

    public Settings getSettings() {
        return settings;
    }
}
