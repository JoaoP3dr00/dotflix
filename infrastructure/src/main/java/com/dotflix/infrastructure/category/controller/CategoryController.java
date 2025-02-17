package com.dotflix.infrastructure.category.controller;

import com.dotflix.application.category.*;
import com.dotflix.application.category.dto.CreateCategoryDTO;
import com.dotflix.application.category.dto.DeleteCategoryDTO;
import com.dotflix.application.category.dto.GetCategoryByIdDTO;
import com.dotflix.application.category.dto.UpdateCategoryDTO;
import com.dotflix.application.category.exceptions.CategoryNotFoundException;
import com.dotflix.domain.Pagination;
import com.dotflix.domain.category.Category;
import com.dotflix.domain.category.CategorySearchQuery;
import com.dotflix.infrastructure.category.controller.dto.CategoryGetAllResponse;
import com.dotflix.infrastructure.category.controller.dto.CategoryResponse;
import com.dotflix.infrastructure.category.controller.dto.CreateCategoryRequest;
import com.dotflix.infrastructure.category.controller.dto.UpdateCategoryRequest;
import com.dotflix.infrastructure.category.controller.presenter.CategoryApiPresenter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final GetAllCategoriesUseCase getAllCategoriesUseCase;

    public CategoryController(final CreateCategoryUseCase createCategoryUseCase, final GetCategoryByIdUseCase getCategoryByIdUseCase, final UpdateCategoryUseCase updateCategoryUseCase, final DeleteCategoryUseCase deleteCategoryUseCase, final GetAllCategoriesUseCase getAllCategoriesUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.getAllCategoriesUseCase = Objects.requireNonNull(getAllCategoriesUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryRequest request) {
        final CreateCategoryDTO createCategoryDTO = new CreateCategoryDTO(request.name(), request.description(), request.active() != null ? request.active() : true);

        try {
            final Category category = this.createCategoryUseCase.execute(createCategoryDTO);

            return ResponseEntity.created(URI.create("/categories/" + category.getId())).body(category);
        } catch (Exception e){
            System.out.println("Error: " + e);

            return ResponseEntity.unprocessableEntity().body(Map.of("Erro: ", e));
        }
    }

    @Override
    public Pagination<CategoryGetAllResponse> getAllCategories(final String search, final int page, final int perPage, final String sort, final String direction) {
        return getAllCategoriesUseCase.execute(new CategorySearchQuery(page, perPage, search, sort, direction)).map(CategoryApiPresenter::presentGetAll);
    }

    @Override
    public ResponseEntity<?> getById(final String id) {
        final GetCategoryByIdDTO getCategoryByIdDTO = new GetCategoryByIdDTO(id);

        try{
            final CategoryResponse response = CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(getCategoryByIdDTO));

            return ResponseEntity.ok(response);
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada: " + e);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID Inválido: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryRequest request) {
        final UpdateCategoryDTO updateCategoryDTO = new UpdateCategoryDTO(id, request.name(), request.description(), request.active() != null ? request.active() : true);

        try {
            final Category category = this.updateCategoryUseCase.execute(updateCategoryDTO);

            return ResponseEntity.ok().body(category);
        } catch (Exception e){
            System.out.println("Erro: " + e);

            return ResponseEntity.unprocessableEntity().body(Map.of("Erro: ", e));
        }
    }

    @Override
    public void deleteById(final String id) {
        final DeleteCategoryDTO deleteCategoryDTO = new DeleteCategoryDTO(id);

        this.deleteCategoryUseCase.execute(deleteCategoryDTO);
    }
}