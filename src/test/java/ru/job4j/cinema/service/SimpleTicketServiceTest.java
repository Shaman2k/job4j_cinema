package ru.job4j.cinema.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleTicketServiceTest {
    private TicketRepository ticketRepository;
    private TicketService ticketService;

    @BeforeEach
    public void init() {
        ticketRepository = mock(TicketRepository.class);
        ticketService = new SimpleTicketService(ticketRepository);
    }

    @Test
    public void thenSaveTicketAndSuccessThenReturnTicketOptional() {
        Ticket ticket = new Ticket(1, 10, 10, 1);
        when(ticketRepository.save(ticket)).thenReturn(Optional.of(ticket));

        Optional<Ticket> optionalTicket = ticketService.save(ticket);

        assertThat(optionalTicket).isPresent();
        assertThat(optionalTicket.get()).isEqualTo(ticket);
    }

    @Test
    public void thenSaveTicketAndErrorThenReturnEmptyOptional() {
        Ticket ticket = new Ticket(1, 10, 10, 1);
        when(ticketRepository.save(ticket)).thenReturn(Optional.empty());

        Optional<Ticket> optionalTicket = ticketService.save(ticket);

        assertThat(optionalTicket).isEmpty();
    }
}