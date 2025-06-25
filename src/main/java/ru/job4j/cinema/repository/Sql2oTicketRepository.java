package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.cinema.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Repository
public class Sql2oTicketRepository implements TicketRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(Sql2oTicketRepository.class.getName());
    private final Sql2o sql2o;

    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        try (var connection = sql2o.open()) {
            var sql = """
                    INSERT INTO tickets(session_id, row_number, place_number, user_id)
                    VALUES(:sessionId, :rowNumber, :placeNumber, :userId)
                    """;
            var query = connection.createQuery(sql, true)
                    .addParameter("sessionId", ticket.getSessionId())
                    .addParameter("rowNumber", ticket.getRowNumber())
                    .addParameter("placeNumber", ticket.getPlaceNumber())
                    .addParameter("userId", ticket.getUserId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            ticket.setId(generatedId);
            return Optional.of(ticket);
        } catch (Sql2oException e) {
            LOGGER.error("Ошибка при сохранении билета {}", ticket.getId(), e);
        }
        return Optional.empty();
    }
}
