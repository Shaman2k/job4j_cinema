package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import ru.job4j.cinema.service.TicketService;

class TicketControllerTest {
    private TicketService ticketService;
    private TicketController ticketController;
    private HttpSession httpSession;

    @BeforeEach
    public void initServices() {
        ticketService = mock(TicketService.class);
        ticketController = new TicketController(ticketService);
        httpSession = mock(HttpSession.class);
    }

    @Test
    public void whenBuyTicketAndSuccessThenGetSuccessPage() {
        User user = new User();
        Ticket ticket = new Ticket();
        when(httpSession.getAttribute("user")).thenReturn(user);
        when(ticketService.save(any(Ticket.class))).thenReturn(Optional.of(ticket));
        Model model = new ConcurrentModel();

        var view = ticketController.buy(model, ticket, httpSession);

        assertThat(view).isEqualTo("tickets/success");
        assertThat(model.getAttribute("ticket")).isEqualTo(ticket);
    }

    @Test
    public void whenBuyTicketAndErrorThenGetErrorPage() {
        User user = new User();
        Ticket ticket = new Ticket();
        when(httpSession.getAttribute("user")).thenReturn(user);
        when(ticketService.save(any(Ticket.class))).thenReturn(Optional.empty());
        Model model = new ConcurrentModel();

        var view = ticketController.buy(model, ticket, httpSession);

        assertThat(view).isEqualTo("tickets/error");
    }
}