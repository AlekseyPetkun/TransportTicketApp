package com.github.alekseypetkun.TransportTicketApp.dao.impl;

import com.github.alekseypetkun.TransportTicketApp.constant.Reserved;
import com.github.alekseypetkun.TransportTicketApp.dao.EntityDAO;
import com.github.alekseypetkun.TransportTicketApp.model.Carrier;
import com.github.alekseypetkun.TransportTicketApp.model.Route;
import com.github.alekseypetkun.TransportTicketApp.model.Ticket;
import com.github.alekseypetkun.TransportTicketApp.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TicketDaoImpl implements EntityDAO<Ticket> {

    private final UserDaoImpl userDao;

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;


    @Override
    @CacheEvict(value = "tickets", key = "#entity.id")
    public Ticket addEntity(Ticket entity) {

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "INSERT INTO tickets (route, departure_date_time, place_number, price, reserved) " +
                             "VALUES ((?), (?), (?), (?), (?))"

             )) {

            statement.setLong(1, entity.getRoute().getId());
            statement.setObject(2, entity.getDepartureDateTime());
            statement.setInt(3, entity.getPlaceNumber());
            statement.setInt(4, entity.getPrice());
            statement.setString(5, entity.getReserved().name());

            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("SQLException in addEntity method ", e);
        }

        return entity;
    }

    @Override
    @Cacheable(value = "tickets", key = "#ticketId")
    public Ticket searchById(Long ticketId) {

        Ticket entity = new Ticket();

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "SELECT * FROM tickets " +
                             "INNER JOIN routes " +
                             "ON tickets.route=routes.id " +
                             "INNER JOIN carriers " +
                             "ON routes.carrier=carriers.id " +
                             "FULL JOIN users " +
                             "ON tickets.buyer_id = users.id " +
                             "WHERE tickets.id = (?)"

             )) {

            statement.setLong(1, ticketId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                entity.setId(resultSet.getLong("id"));
                entity.setRoute(new Route(resultSet.getLong("route"),
                        resultSet.getString("departure"),
                        resultSet.getString("destination"),
                        new Carrier(resultSet.getLong("carrier"),
                                resultSet.getString("name"),
                                resultSet.getString("phone")),
                        resultSet.getInt("durations_min")));
                entity.setDepartureDateTime(resultSet.getObject("departure_date_time", LocalDateTime.class));
                entity.setPlaceNumber(resultSet.getInt("place_number"));
                entity.setPrice(resultSet.getInt("price"));
                entity.setUser(new User(resultSet.getLong("buyer_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getBoolean("enabled"),
                        resultSet.getObject("created_at", LocalDateTime.class),
                        resultSet.getObject("updated_at", LocalDateTime.class)));
                entity.setReserved(Reserved.valueOf(resultSet.getString("reserved")));

            }
        } catch (SQLException e) {
            log.error("SQLException in searchById method ", e);
        }
        return entity;
    }

    @Override
    public List<Ticket> returnAll(Integer pageNumber, Integer pageSize) {

        List<Ticket> ticketsList = new ArrayList<>();

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "SELECT * FROM tickets " +
                             "INNER JOIN routes " +
                             "ON tickets.route=routes.id " +
                             "INNER JOIN carriers " +
                             "ON routes.carrier=carriers.id " +
                             "WHERE tickets.reserved = 'FREE' " +
                             "AND (departure_date_time >= CURRENT_DATE) " +
                             "ORDER BY tickets.departure_date_time ASC " +
                             "LIMIT (?) OFFSET (?)"

             )) {

            statement.setLong(1, pageNumber);
            statement.setLong(2, pageSize);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                Long id = resultSet.getLong("id");
                LocalDateTime dateTime = resultSet.getObject("departure_date_time", LocalDateTime.class);
                Integer placeNumber = resultSet.getInt("place_number");
                Integer price = resultSet.getInt("price");
                Reserved reserved = Reserved.valueOf(resultSet.getString("reserved"));

                Long routeId = resultSet.getLong("id");
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

                Route route = new Route(
                        routeId,
                        departure,
                        destination,
                        carrier,
                        durations_min);

                Ticket entity = new Ticket(
                        id,
                        route,
                        dateTime,
                        placeNumber,
                        price,
                        reserved);

                ticketsList.add(entity);
            }
        } catch (SQLException e) {
            log.error("SQLException in returnAll method ", e);
        }
        return ticketsList;
    }

    public List<Ticket> findAllByBuyer(Long buyer, Integer pageNumber, Integer pageSize) {

        List<Ticket> ticketsList = new ArrayList<>();

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "SELECT * FROM tickets " +
                             "INNER JOIN routes " +
                             "ON tickets.route=routes.id " +
                             "INNER JOIN carriers " +
                             "ON routes.carrier=carriers.id " +
                             "WHERE tickets.buyer_id = (?) " +
                             "AND (departure_date_time >= CURRENT_DATE) " +
                             "ORDER BY tickets.departure_date_time ASC " +
                             "LIMIT (?) OFFSET (?)"

             )) {

            statement.setLong(1, buyer);
            statement.setLong(2, pageNumber);
            statement.setLong(3, pageSize);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                Long id = resultSet.getLong("id");
                LocalDateTime dateTime = resultSet.getObject("departure_date_time", LocalDateTime.class);
                Integer placeNumber = resultSet.getInt("place_number");
                Integer price = resultSet.getInt("price");
                Reserved reserved = Reserved.valueOf(resultSet.getString("reserved"));

                Long routeId = resultSet.getLong("id");
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

                Route route = new Route(
                        routeId,
                        departure,
                        destination,
                        carrier,
                        durations_min);

                User user = userDao.searchById(buyer);
                Ticket entity = new Ticket(
                        id,
                        route,
                        dateTime,
                        placeNumber,
                        price,
                        reserved,
                        user);

                ticketsList.add(entity);
            }
        } catch (SQLException e) {
            log.error("SQLException in findAllByBuyer method ", e);
        }
        return ticketsList;
    }

    @Override
