package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.HallRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleHallServiceTest {
    private HallRepository hallRepository;
    private HallService hallService;

    @BeforeEach
    public void init() {
        hallRepository = mock(HallRepository.class);
        hallService = new SimpleHallService(hallRepository);
    }

    @Test
    public void whenFindByIdAndSuccessThenReturnHallOptional() {
        Hall hall = new Hall("Hall", 10, 10, "Hall Description");
        when(hallRepository.findById(1)).thenReturn(Optional.of(hall));

        var optionalHall = hallService.findById(1);

        assertThat(optionalHall).isPresent();
        assertThat(optionalHall.get().getName()).isEqualTo("Hall");
    }

    @Test
    public void whenFindByIdAndNotFoundThenReturnEmptyOptional() {
        when(hallRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        var optionalHall = hallService.findById(1);

        assertThat(optionalHall).isEmpty();
    }
}