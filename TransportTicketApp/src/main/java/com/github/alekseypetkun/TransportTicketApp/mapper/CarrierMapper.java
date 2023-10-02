package com.github.alekseypetkun.TransportTicketApp.mapper;

import com.github.alekseypetkun.TransportTicketApp.dto.*;
import com.github.alekseypetkun.TransportTicketApp.model.Carrier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Маппинг сущности перевозчика
 */
@Mapper(componentModel = "spring")
public interface CarrierMapper {

    /**
     * Преобразует сущность в дто
     *
     * @param entity сущность
     * @return дто
     */
    CarrierDto map(Carrier entity);

    /**
     * Преобразует дто в сущность
     *
     * @param dto дто
     * @return сущность
     */
    Carrier map(CreateCarrier dto);

    /**
     * Преобразует дто в сущность не изменяя поля на null
     *
     * @param dto    ДТО
     * @param entity сущность
     */
    @Mapping(source = "name", target = "name",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "phone", target = "phone",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(UpdateCarrier dto, @MappingTarget Carrier entity);
}
