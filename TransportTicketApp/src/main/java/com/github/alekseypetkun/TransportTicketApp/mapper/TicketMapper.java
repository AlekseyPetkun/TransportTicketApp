package com.github.alekseypetkun.TransportTicketApp.mapper;

import com.github.alekseypetkun.TransportTicketApp.dto.*;
import com.github.alekseypetkun.TransportTicketApp.model.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Маппинг сущности билета
 */
@Mapper(componentModel = "spring")
public interface TicketMapper {

    /**
     * Преобразует сущность в дто
     *
     * @param entity сущность
     * @return дто
     */
    TicketDto map(Ticket entity);

    /**
     * Преобразует дто в сущность
     *
     * @param dto дто
     * @return сущность
     */
    @Mapping(source = "routeId", target = "route.id")
    Ticket map(CreateTicket dto);

    /**
     * Преобразует сущность в дто
     *
     * @param entity сущность
     * @return дто
     */
    @Mapping(source = "user", target = "buyer")
    BuyerTicketDto mapToBuyer(Ticket entity);

    /**
     * Преобразует дто в сущность не изменяя поля на null
     *
     * @param dto    ДТО
     * @param entity сущность
     */
    @Mapping(source = "route", target = "route.id",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "departureDateTime", target = "departureDateTime",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "placeNumber", target = "placeNumber",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "price", target = "price",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "reserved", target = "reserved",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(UpdateTicket dto, @MappingTarget Ticket entity);
}
