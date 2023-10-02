package com.github.alekseypetkun.TransportTicketApp.dao;

import java.util.List;

/**
 * Сервис по работе с БД.
 */
public interface EntityDAO<T> {

    /**
     * Добавление сущности
     * @param entity сущность
     * @return сущность
     */
    T addEntity(T entity);

    /**
     * Получение объекта по id.
     * @param id идентификатор.
     * @return сущности по идентификатору.
     */
    T searchById(Long id);

    /**
     * Получение всех объектов
     * @return всех сущностей
     */
    List<T> returnAll(Integer pageNumber, Integer pageSize);

    /**
     * Изменение объекта
     * @param entity сущность
     */
    void updateEntity(T entity);

    /**
     * Удаление сущности по его идентификатору
     * @param userId идентификатор сущности
     */
    void deleteEntityById(Long userId);
}
