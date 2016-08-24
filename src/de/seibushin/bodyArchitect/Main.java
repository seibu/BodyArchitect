package de.seibushin.bodyArchitect;

import de.seibushin.bodyArchitect.helper.HibernateUtil;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import de.seibushin.bodyArchitect.model.nutrition.MealFood;
import de.seibushin.bodyArchitect.model.nutrition.Type;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.util.List;

public class Main {
    public static void main(String args[]) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        // create a new Session
        Session session = sessionFactory.openSession();

        // do something database oriented
        //createFood(session);
        //session.getTransaction().commit();

        //printFood(session);

        mealTest(session);
        printMeal(session);
        printFood(session);

        session.close();
        sessionFactory.close();
    }

    private static void mealTest(Session session) {
        session.beginTransaction();
        Food apple = new Food("Apfel", 80, 2, 20, 19, 0, 100, 120);
        Food banana = new Food("Banane", 100, 4, 20, 19, 0, 100, 100);

        Meal meal = new Meal("Frühstück", Type.BREAKFAST);

        MealFood mealFood = new MealFood();
        mealFood.setFood(apple);
        mealFood.setMeal(meal);
        mealFood.setWeight(100);

        MealFood mealFood2 = new MealFood();
        mealFood2.setFood(banana);
        mealFood2.setMeal(meal);
        mealFood2.setWeight(100);

        session.save(mealFood);
        session.save(mealFood2);

        session.getTransaction().commit();
    }

    /**
     *
     * @param session
     */
    //@todo check for dependencies
    private static void deleteMeal(Session session) {
        session.beginTransaction();
        // deletion test for Id 8
        Meal meal = new Meal();
        meal.setId(3);
        session.delete(meal);

        /* mealFood
        MealFood mealFood = new MealFood();
        mealFood.setId(8);
        session.delete(mealFood);
        */
        session.getTransaction().commit();
    }

    private static void printMeal(Session session) {
        session.beginTransaction();
        // print all the meals
        EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
        EntityManager em = entityManagerFactory.createEntityManager();

        CriteriaBuilder builder = entityManagerFactory.getCriteriaBuilder();

        CriteriaQuery<Meal> criteria = builder.createQuery(Meal.class);
        Root<Meal> mealFoodRoot = criteria.from(Meal.class);
        criteria.select(mealFoodRoot);
        List<Meal> meals = em.createQuery(criteria).getResultList();

        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();

        System.out.println("\n----\n");
        for (final Meal m : meals) {
            System.out.println(m);
        }
        System.out.println("\n----\n");
        session.getTransaction().commit();
    }

    private static void createFood(Session session) {
        session.beginTransaction();
        Food food = new Food("Apfel", 80, 2, 20, 19, 0, 100, 120);
        session.save(food);

        Food food2 = new Food("Banane", 100, 4, 20, 19, 0, 100, 100);
        session.save(food2);
        session.getTransaction().commit();
    }

    private static void printFood(Session session) {
        session.beginTransaction();
        EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
        EntityManager em = entityManagerFactory.createEntityManager();

        CriteriaBuilder builder = entityManagerFactory.getCriteriaBuilder();

        CriteriaQuery<Food> criteria = builder.createQuery(Food.class);
        Root<Food> foodRoot = criteria.from(Food.class);
        criteria.select(foodRoot);
        List<Food> foods = em.createQuery(criteria).getResultList();

        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();

        System.out.println("\n----\n");
        System.out.println(MessageFormat.format("Storing {0} foods in the database", foods.size()));
        for (final Food f : foods) {
            System.out.println(f);
        }
        System.out.println("\n----\n");
        session.getTransaction().commit();
    }
}
