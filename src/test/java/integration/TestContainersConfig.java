package integration;

import com.hibernatehw.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestContainersConfig {

    // Не используем @Container, управляем вручную
    private static final PostgreSQLContainer<?> POSTGRES;

    public static final SessionFactory sessionFactory;

    static {
        POSTGRES = new PostgreSQLContainer<>(DockerImageName.parse("postgres:18-alpine"))
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test")
                .withReuse(true);

        POSTGRES.start();

        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.url", POSTGRES.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", POSTGRES.getUsername());
        configuration.setProperty("hibernate.connection.password", POSTGRES.getPassword());
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.setProperty("hibernate.show_sql", "false");
        configuration.setProperty("hibernate.format_sql", "false");

        configuration.addAnnotatedClass(User.class);

        sessionFactory = configuration.buildSessionFactory();
    }
}