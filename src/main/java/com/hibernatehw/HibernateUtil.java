package com.hibernatehw;


import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Log4j2
public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            log.info("Инициализация SessionFactory");
            SessionFactory factory = new Configuration().configure().buildSessionFactory();
            log.info("SessionFactory успешно создана");
            return factory;
        } catch (Throwable ex) {
            log.error("Ошибка при создании SessionFactory: {}", ex.getMessage(), ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void shutdown() {
        log.info("Закрытие SessionFactory");
        getSessionFactory().close();
        log.info("SessionFactory закрыта");
    }
}
