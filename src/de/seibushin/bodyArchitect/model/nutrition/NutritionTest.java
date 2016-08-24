/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect.model.nutrition;

import de.seibushin.bodyArchitect.helper.HibernateUtil;
import de.seibushin.bodyArchitect.model.nutrition.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

public class NutritionTest {
    public static void main(String args[]) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        // create a new Session
        Session session = sessionFactory.openSession();

        // do something database oriented
        //createFood(session);
        //session.getTransaction().commit();

        //printFood(session);

        dayTest(session);
        printDay(session);
        //mealTest(session);
        //printMeal(session);
        //printFood(session);

        session.close();
        sessionFactory.close();
    }

    private static void dayTest(Session session) {
        session.beginTransaction();

        // foods for the meal
        Food apple = new Food("Apfel", 80, 2, 20, 19, 0, 100, 120);
        Food banana = new Food("Banane", 100, 4, 20, 19, 0, 100, 100);

        // new Meal
        Meal meal = new Meal("Frühstück", Type.BREAKFAST);

        // link the food to meal
        MealFood mealFood = new MealFood();
        mealFood.setFood(apple);
        mealFood.setMeal(meal);
        mealFood.setWeight(100);

        // link the food to meal
        MealFood mealFood2 = new MealFood();
        mealFood2.setFood(banana);
        mealFood2.setMeal(meal);
        mealFood2.setWeight(100);

        // save mealFoods meal + food is saved in the process
        session.save(mealFood);
        session.save(mealFood2);

        // foods for the meal
        Food milk = new Food("Milch", 64, 3.3, 4.8, 4.8, 3.5, 100, 200);
        Food creatine = new Food("Creatine", 0, 0, 0, 0, 0, 3, 3);
        Food whey = new Food("Tasty Whey", 387, 74, 7, 3.5, 7, 100, 30);

        // new Meal
        Meal shake = new Meal("Shake", Type.SNACK);

        // link food to meal
        MealFood mf = new MealFood();
        mf.setFood(milk);
        mf.setMeal(shake);
        mf.setWeight(300);

        // link food to meal
        MealFood mf2 = new MealFood();
        mf2.setFood(creatine);
        mf2.setMeal(shake);
        mf2.setWeight(3);

        // link food to meal
        MealFood mf3 = new MealFood();
        mf3.setFood(whey);
        mf3.setMeal(shake);
        mf3.setWeight(30);

        // save mealFoods meal + food is saved in the process
        session.save(mf);
        session.save(mf2);
        session.save(mf3);

        // new Day
        Day day = new Day();
        day.setDate(new Date());
        day.setWeekday(Weekdays.WEDNESDAY);

        // link meal to day
        DayMeal dm = new DayMeal();
        dm.setMeal(shake);
        dm.setDay(day);

        // link meal2 to day
        DayMeal dm2 = new DayMeal();
        dm2.setMeal(meal);
        dm2.setDay(day);

        // save the dayMeals day + meal is saved in the process
        session.save(dm);
        session.save(dm2);

        session.getTransaction().commit();
    }

    private static void printDay(Session session) {
        session.beginTransaction();

        // print all the meals
        EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();
        EntityManager em = entityManagerFactory.createEntityManager();

        CriteriaBuilder builder = entityManagerFactory.getCriteriaBuilder();

        CriteriaQuery<Day> criteria = builder.createQuery(Day.class);
        Root<Day> mealFoodRoot = criteria.from(Day.class);
        criteria.select(mealFoodRoot);
        List<Day> meals = em.createQuery(criteria).getResultList();

        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();

        System.out.println("\n----\n");
        for (final Day m : meals) {
            System.out.println(m);
        }
        System.out.println("\n----\n");

        session.getTransaction().commit();
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
