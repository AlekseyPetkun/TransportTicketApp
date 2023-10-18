package com.github.alekseypetkun.TransportTicketApp.service.impl;

import com.github.alekseypetkun.TransportTicketApp.constant.Role;
import com.github.alekseypetkun.TransportTicketApp.dao.impl.CarrierDaoImpl;
import com.github.alekseypetkun.TransportTicketApp.dto.CarrierDto;
import com.github.alekseypetkun.TransportTicketApp.dto.CreateCarrier;
import com.github.alekseypetkun.TransportTicketApp.dto.ResponseWrapperCarriers;
import com.github.alekseypetkun.TransportTicketApp.dto.UpdateCarrier;
import com.github.alekseypetkun.TransportTicketApp.exception.AuthorisationException;
import com.github.alekseypetkun.TransportTicketApp.exception.NotFoundCarrierException;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarrierServiceImplTest {

    @Mock
    private CarrierDaoImpl carrierDao;

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
        doNothing().when(carrierDao).updateEntity(carrier);
        when(carrierMapper.map(carrier)).thenReturn(expectedDto);

        // Вызываем метод carrierService.updateCarrier()
        CarrierDto result = carrierService.updateCarrier(updateDto, carrierId, userId);

        // и проверяем результат
        assertNotNull(result);
        assertEquals("Update Carrier", result.getName());
        assertEquals("1234567890", result.getPhone());
        verify(carrierDao, times(1)).updateEntity(carrier);
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

    @Test
    void deleteCarriers() {

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

        // Определяем результат, который вернет метод carrierService.deleteCarrier()
        when(carrierDao.searchById(anyLong())).thenReturn(carrier);
        doNothing().when(carrierDao).deleteEntityById(carrierId);

        // Вызываем метод carrierService.deleteCarrier()
        carrierService.deleteCarrier(carrier.getId(), userId);

        // и проверяем результат
        verify(carrierDao, times(1)).deleteEntityById(carrier.getId());
    }

    @Test
    public void testFindCarrierById() {

        // Создаем тестовые данные для сущности Carrier
        Long carrierId = 1L;
        Carrier carrier = new Carrier();
        carrier.setId(carrierId);
        when(carrierDao.searchById(carrierId)).thenReturn(carrier);

        // Вызываем метод carrierService.findCarrierById()
        Carrier result = carrierService.findCarrierById(carrierId);

        // и проверяем результат
        assertEquals(carrierId, result.getId());
    }

    @Test
    public void testFindCarrierByIdNotFound() {

        // Создаем тестовые данные для сущности Carrier
        Long carrierId = 2L;
        when(carrierDao.searchById(carrierId)).thenReturn(null);

        // и проверяем результат
        assertThrows(NotFoundCarrierException.class, () ->
            carrierService.findCarrierById(carrierId));
    }

    @Test
    public void shouldThrowAuthorisationException() {

        // Создаем тестовые данные для пользователя без прав доступа
        Long userId = 1L;
        User notAdmin = new User();
        notAdmin.setId(userId);
        notAdmin.setRole(Role.USER);

        // Определяем результат, который вернет метод userService.findUserById()
        when(userService.findUserById(userId)).thenReturn(notAdmin);
        // Определяем результат, который вернет метод authService.checkAccess()
        when(authService.checkAccess(notAdmin)).thenReturn(false);

//        when(carrierService.createCarrier(mock(CreateCarrier.class), eq(userId)))
//                .thenThrow(AuthorisationException.class);
//        when(carrierService.updateCarrier(mock(UpdateCarrier.class), anyLong(), userId))
//                .thenThrow(AuthorisationException.class);
//        when(carrierService.getAllCarriers(any(Integer.class), any(Integer.class), eq(userId)))
//                .thenThrow(AuthorisationException.class);
//        doThrow(AuthorisationException.class).when(carrierService).deleteCarrier(any(Long.class), eq(userId));

        assertThrows(AuthorisationException.class, () ->
                carrierService.createCarrier(any(CreateCarrier.class), userId));

        assertThrows(AuthorisationException.class, () ->
                carrierService.updateCarrier(mock(UpdateCarrier.class), anyLong(), userId));

//        assertThrows(AuthorisationException.class, () ->
//                carrierService.getAllCarriers(anyInt(), anyInt(), userId));

        assertThrows(AuthorisationException.class, () ->
                carrierService.deleteCarrier(anyLong(), userId));
    }
}