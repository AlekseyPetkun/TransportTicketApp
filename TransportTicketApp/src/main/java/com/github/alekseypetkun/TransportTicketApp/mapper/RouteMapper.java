package com.github.alekseypetkun.TransportTicketApp.mapper;

import com.github.alekseypetkun.TransportTicketApp.dto.*;
import com.github.alekseypetkun.TransportTicketApp.model.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Маппинг сущности перевозчика
 */
@Mapper(componentModel = "spring")
public interface RouteMapper {

    /**
     * Преобразует сущность в дто
     *
     * @param entity сущность
     * @return дто
     */
    RouteDto map(Route entity);

    /**
     * Преобразует дто в сущность
     *
     * @param dto дто
     * @return сущность
     */
    @Mapping(source = "carrier", target = "carrier.id")
    Route map(CreateRoute dto);

    /**
     * Преобразует дто в сущность не изменяя поля на null
     *
     * @param dto    ДТО
     * @param entity сущность
     */
    @Mapping(source = "departure", target = "departure",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "destination", target = "destination",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "carrier", target = "carrier.id",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "durations_min", target = "durations_min",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(UpdateRoute dto, @MappingTarget Route entity);
}
