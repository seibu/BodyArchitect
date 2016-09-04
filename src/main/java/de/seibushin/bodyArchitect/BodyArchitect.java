/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect;

import com.gluonhq.charm.glisten.control.CharmListView;
import de.seibushin.bodyArchitect.helper.HibernateUtil;
import de.seibushin.bodyArchitect.model.nutrition.Day;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BodyArchitect {
    private static BodyArchitect ba = null;
    private SessionFactory sessionFactory;
    private BorderPane root;
    private LocalDate selectedDate;

    public BodyArchitect() {
        sessionFactory = HibernateUtil.getSessionFactory();

        // populate DB with test Food
        //createFood();
    }

    public static void init() {
        if (ba == null) {
            ba = new BodyArchitect();
        }
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }

    public BorderPane getRoot() {
        return this.root;
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    private void createFood() {
        Session session = getSession();
        session.beginTransaction();

        Food milk = new Food("Milch", 64, 3.3, 4.8, 4.8, 3.5, 100, 200);
        Food creatine = new Food("Creatine", 0, 0, 0, 0, 0, 3, 3);
        Food whey = new Food("Tasty Whey", 387, 74, 7, 3.5, 7, 100, 30);

        session.save(milk);
        session.save(creatine);
        session.save(whey);

        session.getTransaction().commit();
    }

    public <T> void addEntry(T entry) {
        Session session = getSession();
        session.beginTransaction();

        session.save(entry);

        session.getTransaction().commit();
    }

    public void deleteEntry(Class type, int id) {
        Session session = getSession();
        session.beginTransaction();

        try {
            Object o = type.getConstructor(int.class).newInstance(id);
            session.delete(o);
        } catch (Exception e) {
            e.printStackTrace();
        }

        session.getTransaction().commit();
    }

    public void close() {
        getSession().close();
        sessionFactory.close();
    }

    public static BodyArchitect getInstance() {
        return ba;
    }

    private <T> List<T> getEntity(Class type) {
        Session session = getSession();

        session.beginTransaction();

        // getEnityManager
        EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
        EntityManager em = entityManagerFactory.createEntityManager();

        CriteriaBuilder builder = entityManagerFactory.getCriteriaBuilder();

        CriteriaQuery<T> criteria = builder.createQuery(type);
        Root<T> root = criteria.from(type);
        criteria.select(root);
        List<T> entity = em.createQuery(criteria).getResultList();

        session.getTransaction().commit();

        return entity;
    }



    public void refreshTableView(TableView tableView, Class type) {
        try {
            tableView.setItems(getData(type));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshListView(ListView listView, Class type) {
        try {
            listView.getItems().clear();
            listView.setItems(getData(type));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ObservableList getData(Class type) {
        ObservableList data = FXCollections.observableArrayList();

        List entries = getEntity(type);

        // add all foods to the collection
        for (Object entry : entries) {
            data.add(entry);
        }
        return data;
    }

    public List<Meal> getMealsForDay(LocalDate day) {
        List<Day> days = getEntry(Day.class, day);

        List<Meal> meals = new ArrayList<>();
        days.forEach(d -> d.getDayMeals().forEach(dm -> meals.add(dm.getMeal())));

        return meals;
    }

    public <T> List<T> getEntry(Class type, LocalDate day) {
        Session session = getSession();

        session.beginTransaction();

        // getEnityManager
        EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
        EntityManager em = entityManagerFactory.createEntityManager();

        CriteriaBuilder builder = entityManagerFactory.getCriteriaBuilder();

        CriteriaQuery<T> criteria = builder.createQuery(type);
        Root<T> root = criteria.from(type);
        criteria.select(root);
        criteria.where(builder.equal(root.get("date"), day));
        List<T> entity = em.createQuery(criteria).getResultList();

        session.getTransaction().commit();

        return entity;
    }

    public <T> List<LocalDate> getColumn(Class type, String column) {
        Session session = getSession();

        session.beginTransaction();

        // getEnityManager
        EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
        EntityManager em = entityManagerFactory.createEntityManager();

        CriteriaBuilder builder = entityManagerFactory.getCriteriaBuilder();

        CriteriaQuery<LocalDate> criteria = builder.createQuery(LocalDate.class);
        Root<T> root = criteria.from(type);
        criteria.select(root.get(column).as(LocalDate.class));
        List<LocalDate> entity = em.createQuery(criteria).getResultList();

        session.getTransaction().commit();

        return entity;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }
}
