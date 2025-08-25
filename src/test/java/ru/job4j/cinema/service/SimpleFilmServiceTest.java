package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;

import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleFilmServiceTest {
    private FilmRepository filmRepository;
    private GenreRepository genreRepository;
    private FilmService filmService;

    @BeforeEach
    public void init() {
        filmRepository = mock(FilmRepository.class);
        genreRepository = mock(GenreRepository.class);
        filmService = new SimpleFilmService(filmRepository, genreRepository);
    }

    @Test
    public void whenFindAllThenReturnAllFilms() {
        Film film1 = new Film("Film1", "New film", 99, 1, 1, 1, 1);
        Film film2 = new Film("Film2", "New film1", 99, 1, 1, 1, 2);
        Genre genre = new Genre("genre");
        when(filmRepository.findAll()).thenReturn(List.of(film1, film2));
        when(genreRepository.getById(1)).thenReturn(Optional.of(genre));

        Collection<FilmDto> result = filmService.findAll();
        assertThat(result.stream().map(FilmDto::getName)).containsExactlyInAnyOrder("Film1", "Film2");
    }

    @Test
    public void whenFindByIdAndSuccessThenReturnFilmOptional() {
        int filmId = 1;
        Film film = new Film("film", "new film", 1, 1, 1, 1, 1);

        when(filmRepository.findById(filmId)).thenReturn(Optional.of(film));

        var optionalFilmDto = filmService.findById(filmId);

        assertThat(optionalFilmDto).isPresent();
        assertThat(optionalFilmDto.get().getName()).isEqualTo(film.getName());
    }

    @Test
    public void whenFindByIdAndNotFoundThenReturnEmptyOptional() {
        when(filmRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        var optionalFilmDto = filmService.findById(1);
        assertThat(optionalFilmDto).isEmpty();
    }
}