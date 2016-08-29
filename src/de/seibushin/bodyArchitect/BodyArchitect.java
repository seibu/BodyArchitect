/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect;

import de.seibushin.bodyArchitect.helper.HibernateUtil;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.util.List;

public class BodyArchitect {
    private static BodyArchitect ba = null;
    private SessionFactory sessionFactory;
    private BorderPane root;

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

    public <T> void deleteEntry(T entry) {
        Session session = getSession();
        session.beginTransaction();
        T en = (T) session.merge(entry);
        session.delete(en);

        session.getTransaction().commit();
    }

    public void close() {
        getSession().close();
        sessionFactory.close();
    }

    public static BodyArchitect getBa() {
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
            ObservableList data = FXCollections.observableArrayList();

            List entries = getEntity(type);

            // add all foods to the collection
            for (Object entry : entries) {
                data.add(entry);
            }

            tableView.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
