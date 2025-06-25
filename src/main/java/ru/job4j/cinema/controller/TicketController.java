package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.TicketService;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/buy")
    public String buy(Model model, @ModelAttribute Ticket ticket, HttpSession session) {
        User user = (User) session.getAttribute("user");
        ticket.setId(user.getId());
        var ticketOptional = ticketService.save(ticket);
        if (ticketOptional.isEmpty()) {
            return "tickets/error";
        }
        model.addAttribute("ticket", ticket);
        return "tickets/success";
    }
}
