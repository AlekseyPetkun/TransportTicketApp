package com.example.ConsumerForTicketService.service.impl;


import com.example.ConsumerForTicketService.dto.*;
import com.example.ConsumerForTicketService.entity.*;
import com.example.ConsumerForTicketService.repository.*;
import com.example.ConsumerForTicketService.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final RouteRepository routeRepository;
    private final CarrierRepository carrierRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public void persistTicket(BuyerTicketDto dto) {

        if (!ticketRepository.existsById(dto.getId())) {

            Ticket ticket = modelMapper.map(dto, Ticket.class);
            Ticket persistedTicket = ticketRepository.save(ticket);

            log.info("Ticket persisted {}", persistedTicket);
        }
    }

    @Override
    public void persistUser(BuyerTicketDto dto) {

        if (!userRepository.existsById(dto.getBuyer().getId())) {

            User user = modelMapper.map(dto.getBuyer(), User.class);
            User persistedUser = userRepository.save(user);

            log.info("User persisted {}", persistedUser);
        }
    }

    @Override
    public void persistRoute(BuyerTicketDto dto) {

        if (!routeRepository.existsById(dto.getRoute().getId())) {

            Route route = modelMapper.map(dto.getRoute(), Route.class);
            Route persistedRoute = routeRepository.save(route);

            log.info("Route persisted {}", persistedRoute);
        }
    }

    @Override
    public void persistCarrier(BuyerTicketDto dto) {

        if (!carrierRepository.existsById(dto.getRoute().getCarrier().getId())) {

            Carrier carrier = modelMapper.map(dto.getRoute().getCarrier(), Carrier.class);
            Carrier persistedCarrier = carrierRepository.save(carrier);

            log.info("Carrier persisted {}", persistedCarrier);
        }
    }
}
