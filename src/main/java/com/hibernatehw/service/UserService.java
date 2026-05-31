package com.hibernatehw.service;

import com.hibernatehw.DAO.HibernateDAO;
import com.hibernatehw.model.User;
import com.hibernatehw.util.HibernateUtil;
import com.hibernatehw.util.TransactionHelper;
import com.hibernatehw.util.UserParser;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

@Log4j2
public class UserService {

    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    TransactionHelper transactionHelper = new TransactionHelper(sessionFactory);

    HibernateDAO hibernateDAO = new HibernateDAO(sessionFactory, transactionHelper);

    public void showUsers() {
        log.debug("Вывод списка всех пользователей");
        List<User> userList = hibernateDAO.findAll();
        if (userList.isEmpty()) {
            log.warn("Список пользователей пуст");
            System.out.println("Нет пользователей в базе данных");
        } else {
            log.info("Вывод {} пользователей", userList.size());
            for (User user : userList) {
                System.out.println(user);
            }
        }
    }

    public boolean addUser(String userToParse) {
        log.info("Попытка добавления пользователя с данными: {}", userToParse);
        try {
            User user = UserParser.parse(userToParse);
            log.debug("Распаршенный пользователь: {}", user);

            hibernateDAO.save(user);
            log.info("Пользователь успешно добавлен с ID: {}", user.getId());
            return true;
        } catch (IllegalArgumentException e) {
            log.error("Ошибка валидации при добавлении пользователя: {}", e.getMessage());
            System.out.println("Ошибка ввода данных: " + e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Неожиданная ошибка при добавлении пользователя: {}", e.getMessage(), e);
            System.out.println("Ошибка при добавлении пользователя: " + e.getMessage());
            return false;
        }
    }

    public void deleteUser(Long id) {
        log.info("Попытка удаления пользователя с id: {}", id);
        try {
            // Добавляем проверку существования пользователя
            Optional<User> userOpt = getUserById(id);
            if (userOpt.isPresent()) {
                hibernateDAO.delete(id);
                log.info("Пользователь с ID {} успешно удален", id);
            } else {
                log.warn("Попытка удалить несуществующего пользователя с ID: {}", id);
                System.out.println("Пользователь с ID " + id + " не найден");
            }
        } catch (Exception e) {
            log.error("Ошибка при удалении пользователя с ID {}: {}", id, e.getMessage(), e);
            System.out.println("Ошибка при удалении пользователя");
        }
    }

    public Optional<User> getUserById(Long id) {
        log.info("Поиск пользователя по ID: {}", id);
        return hibernateDAO.findUserById(id);
    }

    public boolean updateUser(User existingUser, String updatedUser) {
        log.info("Попытка обновления пользователя: {}, обновленные данные: {}", existingUser, updatedUser);
        try {
            User parsedUser = UserParser.parse(updatedUser);

            existingUser.setName(parsedUser.getName());
            existingUser.setEmail(parsedUser.getEmail());
            existingUser.setAge(parsedUser.getAge());

            hibernateDAO.update(existingUser);
            log.info("Пользователь успешно обновлен: {}", existingUser);
            return true;
        } catch (IllegalArgumentException e) {
            log.error("Ошибка при обновлении пользователя: {}", e.getMessage());
            System.out.println("Ошибка ввода данных: " + e.getMessage());
            return false;
        }
    }
}
