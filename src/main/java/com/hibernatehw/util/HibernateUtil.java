package com.hibernatehw.util;


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
            log.debug("Инициализация SessionFactory");
            SessionFactory factory = new Configuration().configure().buildSessionFactory();
            log.debug("SessionFactory успешно создана");
            return factory;
        } catch (Throwable ex) {
            log.error("Ошибка при создании SessionFactory: {}", ex.getMessage(), ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void shutdown() {
        log.debug("Закрытие SessionFactory");
        getSessionFactory().close();
        log.debug("SessionFactory закрыта");
    }
}
