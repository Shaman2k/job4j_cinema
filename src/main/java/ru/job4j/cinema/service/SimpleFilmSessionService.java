package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.FilmSessionRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SimpleFilmSessionService implements FilmSessionService {
    private final FilmSessionRepository filmSessionRepository;
    private final HallService hallService;
    private final FilmService filmService;

    public SimpleFilmSessionService(FilmSessionRepository filmSessionRepository, HallService hallService, FilmService filmService) {
        this.filmSessionRepository = filmSessionRepository;
        this.hallService = hallService;
        this.filmService = filmService;
    }

    @Override
    public Collection<FilmSessionDto> findAll() {
        return filmSessionRepository.findAll().stream()
                .map(this::getFilmSessionDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FilmSessionDto> findById(int id) {
        return filmSessionRepository.findById(id).map(this::getFilmSessionDto);
    }

    private FilmSessionDto getFilmSessionDto(FilmSession filmSession) {
        FilmSessionDto filmSessionDto = new FilmSessionDto();
        filmSessionDto.setId(filmSession.getId());
        filmSessionDto.setStartTime(filmSession.getStartTime());
        filmSessionDto.setEndTime(filmSession.getEndTime());
        filmSessionDto.setPrice(filmSession.getPrice());

        var filmDtoOptional = filmService.findById(filmSession.getFilmId());
        if (filmDtoOptional.isPresent()) {
            FilmDto filmDto = filmDtoOptional.get();
            filmSessionDto.setFileId(filmDto.getFileId());
            filmSessionDto.setFilmName(filmDto.getName());
            filmSessionDto.setDuration(filmDto.getDurationInMinutes());
        }

        var hallOptional = hallService.findById(filmSession.getHallsId());
        if (hallOptional.isPresent()) {
            Hall hall = hallOptional.get();
            filmSessionDto.setRowCount(hall.getRowCount());
            filmSessionDto.setPlaceCount(hall.getPlaceCount());
            filmSessionDto.setHallName(hall.getName());
            filmSessionDto.setHallDescription(hall.getDescription());
        }
        return filmSessionDto;
    }
}
