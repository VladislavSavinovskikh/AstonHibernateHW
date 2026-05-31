package com.hibernatehw.DAO;

import com.hibernatehw.util.TransactionHelper;
import com.hibernatehw.model.User;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

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
        log.info("Запрос всех пользователей из БД");
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
    public Optional<User> findUserById(Long id) {
        log.info("Попытка получения пользователя по id: {}", id);
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            log.error("Ошибка при поиске пользователя с id {}: {}", id, e.getMessage());
            return Optional.empty();
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
            log.debug("Пользователь с ID {} успешно обновлен", user.getId());
        });
    }

    @Override
    public void delete(Long id) {
        log.warn("Удаление пользователя с ID: {}", id);
        transactionHelper.executeInTransaction(session -> {
            User userToDelete = session.get(User.class, id);
            session.remove(userToDelete);
        });
        log.debug("Пользователь с ID {} удален из БД", id);
    }
}
