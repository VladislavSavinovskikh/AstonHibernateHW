package com.hibernatehw.DAO;

import com.hibernatehw.model.User;

import java.util.List;
import java.util.Optional;

public interface DAO {
    List<User> findAll();
    Optional<User> findUserById(Long id);
    void save(User user);
    void update(User user);
    void delete(Long id);
}
