package com.github.alekseypetkun.TransportTicketApp.dao.impl;

import com.github.alekseypetkun.TransportTicketApp.dao.EntityDAO;
import com.github.alekseypetkun.TransportTicketApp.model.Carrier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CarrierDaoImpl implements EntityDAO<Carrier> {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public Carrier addEntity(Carrier entity) {

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "INSERT INTO carriers (name, phone) " +
                             "VALUES ((?), (?))"

             )) {

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getPhone());

            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("SQLException in addEntity method ", e);
        }

        return entity;
    }

    @Override
    public Carrier searchById(Long carrierId) {

        Carrier entity = new Carrier();

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "SELECT * FROM carriers " +
                             "WHERE id = (?) "

             )) {

            statement.setLong(1, carrierId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                entity.setPhone(resultSet.getString("phone"));
            }
        } catch (SQLException e) {
            log.error("SQLException in searchById method ", e);
        }
        return entity;
    }


    @Override
    public List<Carrier> returnAll(Integer pageNumber, Integer pageSize) {

        List<Carrier> carriersList = new ArrayList<>();

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "SELECT * FROM carriers " +
                             "ORDER BY name " +
                             "LIMIT (?)" +
                             "OFFSET (?)"

             )) {

            statement.setLong(1, pageNumber);
            statement.setLong(2, pageSize);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String phone = resultSet.getString("phone");

                Carrier entity = new Carrier(
                        id,
                        name,
                        phone);

                carriersList.add(entity);
            }
        } catch (SQLException e) {
            log.error("SQLException in returnAll method ", e);
        }
        return carriersList;
    }

    @Override
    public void updateEntity(Carrier entity) {

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "UPDATE carriers SET name=(?), phone=(?) " +
                             "WHERE id=(?)"

             )) {

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getPhone());
            statement.setLong(3, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException in updateEntity method ", e);
        }
    }

    @Override
    public void deleteEntityById(Long carrierId) {

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "DELETE FROM carriers WHERE id=(?)"

             )) {

            statement.setLong(1, carrierId);
            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("SQLException in deleteEntityById method ", e);
        }
    }

    public Long count() {

        Long count = null;

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "SELECT COUNT(*) AS recordCount FROM carriers"

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
