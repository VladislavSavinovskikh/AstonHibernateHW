public interface DAO {
    List<User> findAll();
    void save(User user);
    void update(User user);
    void delete(Long id);
}
