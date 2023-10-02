package com.example.ConsumerForTicketService.repository;

import com.example.ConsumerForTicketService.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
