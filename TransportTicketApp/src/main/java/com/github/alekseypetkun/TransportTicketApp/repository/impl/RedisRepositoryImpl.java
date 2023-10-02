package com.github.alekseypetkun.TransportTicketApp.repository.impl;

import com.github.alekseypetkun.TransportTicketApp.model.Ticket;
import com.github.alekseypetkun.TransportTicketApp.repository.RedisRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * Бизнес-логика по работе с Redis
 */
@Service
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {

    private static final String HASH_KEY = "Ticket";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, Ticket> hashOperations;

    @PostConstruct
    private void init() {

        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public Map<String, Ticket> findAllTickets() {

        return hashOperations.entries(HASH_KEY);
    }

    @Override
    public void add(final Ticket ticket) {

        hashOperations.put(HASH_KEY, String.valueOf(ticket.getId()), ticket);
    }

    @Override
    public void delete(String id) {

        hashOperations.delete(HASH_KEY, id);
    }

    @Override
    public Ticket findTicket(final String id) {

        return (Ticket) hashOperations.get(HASH_KEY, id);
    }
}
