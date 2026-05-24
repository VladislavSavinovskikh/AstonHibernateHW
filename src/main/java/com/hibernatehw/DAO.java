package com.hibernatehw;

import java.util.List;

public interface DAO {
    List<User> findAll();
    User findUserById(Long id);
    void save(User user);
    void update(User user);
    void delete(Long id);
}
