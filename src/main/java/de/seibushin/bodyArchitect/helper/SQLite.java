/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.helper;

import com.gluonhq.charm.down.common.JavaFXPlatform;
import com.gluonhq.charm.down.common.PlatformFactory;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class SQLite {
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

    private final ResourceBundle stmts;

    public SQLite() {
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

        System.out.println("CREATE DB");
        createDB();
    }

    public ObservableList<Food> getFood() {
        ObservableList<Food> foods = FXCollections.observableArrayList();
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
                System.out.println(id + name + weight + portion + kcal + protein + fat + carbs + sugar);
                foods.add(new Food(id, name, weight, portion, kcal, protein, fat, carbs, sugar));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foods;
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

    private ResourceBundle getBundle() {
        try {
            ResourceBundle rb = ResourceBundle.getBundle("de.seibushin.bodyArchitect.i18n.DbBundle");
            return rb;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
