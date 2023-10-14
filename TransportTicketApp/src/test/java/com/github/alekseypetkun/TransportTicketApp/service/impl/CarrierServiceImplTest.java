package com.github.alekseypetkun.TransportTicketApp.service.impl;

import com.github.alekseypetkun.TransportTicketApp.constant.Role;
import com.github.alekseypetkun.TransportTicketApp.dao.impl.CarrierDaoImpl;
import com.github.alekseypetkun.TransportTicketApp.dto.CarrierDto;
import com.github.alekseypetkun.TransportTicketApp.dto.CreateCarrier;
import com.github.alekseypetkun.TransportTicketApp.dto.ResponseWrapperCarriers;
import com.github.alekseypetkun.TransportTicketApp.dto.UpdateCarrier;
import com.github.alekseypetkun.TransportTicketApp.mapper.CarrierMapper;
import com.github.alekseypetkun.TransportTicketApp.model.Carrier;
import com.github.alekseypetkun.TransportTicketApp.model.User;
import com.github.alekseypetkun.TransportTicketApp.service.AuthService;
import com.github.alekseypetkun.TransportTicketApp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarrierServiceImplTest {

    @Mock
    private CarrierDaoImpl carrierDao;

    @Mock
    private Connection connection;

    @Mock
    private CarrierMapper carrierMapper;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CarrierServiceImpl carrierService;

    @Test
    void createCarrier() {

        // Создаем тестовые данные для ДТО запроса
        CreateCarrier createCarrierDto = new CreateCarrier();
        createCarrierDto.setName("Test Carrier");
        createCarrierDto.setPhone("1234567890");

        // Создаем тестовые данные для пользователя с правами доступа
        Long userId = 1L;
        User admin = new User();
        admin.setId(userId);
        admin.setRole(Role.ADMIN);

        // Определяем результат, который вернет метод userService.findUserById()
        when(userService.findUserById(userId)).thenReturn(admin);
        // Определяем результат, который вернет метод authService.checkAccess()
        when(authService.checkAccess(admin)).thenReturn(true);

        // Создаем тестовые данные для сущности Carrier
        Carrier carrier = new Carrier();
        carrier.setId(1L);
        carrier.setName("Test Carrier");
        carrier.setPhone("1234567890");

        // Определяем ожидаемый результат
        CarrierDto expectedDto = new CarrierDto(1L, "Test Carrier", "1234567890");

        // Определяем результат, который вернет метод carrierService.createCarrier()
        when(carrierMapper.map(createCarrierDto)).thenReturn(carrier);
        when(carrierDao.addEntity(carrier)).thenReturn(carrier);
        when(carrierMapper.map(carrier)).thenReturn(expectedDto);

        // Вызываем метод carrierService.createCarrier()
        CarrierDto result = carrierService.createCarrier(createCarrierDto, userId);

        // и проверяем результат
        assertNotNull(result);
        assertEquals(expectedDto, result);
        assertEquals("Test Carrier", result.getName());
        assertEquals("1234567890", result.getPhone());
    }

    @Test
    void updateCarrier() {

        // Создаем тестовые данные для ДТО запроса
        UpdateCarrier updateDto = new UpdateCarrier("Update Carrier", "1234567890");

        // Создаем тестовые данные для пользователя с правами доступа
        Long userId = 1L;
        User admin = new User();
        admin.setId(userId);
        admin.setRole(Role.ADMIN);

        // Определяем результат, который вернет метод userService.findUserById()
        when(userService.findUserById(userId)).thenReturn(admin);
        // Определяем результат, который вернет метод authService.checkAccess()
        when(authService.checkAccess(admin)).thenReturn(true);

        // Создаем тестовые данные для сущности Carrier
        Long carrierId = 1L;
        Carrier carrier = new Carrier();
        carrier.setId(carrierId);
        carrier.setName("Test Carrier");
        carrier.setPhone("1234567890");

        // Определяем ожидаемый результат
        CarrierDto expectedDto = new CarrierDto(1L, "Update Carrier", "1234567890");

        // Определяем результат, который вернет метод carrierService.updateCarrier()
        when(carrierDao.searchById(carrierId)).thenReturn(carrier);
        when(carrierMapper.map(carrier)).thenReturn(expectedDto);

        // Вызываем метод carrierService.updateCarrier()
        CarrierDto result = carrierService.updateCarrier(updateDto, carrierId, userId);

        // и проверяем результат
        assertNotNull(result);
        assertEquals("Update Carrier", result.getName());
        assertEquals("1234567890", result.getPhone());
    }

    @Test
    void getAllCarriers() {

        // Создаем тестовые данные для пользователя с правами доступа
        Long userId = 1L;
        User admin = new User();
        admin.setId(userId);
        admin.setRole(Role.ADMIN);

        // Определяем результат, который вернет метод userService.findUserById()
        when(userService.findUserById(userId)).thenReturn(admin);
        // Определяем результат, который вернет метод authService.checkAccess()
        when(authService.checkAccess(admin)).thenReturn(true);

        // Создаем тестовые данные для списка перевозчиков
        Long totalAmount = 100L;
        Integer pageNumber = 1;
        Integer pageSize = 10;

//        List<CarrierDto> dtoList = new ArrayList<>();
//        dtoList.add(new CarrierDto());
//        dtoList.add(new CarrierDto());
//        dtoList.add(new CarrierDto());

        List<Carrier> entityList = new ArrayList<>();
        entityList.add(new Carrier());
        entityList.add(new Carrier());
        entityList.add(new Carrier());

        // Определяем результат, который вернет метод carrierService.getAllCarriers()
        when(carrierDao.count()).thenReturn(totalAmount);
        when(carrierDao.returnAll(pageNumber, pageSize)).thenReturn(entityList);
        when(carrierMapper.map(any(Carrier.class))).thenReturn(new CarrierDto(),
                new CarrierDto(),
                new CarrierDto());

//        when(carrierDao.count()).thenReturn(totalAmount);
//        when(carrierDao.returnAll(pageNumber, pageSize)).thenReturn(entityList);
//        for (Carrier value : entityList) {
//            when(carrierMapper.map(value)).thenReturn(new CarrierDto());
//        }

        // Вызываем метод carrierService.getAllCarriers()
        ResponseWrapperCarriers result = carrierService.getAllCarriers(pageNumber, pageSize, userId);

        // и проверяем результат
        assertNotNull(result);
        assertEquals(totalAmount, result.getCount());
//        assertEquals(dtoList, result.getResults());
        assertEquals(3, result.getResults().size());
    }
}