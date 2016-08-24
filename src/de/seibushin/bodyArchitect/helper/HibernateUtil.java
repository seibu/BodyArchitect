package de.seibushin.bodyArchitect.helper;

import com.fasterxml.classmate.AnnotationConfiguration;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        /*
        Configuration conf = new Configuration().addAnnotatedClass(Food.class).configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();

        return conf.buildSessionFactory(serviceRegistry);
        */

        return new Configuration().configure().buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
