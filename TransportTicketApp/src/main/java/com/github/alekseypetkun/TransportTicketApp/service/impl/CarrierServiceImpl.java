package com.github.alekseypetkun.TransportTicketApp.service.impl;

import com.github.alekseypetkun.TransportTicketApp.dao.impl.CarrierDaoImpl;
import com.github.alekseypetkun.TransportTicketApp.dto.*;
import com.github.alekseypetkun.TransportTicketApp.exception.AuthorisationException;
import com.github.alekseypetkun.TransportTicketApp.exception.NotFoundCarrierException;
import com.github.alekseypetkun.TransportTicketApp.mapper.CarrierMapper;
import com.github.alekseypetkun.TransportTicketApp.model.*;
import com.github.alekseypetkun.TransportTicketApp.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Бизнес-логика по работе с перевозчиками
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CarrierServiceImpl implements CarrierService {

    private final CarrierDaoImpl carrierDao;
    private final CarrierMapper carrierMapper;
    private final AuthService authService;
    private final UserService userService;

    @Override
    public CarrierDto createCarrier(CreateCarrier dto, Long userId) {

        User admin = userService.findUserById(userId);

        if (authService.checkAccess(admin)) {

            Carrier carrier = carrierMapper.map(dto);
            carrier.setName(dto.getName());
            carrier.setPhone(dto.getPhone());

            CarrierDto newCarrier = carrierMapper.map(carrierDao.addEntity(carrier));
            log.info("IN createCarrier - carrier: {} created", carrier);
            return newCarrier;
        } else {
            throw new AuthorisationException("user is forbidden", "8070_USER_IS_FORBIDDEN");
        }
    }

    @Override
    public CarrierDto updateCarrier(UpdateCarrier dto, Long carrierId, Long userId) {

        User admin = userService.findUserById(userId);

        if (authService.checkAccess(admin)) {

            Carrier carrier = findCarrierById(carrierId);

            carrierMapper.patch(dto, carrier);

            carrierDao.updateEntity(carrier);
            CarrierDto updatedCarrier = carrierMapper.map(carrier);

            log.info("IN updateCarrier - carrier: {} updated", carrier);
            return updatedCarrier;
        } else {
            throw new AuthorisationException("user is forbidden", "8070_USER_IS_FORBIDDEN");
        }
    }

    @Override
    public ResponseWrapperCarriers getAllCarriers(Integer pageNumber, Integer pageSize, Long userId) {

        User admin = userService.findUserById(userId);

        if (authService.checkAccess(admin)) {

            Long totalAmount = carrierDao.count();
            List<CarrierDto> dtoList = new ArrayList<>(carrierDao
                    .returnAll(pageNumber, pageSize)).stream()
                    .map(carrierMapper::map)
                    .toList();

            return new ResponseWrapperCarriers(totalAmount, dtoList);
        } else {
            throw new AuthorisationException("user is forbidden", "8070_USER_IS_FORBIDDEN");
        }
    }

    @Override
    public void deleteCarrier(Long carrierId, Long userId) {

        User admin = userService.findUserById(userId);

        if (authService.checkAccess(admin)) {

            Carrier carrier = findCarrierById(carrierId);

            carrierDao.deleteEntityById(carrierId);
            log.info("IN deleteCarrier - carrier: {} deleted", carrier);
        } else {
            throw new AuthorisationException("user is forbidden", "8070_USER_IS_FORBIDDEN");
        }
    }

    @Override
    public Carrier findCarrierById(Long carrierId) {

        Carrier carrier = carrierDao.searchById(carrierId);
        if (carrier == null) {
            throw new NotFoundCarrierException(carrierId);
        }
        return carrier;
    }
}
