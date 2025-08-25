package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.FilmSessionRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

class SimpleFilmSessionServiceTest {
    private FilmSessionRepository filmSessionRepository;
    private HallService hallService;
    private FilmService filmService;
    private FilmSessionService filmSessionService;

    @BeforeEach
    public void init() {
        filmSessionRepository = mock(FilmSessionRepository.class);
        hallService = mock(HallService.class);
        filmService = mock(FilmService.class);
        filmSessionService = new SimpleFilmSessionService(filmSessionRepository, hallService, filmService);
    }

    @Test
    public void whenFindAllThenReturnFilmSessionDTOList() {
        FilmSession filmSession = new FilmSession();
        filmSession.setId(1);
        filmSession.setFilmId(2);
        filmSession.setHallsId(3);
        filmSession.setStartTime(LocalDateTime.now());
        filmSession.setPrice(100);

        FilmDto filmDto = new FilmDto();
        filmDto.setId(2);
        filmDto.setName("Film");
        filmDto.setDurationInMinutes(120);
        filmDto.setFileId(8);

        Hall hall = new Hall();
        hall.setId(3);
        hall.setName("Hall");
        hall.setDescription("Hall Description");
        hall.setPlaceCount(10);
        hall.setRowCount(10);

        when(filmSessionRepository.findAll()).thenReturn(List.of(filmSession));
        when(filmService.findById(filmSession.getFilmId())).thenReturn(Optional.of(filmDto));
        when(hallService.findById(filmSession.getHallsId())).thenReturn(Optional.of(hall));

        Collection<FilmSessionDto> result = filmSessionService.findAll();

        assertThat(result).hasSize(1);
        FilmSessionDto dto = result.iterator().next();
        assertThat(dto.getFilmName()).isEqualTo("Film");
        assertThat(dto.getHallDescription()).isEqualTo("Hall Description");
        assertThat(dto.getPlaceCount()).isEqualTo(10);
    }

    @Test
    void whenFindByIdAndSuccessThenReturnFilmSessionDto() {
        FilmSession filmSession = new FilmSession();
        filmSession.setId(1);
        filmSession.setFilmId(2);
        filmSession.setHallsId(3);
        filmSession.setStartTime(LocalDateTime.now());
        filmSession.setPrice(100);

        FilmDto filmDto = new FilmDto();
        filmDto.setId(2);
        filmDto.setName("Film");
        filmDto.setDurationInMinutes(120);
        filmDto.setFileId(8);

        Hall hall = new Hall();
        hall.setId(3);
        hall.setName("Hall");
        hall.setDescription("Hall Description");
        hall.setPlaceCount(10);
        hall.setRowCount(10);

        when(filmSessionRepository.findById(1)).thenReturn(Optional.of(filmSession));
        when(filmService.findById(filmSession.getFilmId())).thenReturn(Optional.of(filmDto));
        when(hallService.findById(filmSession.getHallsId())).thenReturn(Optional.of(hall));

        var result = filmSessionService.findById(1);
        assertThat(result).isPresent();
        assertThat(result.get().getFilmName()).isEqualTo("Film");
        assertThat(result.get().getHallDescription()).isEqualTo("Hall Description");
        assertThat(result.get().getPlaceCount()).isEqualTo(10);
    }

    @Test
    void whenFindByIdAndNotFoundThenReturnEmptyOptional() {
        when(filmSessionService.findById(any(Integer.class))).thenReturn(Optional.empty());

        var result = filmSessionService.findById(1);

        assertThat(result).isEmpty();
    }
}