package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.service.FilmSessionService;
import org.springframework.ui.ConcurrentModel;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class FilmSessionControllerTest {
    private FilmSessionService filmSessionService;
    private FilmSessionController filmSessionController;

    @BeforeEach
    public void initServices() {
        filmSessionService = mock(FilmSessionService.class);
        filmSessionController = new FilmSessionController(filmSessionService);
    }

    @Test
    public void whenRequestSessionsListPageThenGetPageWithSessions() {
        var filmSessionDto1 = new FilmSessionDto();
        var filmSessionDto2 = new FilmSessionDto();
        var expectedSessions = List.of(filmSessionDto1, filmSessionDto2);
        when(filmSessionService.findAll()).thenReturn(expectedSessions);

        var model = new ConcurrentModel();
        var view = filmSessionController.getAll(model);
        var actualSessions = model.getAttribute("filmSessions");

        assertThat(view).isEqualTo("sessions/list");
        assertThat(actualSessions).isEqualTo(expectedSessions);
    }

    @Test
    public void whenRequestSessionByIdAndFoundThenGetPageWithSession() {
        var filmSessionDto = new FilmSessionDto();
        filmSessionDto.setId(1);
        when(filmSessionService.findById(1)).thenReturn(Optional.of(filmSessionDto));
        var model = new ConcurrentModel();
        var view = filmSessionController.getById(model, 1);

        assertThat(view).isEqualTo("sessions/one");
        assertThat(model.getAttribute("filmSession")).isEqualTo(filmSessionDto);
    }

    @Test
    public void whenRequestSessionByIdAndNotFoundThenGetErrorPage() {
        var model = new ConcurrentModel();
        when(filmSessionService.findById(1)).thenReturn(Optional.empty());
        var view = filmSessionController.getById(model, 1);

        assertThat(view).isEqualTo("errors/404");
        assertThat(model.getAttribute("message"))
                .isEqualTo("Film session not found");
    }

}