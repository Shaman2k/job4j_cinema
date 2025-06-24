package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.cinema.model.User;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class Sql2oUserRepository implements UserRepository {
    private static final Logger LOG = LoggerFactory.getLogger(Sql2oUserRepository.class.getName());
    private final Sql2o sql2o;

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> save(User user) {
        try (var connection = sql2o.open()) {
            var sql = """
                    INSERT INTO users(email, full_name, password)
                    VALUES (:email, :fullName, :password)
                    """;

            var query = connection.createQuery(sql, true)
                    .addParameter("email", user.getEmail())
                    .addParameter("fullName", user.getFullName())
                    .addParameter("password", user.getPassword());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            user.setId(generatedId);
            return Optional.of(user);
        } catch (Sql2oException e) {
            LOG.error("Ошибка при сохранении пользователя {}", user.getEmail(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM users WHERE email = :email AND password = :password")
                    .addParameter("email", email)
                    .addParameter("password", password);
            var user = query.setColumnMappings(User.COLUMN_MAPPING).executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        }

    }
}