//    @CacheEvict(value = "tickets", key = "#entity.id")
    public void updateEntity(Ticket entity) {

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "UPDATE tickets SET " +
                             "route=(?), " +
                             "departure_date_time=(?), " +
                             "place_number=(?), " +
                             "price=(?), " +
                             "reserved=(?) " +
                             "WHERE id=(?)"

             )) {

            statement.setLong(1, entity.getRoute().getId());
            statement.setObject(2, entity.getDepartureDateTime());
            statement.setInt(3, entity.getPlaceNumber());
            statement.setInt(4, entity.getPrice());
            statement.setString(5, entity.getReserved().name());
            statement.setLong(6, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException in updateEntity method ", e);
        }
    }

    @CacheEvict(value = "tickets", key = "#entity.id")
    public void buyTicket(Ticket entity) {

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "UPDATE tickets SET " +
                             "buyer_id=(?), " +
                             "reserved=(?) " +
                             "WHERE id=(?)"

             )) {

            statement.setLong(1, entity.getUser().getId());
            statement.setString(2, entity.getReserved().name());
            statement.setLong(3, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException in buyTicket method ", e);
        }
    }

    @CacheEvict(value = "tickets", key = "#entity.id")
    public void canselTicket(Ticket entity) {

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "UPDATE tickets SET " +
                             "buyer_id=(?), " +
                             "reserved=(?) " +
                             "WHERE id=(?)"

             )) {

            statement.setObject(1, entity.getUser());
            statement.setString(2, entity.getReserved().name());
            statement.setLong(3, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException in canselTicket method ", e);
        }
    }

    @Override
    @CacheEvict(value = "tickets", allEntries = true)
    public void deleteEntityById(Long ticketId) {

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "DELETE FROM tickets WHERE id=(?)"

             )) {

            statement.setLong(1, ticketId);
            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("SQLException in deleteEntityById method ", e);
        }
    }

    public Long count(Long userId) {

        Long count = null;

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "SELECT COUNT(*) AS recordCount FROM tickets " +
                             "WHERE buyer_id = (?) " +
                             "AND (departure_date_time >= CURRENT_DATE)"

             )) {

            statement.setLong(1, userId);

            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            count = resultSet.getLong("recordCount");
            resultSet.close();

        } catch (SQLException e) {
            log.error("SQLException in searchById method ", e);
        }

        return count;
    }

    public Long count() {

        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(

                     "SELECT COUNT(*) AS recordCount FROM tickets " +
                             "WHERE reserved = 'FREE' " +
                             "AND (departure_date_time >= CURRENT_DATE)"

             )) {

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong("recordCount");
            }

        } catch (SQLException e) {
            log.error("SQLException in searchById method ", e);
        }

        return null;
    }
}
