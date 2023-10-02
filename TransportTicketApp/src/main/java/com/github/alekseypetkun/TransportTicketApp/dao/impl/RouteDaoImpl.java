package com.github.alekseypetkun.TransportTicketApp.dao.impl;

import com.github.alekseypetkun.TransportTicketApp.dao.EntityDAO;
import com.github.alekseypetkun.TransportTicketApp.model.Carrier;
import com.github.alekseypetkun.TransportTicketApp.model.Route;
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
public class RouteDaoImpl implements EntityDAO<Route> {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public Route addEntity(Route entity) {

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "INSERT INTO routes (departure, destination, carrier, durations_min) " +
                             "VALUES ((?), (?), (?), (?))"

             )) {

            statement.setString(1, entity.getDeparture());
            statement.setString(2, entity.getDestination());
            statement.setLong(3, entity.getCarrier().getId());
            statement.setInt(4, entity.getDurations_min());

            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("SQLException in addEntity method ", e);
        }

        return entity;
    }

    @Override
    public Route searchById(Long routeId) {

        Route entity = new Route();

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "SELECT * FROM routes " +
                             "INNER JOIN carriers " +
                             "ON routes.carrier = carriers.id AND routes.id=(?)"

             )) {

            statement.setLong(1, routeId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                entity.setId(resultSet.getLong("id"));
                entity.setDeparture(resultSet.getString("departure"));
                entity.setDestination(resultSet.getString("destination"));
                entity.setCarrier(new Carrier(resultSet.getLong("carrier"),
                        resultSet.getString("name"),
                        resultSet.getString("phone")));
                entity.setDurations_min(resultSet.getInt("durations_min"));
            }
        } catch (SQLException e) {
            log.error("SQLException in searchById method ", e);
        }
        return entity;
    }


    @Override
    public List<Route> returnAll(Integer pageNumber, Integer pageSize) {

        List<Route> routesList = new ArrayList<>();

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "SELECT * FROM routes " +
                             "INNER JOIN carriers " +
                             "ON routes.carrier = carriers.id " +
                             "ORDER BY departure " +
                             "LIMIT (?)" +
                             "OFFSET (?)"

             )) {

            statement.setLong(1, pageNumber);
            statement.setLong(2, pageSize);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                Long id = resultSet.getLong("id");
                String departure = resultSet.getString("departure");
                String destination = resultSet.getString("destination");
                Integer durations_min = resultSet.getInt("durations_min");

                Carrier carrier = null;
                if (resultSet.getLong("carrier") != 0) {

                    carrier = new Carrier(
                            resultSet.getLong("carrier"),
                            resultSet.getString("name"),
                            resultSet.getString("phone"));
                }

                Route entity = new Route(
                        id,
                        departure,
                        destination,
                        carrier,
                        durations_min);

                routesList.add(entity);
            }
        } catch (SQLException e) {
            log.error("SQLException in returnAll method ", e);
        }
        return routesList;
    }

    @Override
    public void updateEntity(Route entity) {

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "UPDATE routes SET " +
                             "departure=(?), " +
                             "destination=(?), " +
                             "carrier=(?), " +
                             "durations_min=(?) " +
                             "WHERE id=(?)"

             )) {

            statement.setString(1, entity.getDeparture());
            statement.setString(2, entity.getDestination());
            statement.setLong(3, entity.getCarrier().getId());
            statement.setInt(4, entity.getDurations_min());
            statement.setLong(5, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException in updateEntity method ", e);
        }
    }

    @Override
    public void deleteEntityById(Long routeId) {

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "DELETE FROM routes WHERE id=(?)"

             )) {

            statement.setLong(1, routeId);
            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("SQLException in deleteEntityById method ", e);
        }
    }

    public Long count() {

        Long count = null;

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "SELECT COUNT(*) AS recordCount FROM routes"

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
