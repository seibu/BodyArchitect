/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect;

import de.seibushin.bodyArchitect.helper.HibernateUtil;
import de.seibushin.bodyArchitect.model.nutrition.Food;
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

    public BodyArchitect() {
        sessionFactory = HibernateUtil.getSessionFactory();

        // populate DB with Food
        createFood();
    }

    public static void init() {
        if (ba == null) {
            ba = new BodyArchitect();
        }
    }

    public Session getSession() {
        return sessionFactory.openSession();
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
        session.close();
    }

    public void close() {
        sessionFactory.close();
    }

    public static BodyArchitect getBa() {
        return ba;
    }

    public <T> List<T> getEntity(Class type) {
        Session session = getSession();

        // getEnityManager
        EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
        EntityManager em = entityManagerFactory.createEntityManager();

        CriteriaBuilder builder = entityManagerFactory.getCriteriaBuilder();



        CriteriaQuery<T> criteria = builder.createQuery(type);
        Root<T> root = criteria.from(type);
        criteria.select(root);
        List<T> entity = em.createQuery(criteria).getResultList();

        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();

        session.close();

        return entity;
    }
}
