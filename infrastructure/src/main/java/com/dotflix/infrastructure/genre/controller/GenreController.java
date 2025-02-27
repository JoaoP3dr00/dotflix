package com.dotflix.infrastructure.genre.controller;

import com.dotflix.application.genre.*;
import com.dotflix.application.genre.dto.CreateGenreDTO;
import com.dotflix.application.genre.dto.UpdateGenreDTO;
import com.dotflix.domain.Pagination;
import com.dotflix.domain.SearchQuery;
import com.dotflix.domain.genre.Genre;
import com.dotflix.infrastructure.genre.controller.dto.CreateGenreRequest;
import com.dotflix.infrastructure.genre.controller.dto.GenreGetAllResponse;
import com.dotflix.infrastructure.genre.controller.dto.UpdateGenreRequest;
import com.dotflix.infrastructure.genre.controller.presenter.GenreApiPresenter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
public class GenreController implements GenreAPI {
    private final CreateGenreUseCase createGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;
    private final GetGenreByIdUseCase getGenreByIdUseCase;
    private final GetAllGenreUseCase getAllGenreUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;

    public GenreController(final CreateGenreUseCase createGenreUseCase, final DeleteGenreUseCase deleteGenreUseCase, final GetGenreByIdUseCase getGenreByIdUseCase, final GetAllGenreUseCase getAllGenreUseCase, final UpdateGenreUseCase updateGenreUseCase) {
        this.createGenreUseCase = createGenreUseCase;
        this.deleteGenreUseCase = deleteGenreUseCase;
        this.getGenreByIdUseCase = getGenreByIdUseCase;
        this.getAllGenreUseCase = getAllGenreUseCase;
        this.updateGenreUseCase = updateGenreUseCase;
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        try {
            final CreateGenreDTO createGenreDTO = new CreateGenreDTO(input.name(), input.isActive(), input.categories());

            final Genre output = this.createGenreUseCase.execute(createGenreDTO);

            return ResponseEntity.created(URI.create("/genres/" + output.getId())).body(output);
        } catch (Exception e){
            System.out.println("Error: " + e);

            return ResponseEntity.badRequest().body(Map.of(
                    "errors", List.of(Map.of("message", e.getMessage()))
            ));
        }
    }

    @Override
    public Pagination<GenreGetAllResponse> list(final String search, final int page, final int perPage, final String sort, final String direction) {
        return this.getAllGenreUseCase.execute(new SearchQuery(page, perPage, search, sort, direction)).map(GenreApiPresenter::presentGetAll);
    }

    @Override
    public ResponseEntity<?> getById(final String id) {
        try {
            return ResponseEntity.ok(GenreApiPresenter.present(this.getGenreByIdUseCase.execute(id)));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateGenreRequest input) {
        try {
            final UpdateGenreDTO aCommand = new UpdateGenreDTO(id, input.name(), input.isActive(), input.categories());

            final Genre output = this.updateGenreUseCase.execute(aCommand);

            return ResponseEntity.ok(output);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

            return ResponseEntity.badRequest().body(Map.of("errors", List.of(Map.of("message", e.getMessage()))));
        }
    }

    @Override
    public void deleteById(final String id) {
        this.deleteGenreUseCase.execute(id);
    }
}