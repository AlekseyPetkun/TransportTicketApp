package com.github.alekseypetkun.TransportTicketApp.dao.impl;

import com.github.alekseypetkun.TransportTicketApp.constant.Role;
import com.github.alekseypetkun.TransportTicketApp.dao.EntityDAO;
import com.github.alekseypetkun.TransportTicketApp.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements EntityDAO<User> {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public User addEntity(User entity) {

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "INSERT INTO users (first_name, last_name, email, password, username, user_role, enabled, created_at, updated_at) " +
                             "VALUES ((?), (?), (?), (?), (?), (?), (?), (?), (?))"

             )) {

            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getPassword());
            statement.setString(5, entity.getUsername());
            statement.setString(6, entity.getRole().name());
            statement.setBoolean(7, entity.isEnabled());
            statement.setTimestamp(8, Timestamp.valueOf(entity.getCreatedAt()));
            statement.setTimestamp(9, Timestamp.valueOf(entity.getUpdatedAt()));

            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("SQLException in addEntity method ", e);
        }

        return entity;
    }

    public User findByUsername(String username) {

        User entity = new User();

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "SELECT * FROM users " +
                             "WHERE username LIKE (?) "

             )) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                entity.setId(resultSet.getLong("id"));
                entity.setFirstName(resultSet.getString("first_name"));
                entity.setLastName(resultSet.getString("last_name"));
                entity.setEmail(resultSet.getString("email"));
                entity.setUsername(resultSet.getString("username"));
                entity.setPassword(resultSet.getString("password"));
                entity.setRole(Role.valueOf(resultSet.getString("user_role")));
                entity.setCreatedAt(resultSet.getObject("created_at", LocalDateTime.class));
                entity.setUpdatedAt(resultSet.getObject("updated_at", LocalDateTime.class));
                entity.setEnabled(resultSet.getBoolean("enabled"));
            }
        } catch (SQLException e) {
            log.error("SQLException", e);
        }
        return entity;
    }

    @Override
    public User searchById(Long userId) {

        User entity = new User();

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "SELECT * FROM users " +
                             "WHERE id = (?) "

             )) {

            statement.setLong(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                entity.setId(resultSet.getLong("id"));
                entity.setFirstName(resultSet.getString("first_name"));
                entity.setLastName(resultSet.getString("last_name"));
                entity.setEmail(resultSet.getString("email"));
                entity.setUsername(resultSet.getString("username"));
                entity.setPassword(resultSet.getString("password"));
                entity.setRole(Role.valueOf(resultSet.getString("user_role")));
                entity.setCreatedAt(resultSet.getObject("created_at", LocalDateTime.class));
                entity.setUpdatedAt(resultSet.getObject("updated_at", LocalDateTime.class));
                entity.setEnabled(resultSet.getBoolean("enabled"));
            }
        } catch (SQLException e) {
            log.error("SQLException in searchById method ", e);
        }
        return entity;
    }


    @Override
    public List<User> returnAll(Integer pageNumber, Integer pageSize) {

        List<User> usersList = new ArrayList<>();

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "SELECT * FROM users " +
                             "ORDER BY first_name " +
                             "LIMIT (?)" +
                             "OFFSET (?)"

             )) {

            statement.setLong(1, pageNumber);
            statement.setLong(2, pageSize);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                Role role = Role.valueOf(resultSet.getString("user_role"));
                LocalDateTime createdAt = resultSet.getObject("created_at", LocalDateTime.class);
                LocalDateTime updatedAt = resultSet.getObject("updated_at", LocalDateTime.class);
                boolean enabled = resultSet.getBoolean("enabled");

                User entity = new User(
                        id,
                        username,
                        password,
                        email,
                        role,
                        firstName,
                        lastName,
                        enabled,
                        createdAt,
                        updatedAt);

                usersList.add(entity);
            }
        } catch (SQLException e) {
            log.error("SQLException in returnAll method ", e);
        }
        return usersList;
    }

    @Override
    public void updateEntity(User entity) {

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "UPDATE users SET " +
                             "first_name=(?), " +
                             "last_name=(?), " +
                             "email=(?), " +
                             "username=(?), " +
                             "updated_at=(?) " +
                             "WHERE id=(?)"

             )) {

            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getUsername());
            statement.setTimestamp(5, Timestamp.valueOf(entity.getUpdatedAt()));
            statement.setLong(6, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException in updateEntity method ", e);
        }
    }

    @Override
    public void deleteEntityById(Long userId) {

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "DELETE FROM users WHERE id=(?)"

             )) {

            statement.setLong(1, userId);
            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("SQLException in deleteEntityById method ", e);
        }
    }

    public Long count() {

        Long count = null;

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "SELECT COUNT(*) AS recordCount FROM users"

             )) {

            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            count = resultSet.getLong("recordCount");
            resultSet.close();

        } catch (SQLException e) {
            log.error("SQLException in searchById method ", e);
        }

        return count;
    }
}
