package dam.ad.almacenamiento.mysql.hib;

import dam.ad.persistencia.dto.HamburguesaDTO;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            configuration.addAnnotatedClass(HamburguesaDTO.class);

            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Error al inicializar la SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}