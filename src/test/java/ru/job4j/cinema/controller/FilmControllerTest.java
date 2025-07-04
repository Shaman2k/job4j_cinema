package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.service.FilmService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmControllerTest {
    private FilmService filmService;
    private FilmController filmController;

    @BeforeEach
    public void initServices() {
        filmService = mock(FilmService.class);
        filmController = new FilmController(filmService);
    }

    @Test
    public void whenRequestFilmsListPageThenGetPageWithFilms() {
        var filmDto1 = new FilmDto();
        var filmDto2 = new FilmDto();
        var expectedFilms = List.of(filmDto1, filmDto2);
        when(filmService.findAll()).thenReturn(expectedFilms);

        var model = new ConcurrentModel();
        var view = filmController.getAll(model);
        var actualFilms = model.getAttribute("films");

        assertThat(view).isEqualTo("films/list");
        assertThat(actualFilms).isEqualTo(expectedFilms);
    }

    @Test
    public void whenRequestFilmByIdAndFoundThenGetPageWithFilm() {
        var filmDto = new FilmDto();
        var model = new ConcurrentModel();
        when(filmService.findById(1)).thenReturn(Optional.of(filmDto));
        var view = filmController.getById(model, 1);
        var actualFilm = model.getAttribute("film");

        assertThat(view).isEqualTo("films/one");
        assertThat(actualFilm).isEqualTo(filmDto);
    }

    @Test
    public void whenRequestFilmByIdAndNotFoundThenGetErrorPage() {
        var model = new ConcurrentModel();
        when(filmService.findById(1)).thenReturn(Optional.empty());
        var view = filmController.getById(model, 1);

        assertThat(view).isEqualTo("errors/404");
        assertThat(model.getAttribute("message"))
                .isEqualTo("Film not found");
    }
}