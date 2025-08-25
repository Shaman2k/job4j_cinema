package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.repository.FileRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleFileServiceTest {
    private FileRepository fileRepository;
    private FileService fileService;

    @BeforeEach
    public void init() {
        fileRepository = mock(FileRepository.class);
        fileService = new SimpleFileService(fileRepository);
    }

    @Test
    public void whenGetByIdThenReturnOptionalFileWithContent() throws IOException {
        byte[] content = {1, 2, 3};
        Path tmpFile = Files.createTempFile("test", "file");
        Files.write(tmpFile, content);
        File file = new File("test.file", tmpFile.toAbsolutePath().toString());
        when(fileRepository.findById(1)).thenReturn(Optional.of(file));

        var result = fileService.getFileById(1);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getName()).isEqualTo("test.file");
        assertThat(result.get().getContent()).isEqualTo(content);

    }

    @Test
    public void whenFileNotExistThenReturnEmptyOptional() {
        when(fileRepository.findById(1)).thenReturn(Optional.empty());
        var result = fileService.getFileById(1);
        assertThat(result).isEmpty();
    }
}