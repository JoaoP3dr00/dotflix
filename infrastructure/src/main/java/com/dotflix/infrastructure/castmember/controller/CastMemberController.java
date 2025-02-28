package com.dotflix.infrastructure.castmember.controller;

import com.dotflix.application.castmember.*;
import com.dotflix.application.castmember.dto.CreateCastMemberDTO;
import com.dotflix.application.castmember.dto.UpdateCastMemberDTO;
import com.dotflix.application.castmember.exceptions.CastMemberNotFoundException;
import com.dotflix.domain.Pagination;
import com.dotflix.domain.SearchQuery;
import com.dotflix.domain.castmember.CastMember;
import com.dotflix.infrastructure.castmember.controller.dto.CastMemberListResponse;
import com.dotflix.infrastructure.castmember.controller.dto.CreateCastMemberRequest;
import com.dotflix.infrastructure.castmember.controller.dto.UpdateCastMemberRequest;
import com.dotflix.infrastructure.castmember.controller.presenter.CastMemberPresenter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {
    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;
    private final GetAllCastMemberUseCase getAllCastMemberUseCase;

    public CastMemberController(final CreateCastMemberUseCase createCastMemberUseCase, final GetCastMemberByIdUseCase getCastMemberByIdUseCase, final UpdateCastMemberUseCase updateCastMemberUseCase, final DeleteCastMemberUseCase deleteCastMemberUseCase, final GetAllCastMemberUseCase getAllCastMemberUseCase) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.getCastMemberByIdUseCase = Objects.requireNonNull(getCastMemberByIdUseCase);
        this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
        this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
        this.getAllCastMemberUseCase = Objects.requireNonNull(getAllCastMemberUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberRequest input) {
        try {
            final CreateCastMemberDTO aCommand = new CreateCastMemberDTO(input.name(), input.type());

            final CastMember output = this.createCastMemberUseCase.execute(aCommand);

            return ResponseEntity.created(URI.create("/cast_members/" + output.getId())).body(output);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

            return ResponseEntity.badRequest().body(Map.of(
                    "errors", List.of(Map.of("message", e.getMessage()))
            ));
        }
    }

    @Override
    public Pagination<CastMemberListResponse> getAllCastMembers(final String search, final int page, final int perPage, final String sort, final String direction) {
        return this.getAllCastMemberUseCase.execute(new SearchQuery(page, perPage, search, sort, direction)).map(CastMemberPresenter::presentGetAll);
    }

    @Override
    public ResponseEntity<?> getById(final String id) {
        try {
            return ResponseEntity.ok(CastMemberPresenter.present(this.getCastMemberByIdUseCase.execute(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCastMemberRequest aBody) {
        try {
            final UpdateCastMemberDTO aCommand = new UpdateCastMemberDTO(id, aBody.name(), aBody.type());

            final CastMember output = this.updateCastMemberUseCase.execute(aCommand);

            return ResponseEntity.ok(output);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

            return ResponseEntity.badRequest().body(Map.of("errors", List.of(Map.of("message", e.getMessage()))));
        }
    }

    @Override
    public void deleteById(final String id) {
        this.deleteCastMemberUseCase.execute(id);
    }
}