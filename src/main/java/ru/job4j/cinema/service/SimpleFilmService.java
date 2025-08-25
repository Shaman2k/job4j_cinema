package ru.job4j.cinema.service;

import org.springframework.stereotype.Controller;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class SimpleFilmService implements FilmService {
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;

    public SimpleFilmService(FilmRepository filmRepository, GenreRepository genreRepository) {
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Collection<FilmDto> findAll() {
        var films = filmRepository.findAll();

        return films.stream()
                .map(this::getFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FilmDto> findById(int id) {
        Optional<Film> optionalFilm = filmRepository.findById(id);
        if (optionalFilm.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(getFilmDto(optionalFilm.get()));
    }

    private FilmDto getFilmDto(Film film) {
        var genre = genreRepository.getById(film.getGenreId());
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setYear(film.getYear());
        filmDto.setMinimalAge(film.getMinimalAge());
        filmDto.setDurationInMinutes(film.getDurationInMinutes());
        filmDto.setFileId(film.getFileId());
        filmDto.setGenre(genre.orElseGet(Genre::new).getName());

        return filmDto;
    }
}
