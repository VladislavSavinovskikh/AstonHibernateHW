package com.hibernatehw.util;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.Consumer;

@Log4j2
public class TransactionHelper {

    private final SessionFactory sessionFactory;

    public TransactionHelper(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void executeInTransaction(Consumer<Session> action) {
        Transaction transaction = null;
        log.debug("Начало выполнения операции в транзакции");
        try (Session session = sessionFactory.openSession()) {
            log.debug("Открыта сессия Hibernate: {}", session);

            transaction = session.beginTransaction();
            log.debug("Транзакция начата");

            action.accept(session);
            log.debug("Действие в транзакции выполнено");

            transaction.commit();
            log.debug("Транзакция успешно подтверждена");
        } catch (Exception e) {
            log.error("Ошибка при выполнении транзакции: {}", e.getMessage());
            if (transaction != null && transaction.isActive()) {
                log.warn("Выполняется rollback транзакции");
                transaction.rollback();
                log.debug("Rollback выполнен");
            }
            throw new RuntimeException("Ошибка в транзакции", e);
        }
    }
}
