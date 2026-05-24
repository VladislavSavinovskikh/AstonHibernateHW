package com.hibernatehw;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

@Log4j2
public class HibernateDAO implements DAO {

    private final SessionFactory sessionFactory;

    private final TransactionHelper transactionHelper;

    public HibernateDAO(SessionFactory sessionFactory, TransactionHelper transactionHelper) {
        this.sessionFactory = sessionFactory;
        this.transactionHelper = transactionHelper;
    }

    @Override
    public List<User> findAll() {
        log.debug("Запрос всех пользователей из БД");
        try (Session session = sessionFactory.openSession()) {
            return session
                    .createQuery("SELECT u FROM User u", User.class)
                    .list();
        } catch (Exception e) {
            log.error("Ошибка при получении списка пользователей: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось получить список пользователей", e);
        }
    }

    @Override
    public User findUserById(Long id) {
        log.info("Попытка получения пользователя по id: {}", id);
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        } catch (Exception e) {
            log.error("Ошибка при поиске пользователя с id {}: {}", id, e.getMessage());
            return null;
        }
    }

    @Override
    public void save(User user) {
        log.info("Сохранение нового пользователя: {}", user);
        transactionHelper.executeInTransaction(session -> {
            session.persist(user);
            log.debug("Пользователь сохранен, присвоен ID: {}", user.getId());
        });
    }

    @Override
    public void update(User user) {
        log.info("Обновление пользователя: {}", user);
        transactionHelper.executeInTransaction(session -> {
            session.merge(user);
            log.info("Пользователь с ID {} успешно обновлен", user.getId());
        });
    }

    @Override
    public void delete(Long id) {
        log.warn("Удаление пользователя с ID: {}", id);
        transactionHelper.executeInTransaction(session -> {
            User userToDelete = session.get(User.class, id);
            session.remove(userToDelete);
        });
        log.info("Пользователь с ID {} удален из БД", id);
    }
}
