/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect;

import com.gluonhq.charm.down.Platform;
import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.StorageService;
import com.gluonhq.charm.glisten.layout.Layer;
import com.gluonhq.charm.glisten.layout.layer.SnackbarPopupView;
import de.seibushin.bodyArchitect.model.nutrition.*;
import de.seibushin.bodyArchitect.model.nutrition.plan.Plan;
import de.seibushin.bodyArchitect.model.nutrition.plan.PlanDay;
import de.seibushin.bodyArchitect.views.LogBook;
import de.seibushin.bodyArchitect.views.layers.MealInfoLayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Service {

    private static Service service;

    private LogBook logBook;

    private final Settings settings = new Settings();
    private final ObservableList<BAFood> foods = FXCollections.observableArrayList();
    private final ObservableList<BAMeal> meals = FXCollections.observableArrayList();
    private final ObservableList<BANutritionUnit> all = FXCollections.observableArrayList();
    private final ObservableList<Day> days = FXCollections.observableArrayList();

    private final ObservableList<Plan> plans = FXCollections.observableArrayList();
    private Plan selectedPlan;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E dd.MM.yyyy");

    private final DecimalFormat df = new DecimalFormat("#.#");
    private MealInfoLayer mealInfoLayer;
    private SnackbarPopupView infoLayer;

    private final static String DB_NAME = "ba.db";
    private final static int QUERY_TIMEOUT = 30;

    private final static String FOOD_TABLE = "FOOD";
    private final static String MEAL_TABLE = "MEAL";
    private final static String MEAL_FOOD_TABLE = "MEAL_FOOD";
    private final static String DAYS_TABLE = "DAYS";
    private final static String DAY_MEAL_TABLE = "DAY_MEAL";
    private static final String DAY_FOOD_TABLE = "DAY_FOOD";
    private static final String NUTRITON_PLANS_TABLE = "NUTRITION_PLANS";
    private static final String NUTRITON_PLAN_DAYS_TABLE = "NUTRITION_PLAN_DAYS";
    private static final String NUTRITON_PLAN_DAY_MEALS_TABLE = "NUTRITION_PLAN_DAY_MEALS";
    private static final String NUTRITON_PLAN_DAY_FOOD_TABLE = "NUTRITION_PLAN_DAY_FOOD";

    private Connection connection = null;
    private Statement stmt;
    private PreparedStatement ps;
    private ResultSet rs;

    private final DateTimeFormatter dtfDb = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final NumberFormat intNF = NumberFormat.getInstance(new Locale("de", "DE"));
    private final NumberFormat doubleNF = NumberFormat.getInstance(new Locale("de", "DE"));

    public static void copyDatabase(String pathIni, String pathEnd, String name) {
        try (InputStream myInput = Service.class.getResourceAsStream(pathIni + name);
             OutputStream myOutput = new FileOutputStream(pathEnd + "/" + name)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
        } catch (IOException ex) {
            System.out.println("Error " + ex);
        }
    }

    public Service() {
        // adjust numberformats
        intNF.setMaximumFractionDigits(0);
        doubleNF.setMaximumFractionDigits(1);

        // Database Connector Object
        try {
            Class c = null;
            if (Platform.isAndroid()) {
                c = Class.forName("org.sqldroid.SQLDroidDriver");
            } else if (Platform.isIOS()) {
                c = Class.forName("SQLite.JDBCDriver");
            } else if (Platform.isDesktop()) {
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
        createClearDB();

        // load data
        loadRelevant();

        logBook = new LogBook(days);
    }

    private void createClearDB() {
        String dbUrl = "jdbc:sqlite:";

        File dir;
        try {
            dir = Services.get(StorageService.class)
                    .flatMap(StorageService::getPrivateStorage)
                    .orElseThrow(() -> new IOException("Error: PrivateStorage not available"));
            File db = new File(dir, DB_NAME);
            if (!db.exists()) {
                System.out.println("Info: Database does not exist");
                copyDatabase("/databases/", dir.getAbsolutePath(), DB_NAME);
            }
            dbUrl = dbUrl + db.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            connection = DriverManager.getConnection(dbUrl);
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        if (service == null) {
            service = new Service();
        }
    }

    public static Service getInstance() {
        return service;
    }

    public NumberFormat getIntNF() {
        return intNF;
    }

    public NumberFormat getDoubleNF() {
        return doubleNF;
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

    public ObservableList<BANutritionUnit> getMealsForSelectedDay() {
        Day d = getSelectedDayObject();
        return d.getMeals();
    }

    public LocalDate getSelectedDay() {
        return logBook.getSelectedDay();
    }

    public LogBook getLogBook() {
        return logBook;
    }

    public String getSelectedDayString() {
        return getSelectedDay().format(dtf);
    }

    public DecimalFormat getDf() {
        return df;
    }

    public MealInfoLayer getMealInfoLayer() {
        if (mealInfoLayer == null) {
            mealInfoLayer = new MealInfoLayer();
        }
        return mealInfoLayer;
    }

    public SnackbarPopupView getInfoLayer() {
        if (infoLayer == null) {
            infoLayer = new SnackbarPopupView();
        }
        return infoLayer;
    }

    public void loadRelevant() {
        // load settings

        // load foods
        retriveFoods();

        // load meals
        retrieveMeals();

        // load days
        retrieveDays();

        // load plans
        retrieveNutritionPlans();
    }

    // =================== Settings ==========================

    public void storeSettings() {
        // move to database
    }

    // =================== Food ==========================

    private void retriveFoods() {
        try {
            stmt = connection.createStatement();
            try {
                stmt.setQueryTimeout(QUERY_TIMEOUT);
            } catch (Exception e) {
                // thrown from SQLDroid we can just ignore this
            }

            rs = stmt.executeQuery("select * from " + FOOD_TABLE);
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                //System.out.println(name + " " + id);
                double weight = rs.getDouble(3);
                double portion = rs.getDouble(4);
                double kcal = rs.getDouble(5);
                double protein = rs.getDouble(6);
                double fat = rs.getDouble(7);
                double carbs = rs.getDouble(8);
                double sugar = rs.getDouble(9);
                BAFood f = new BAFood(id, name, weight, portion, kcal, protein, fat, carbs, sugar);
                foods.add(f);
                all.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList<BAFood> getFoods() {
        return foods;
    }

    public void addFood(BAFood food) {
        foods.add(food);
        all.add(food);

        try {
            ps = connection.prepareStatement("insert into " + FOOD_TABLE + " values(null, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            try {
                ps.setQueryTimeout(QUERY_TIMEOUT);
            } catch (Exception e) {
                // thrown from SQLDroid we can just ignore this
            }

            ps.setString(1, food.getName());
            ps.setDouble(2, food.getWeight());
            ps.setDouble(3, food.getPortion());
            // we need to do this to get the unportioned Value
            food.setPortion(food.getWeight());
            ps.setDouble(4, food.getKcal());
            ps.setDouble(5, food.getProtein());
            ps.setDouble(6, food.getFat());
            ps.setDouble(7, food.getCarbs());
            ps.setDouble(8, food.getSugar());

            ps.executeUpdate();
            food.resetPortion();

            // get the Generated Key and Update the Object for later use we might need the id
            rs = ps.getGeneratedKeys();
            rs.next();
            food.setId(rs.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList<BAFood> getFoodsClone() {
        // ???? check
        ObservableList<BAFood> clone = FXCollections.observableArrayList();
        clone.addAll(foods);
        return clone;
    }

    // =================== Meal ==========================

    private void retrieveMeals() {
        ResultSet res = null;
        try {
            ps = connection.prepareStatement("select * from " + MEAL_FOOD_TABLE + " WHERE m_id = ?");
            stmt = connection.createStatement();
            try {
                ps.setQueryTimeout(QUERY_TIMEOUT);
                stmt.setQueryTimeout(QUERY_TIMEOUT);
            } catch (Exception e) {
                // thrown from SQLDroid we can just ignore this
            }

            rs = stmt.executeQuery("select * from " + MEAL_TABLE);
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String tag = rs.getString(3);
                BAMeal meal = new BAMeal(id, name, tag);

                // execute prepared Statement to get all the food for the meal
                ps.setInt(1, id);
                res = ps.executeQuery();
                while (res.next()) {
                    int mf_id = res.getInt(1);
                    int f_id = res.getInt(2);
                    int m_id = res.getInt(3);
                    Double weight = res.getDouble(4);

                    // streams not supported by fxports :///
                    BAFood f = null;
                    for (BAFood tmp : foods) {
                        if (tmp.getId() == f_id) {
                            f = tmp;
                            break;
                        }
                    }

                    BAFoodPortion mf = new BAFoodPortion(f, weight);
                    meal.addFood(mf);
                }

                meals.add(meal);
                all.add(meal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList<BAMeal> getMeals() {
        return meals;
    }

    public ObservableList<BANutritionUnit> getAllNutritionUnits() {
        return all;
    }

    public void addMeal(BAMeal meal) {
        meals.add(meal);
        all.add(meal);

        try {
            ps = connection.prepareStatement("insert into " + MEAL_TABLE + " values(null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            try {
                ps.setQueryTimeout(QUERY_TIMEOUT);
            } catch (Exception e) {
                // thrown from SQLDroid we can just ignore this
            }

            ps.setString(1, meal.getName());
            ps.setString(2, meal.getTag());
            ps.executeUpdate();

            // get the Generated Key and Update the Object for later use we might need the id
            rs = ps.getGeneratedKeys();
            rs.next();
            meal.setId(rs.getInt(1));

            for (BAFoodPortion mf : meal.getFood()) {
                ps = connection.prepareStatement("insert into " + MEAL_FOOD_TABLE + " values(null, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                try {
                    ps.setQueryTimeout(QUERY_TIMEOUT);
                } catch (Exception e) {
                    // thrown from SQLDroid we can just ignore this
                }

                ps.setInt(1, mf.getNutritionUnitId());
                ps.setInt(2, meal.getId());
                ps.setDouble(3, mf.getPortion());
                ps.executeUpdate();

                // get the Generated Key and Update the Object for later use we might need the id
                rs = ps.getGeneratedKeys();
                rs.next();
                mf.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // =================== Day ==========================

    private void retrieveDays() {
        ResultSet res = null;

        try {
            ps = connection.prepareStatement("select * from " + DAY_MEAL_TABLE + " WHERE d_id = ?");
            PreparedStatement ps2 = connection.prepareStatement("select * from " + DAY_FOOD_TABLE + " WHERE d_id = ?");
            stmt = connection.createStatement();
            try {
                ps.setQueryTimeout(QUERY_TIMEOUT);
                ps2.setQueryTimeout(QUERY_TIMEOUT);
                stmt.setQueryTimeout(QUERY_TIMEOUT);
            } catch (Exception e) {
                // thrown from SQLDroid we can just ignore this
            }

            rs = stmt.executeQuery("select * from " + DAYS_TABLE);
            while (rs.next()) {
                int id = rs.getInt(1);
                LocalDate date = LocalDate.parse(rs.getString(2), dtfDb);
                String notice = rs.getString(3);
                Day day = new Day(id, date, notice);

                // execute prepared Statement to get all the food for the meal
                ps.setInt(1, id);
                res = ps.executeQuery();
                while (res.next()) {
                    int dm_id = res.getInt(1);
                    int d_id = res.getInt(2);
                    int m_id = res.getInt(3);
                    double portions = res.getDouble(4);

                    // streams not supported by fxports :///
                    BAMealPortion m = null;
                    for (BAMeal tmp : meals) {
                        if (tmp.getId() == m_id) {
                            m = new BAMealPortion(tmp, portions);
                            m.setSaved(true);
                            break;
                        }
                    }
                    day.addMeal(m);
                }

                ps2.setInt(1, id);
                res = ps2.executeQuery();
                while (res.next()) {
                    int df_id = res.getInt(1);
                    int d_id = res.getInt(2);
                    int f_id = res.getInt(3);
                    double weight = res.getDouble(4);

                    BAFoodPortion m = null;
                    for (BAFood tmp : foods) {
                        if (tmp.getId() == f_id) {
                            m = new BAFoodPortion(tmp, weight);
                            m.setSaved(true);
                        }
                    }
                    day.addMeal(m);
                }

                days.add(day);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList<Day> getDays() {
        return days;
    }

    public ObservableList getCountDays(int c) {
        FilteredList<Day> fDays = days.filtered(day -> day.getDate().isBefore(getSelectedDay()));

        if (c > fDays.size()) {
            c = fDays.size();
        }

        ObservableList<Day> stats = FXCollections.observableArrayList();

        ObservableList<Day> copy = fDays.sorted((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        for (int i = copy.size() - 1; i >= fDays.size() - c; i--) {
            Day d = copy.get(i);
            stats.add(d);
        }

        return stats;
    }

    public void addSimpleMealToDay(BANutritionUnit simpleMeal, int dayId) {
        try {
            ps = connection.prepareStatement("insert into " + DAY_MEAL_TABLE + " values(null, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement ps2 = connection.prepareStatement("insert into " + DAY_FOOD_TABLE + " values(null, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            try {
                ps.setQueryTimeout(QUERY_TIMEOUT);
                ps2.setQueryTimeout(QUERY_TIMEOUT);
            } catch (Exception e) {
                // thrown from SQLDroid we can just ignore this
            }

            if (BAMealPortion.class.isInstance(simpleMeal) && !((BAMealPortion) simpleMeal).isSaved()) {
                ps.setInt(1, dayId);
                ps.setInt(2, ((BAMealPortion) simpleMeal).getNutritionUnitId());
                ps.setDouble(3, simpleMeal.getPortion());
                ps.executeUpdate();
                ((BAMealPortion) simpleMeal).setSaved(true);
            } else if (BAFoodPortion.class.isInstance(simpleMeal) && !((BAFoodPortion) simpleMeal).isSaved()) {
                ps2.setInt(1, dayId);
                ps2.setInt(2, ((BAFoodPortion) simpleMeal).getNutritionUnitId()); // -> Food-ID
                ps2.setDouble(3, ((BAFoodPortion) simpleMeal).getPortion());
                ps2.executeUpdate();
                ((BAFoodPortion) simpleMeal).setSaved(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addDay(Day day) {
        try {
            if (day.getId() == null) {
                ps = connection.prepareStatement("insert into " + DAYS_TABLE + " values(null, ?, ?)", Statement.RETURN_GENERATED_KEYS);

                try {
                    ps.setQueryTimeout(QUERY_TIMEOUT);
                } catch (Exception e) {
                    // thrown from SQLDroid we can just ignore this
                }

                ps.setString(1, day.getDate().format(dtfDb));
                ps.setString(2, day.getNotice());
                ps.executeUpdate();

                // get the Generated Key and Update the Object for later use we might need the id
                rs = ps.getGeneratedKeys();
                rs.next();
                day.setId(rs.getInt(1));

                days.add(day);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDayMeal(BANutritionUnit simpleMeal) {
        System.out.println("deleteDayMeal");
        try {
            if (BAMealPortion.class.isInstance(simpleMeal)) {
                ps = connection.prepareStatement("DELETE FROM " + DAY_MEAL_TABLE + " WHERE id IN (SELECT id FROM " + DAY_MEAL_TABLE + " WHERE d_id = ? AND m_id = ? ORDER BY id DESC LIMIT 1)");
            } else if (BAFoodPortion.class.isInstance(simpleMeal)) {
                ps = connection.prepareStatement("DELETE FROM " + DAY_FOOD_TABLE + " WHERE id IN (SELECT id FROM " + DAY_FOOD_TABLE + " WHERE d_id = ? AND f_id = ? ORDER BY id DESC LIMIT 1)");
            } else {
                return;
            }

            try {
                ps.setQueryTimeout(QUERY_TIMEOUT);
            } catch (Exception e) {
                // thrown from SQLDroid we can just ignore this
            }

            //noinspection JpaQueryApiInspection
            ps.setInt(1, getSelectedDayObject().getId());
            //noinspection JpaQueryApiInspection
            ps.setInt(2, simpleMeal.getId());

            System.out.println(getSelectedDayObject().getId() + " - " + simpleMeal.getId());

            ps.executeUpdate();
            getSelectedDayObject().removeMeal(simpleMeal);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // =================== Plan ==========================

    private void retrieveNutritionPlans() {
        ResultSet res = null;
        ResultSet res2 = null;

        try {
            ps = connection.prepareStatement("SELECT * FROM " + NUTRITON_PLAN_DAYS_TABLE + " WHERE np_id = ?");
            PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM " + NUTRITON_PLAN_DAY_MEALS_TABLE + " WHERE npd_id = ?");
            stmt = connection.createStatement();
            try {
                ps.setQueryTimeout(QUERY_TIMEOUT);
                ps2.setQueryTimeout(QUERY_TIMEOUT);
                stmt.setQueryTimeout(QUERY_TIMEOUT);
            } catch (Exception e) {
                // thrown from SQLDroid we can just ignore this
            }

            rs = stmt.executeQuery("SELECT * FROM " + NUTRITON_PLANS_TABLE);
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                double kcal = rs.getDouble(3);
                double protein = rs.getDouble(4);
                double fat = rs.getDouble(5);
                double carbs = rs.getDouble(6);
                double sugar = rs.getDouble(7);
                boolean selected = rs.getBoolean(8);

                Plan plan = new Plan(id, name, kcal, protein, fat, carbs, sugar, selected);

                // execute prepared Statement to get all the days for the plan
                ps.setInt(1, id);
                res = ps.executeQuery();
                while (res.next()) {
                    int d_id = res.getInt(1);
                    int np_id = res.getInt(2);
                    int d_order = res.getInt(3);

                    PlanDay planDay = new PlanDay(d_id, np_id, d_order);
                    plan.addDay(planDay);

                    ps2.setInt(1, d_id);
                    res2 = ps2.executeQuery();
                    // @todo add support for Food
                    while (res2.next()) {
                        int m_id = res2.getInt(3);
                        double portions = res2.getDouble(4);

                        BAMealPortion m = null;
                        for (BAMeal tmp : meals) {
                            if (tmp.getId() == m_id) {
                                m = new BAMealPortion(tmp, 1);
                                m.setSaved(true);
                            }
                        }
                        planDay.addMeal(m);
                    }
                }

                plans.add(plan);
                if (plan.isSelected()) {
                    selectedPlan = plan;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (res != null) {
                    res.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (res2 != null) {
                    res2.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addSimpleMealToPlanDay(BANutritionUnit simpleMeal, int planId) {
        try {
            ps = connection.prepareStatement("insert into " + NUTRITON_PLAN_DAY_MEALS_TABLE + " values(null, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement ps2 = connection.prepareStatement("insert into " + NUTRITON_PLAN_DAY_FOOD_TABLE + " values(null, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            try {
                ps.setQueryTimeout(QUERY_TIMEOUT);
                ps2.setQueryTimeout(QUERY_TIMEOUT);
            } catch (Exception e) {
                // thrown from SQLDroid we can just ignore this
            }

            if (BAMealPortion.class.isInstance(simpleMeal) && !((BAMealPortion) simpleMeal).isSaved()) {
                ps.setInt(1, planId);
                ps.setInt(2, ((BAMealPortion) simpleMeal).getNutritionUnitId());
                ps.setDouble(3, simpleMeal.getPortion());
                ps.executeUpdate();
                ((BAMealPortion) simpleMeal).setSaved(true);
            } else if (BAFoodPortion.class.isInstance(simpleMeal) && !((BAFoodPortion) simpleMeal).isSaved()) {
                ps2.setInt(1, planId);
                ps2.setInt(2, ((BAFoodPortion) simpleMeal).getNutritionUnitId()); // -> Food-ID
                ps2.setDouble(3, ((BAFoodPortion) simpleMeal).getPortion());
                ps2.executeUpdate();
                ((BAFoodPortion) simpleMeal).setSaved(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePlanDayMeal(BANutritionUnit simpleMeal, int planDayId) {
        System.out.println("deletePlanDayMeal " + simpleMeal.getName() + " " + planDayId);
        try {
            if (BAMealPortion.class.isInstance(simpleMeal)) {
                ps = connection.prepareStatement("DELETE FROM " + NUTRITON_PLAN_DAY_MEALS_TABLE + " WHERE id IN (SELECT id FROM " + NUTRITON_PLAN_DAY_MEALS_TABLE + " WHERE npd_id = ? AND m_id = ? ORDER BY id DESC LIMIT 1)");
            } else {
                ps = connection.prepareStatement("DELETE FROM " + NUTRITON_PLAN_DAY_FOOD_TABLE + " WHERE id IN (SELECT id FROM " + NUTRITON_PLAN_DAY_FOOD_TABLE + " WHERE npd_id = ? AND f_id = ? ORDER BY id DESC LIMIT 1)");
            }

            try {
                ps.setQueryTimeout(QUERY_TIMEOUT);
            } catch (Exception e) {
                // thrown from SQLDroid we can just ignore this
            }

            //noinspection JpaQueryApiInspection
            ps.setInt(1, planDayId);
            //noinspection JpaQueryApiInspection
            ps.setInt(2, simpleMeal.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void selectPlan(Plan selected) {
        try {
            ps = connection.prepareStatement("UPDATE " + NUTRITON_PLANS_TABLE + " SET selected = 1 WHERE id = ?");
            stmt = connection.createStatement();
            try {
                ps.setQueryTimeout(QUERY_TIMEOUT);
                stmt.setQueryTimeout(QUERY_TIMEOUT);
            } catch (Exception e) {
                // thrown from SQLDroid we can just ignore this
            }

            // deselect all
            stmt.execute("UPDATE " + NUTRITON_PLANS_TABLE + " SET selected = 0 WHERE selected = 1");
            // select the new
            ps.setInt(1, selected.getId());
            ps.execute();

            // deselect all Plans and selecte the newly selected one
            for (Plan p : plans) {
                p.setSelected(false);
            }
            selected.setSelected(true);
            selectedPlan = selected;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPlan(Plan plan) {
        try {
            // existing plan update!!
            if (plan.getId() != null) {
                //@todo implement
            }
            // new plan insert
            else {
                ps = connection.prepareStatement("INSERT INTO " + NUTRITON_PLANS_TABLE + " VALUES(null, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

                try {
                    ps.setQueryTimeout(QUERY_TIMEOUT);
                } catch (Exception e) {
                    // thrown from SQLDroid we can just ignore this
                }

                // set the values
                ps.setString(1, plan.getName());
                ps.setDouble(2, plan.getKcal());
                ps.setDouble(3, plan.getProtein());
                ps.setDouble(4, plan.getFat());
                ps.setDouble(5, plan.getCarbs());
                ps.setDouble(6, plan.getSugar());
                ps.setBoolean(7, plan.isSelected());

                ps.execute();
                rs = ps.getGeneratedKeys();
                rs.next();
                plan.setId(rs.getInt(1));

                // add the plan to the list
                plans.add(plan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPlanDay(PlanDay pd) {
        try {
            ps = connection.prepareStatement("INSERT INTO " + NUTRITON_PLAN_DAYS_TABLE + " VALUES(null, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            try {
                ps.setQueryTimeout(QUERY_TIMEOUT);
            } catch (Exception e) {
                // thrown from SQLDroid we can just ignore this
            }

            // set the values
            ps.setInt(1, pd.getNp_id());
            ps.setInt(2, pd.getOrder());

            ps.execute();
            rs = ps.getGeneratedKeys();
            rs.next();
            pd.setId(rs.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList<Plan> getPlans() {
        return plans;
    }

    public Settings getSettings() {
        return settings;
    }
}