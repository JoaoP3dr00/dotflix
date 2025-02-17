package com.dotflix.infrastructure.category.persistence;

import com.dotflix.domain.category.Category;
import com.dotflix.domain.category.CategoryGateway;
import com.dotflix.domain.category.CategorySearchQuery;
import com.dotflix.domain.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CategoryPostgresqlGateway implements CategoryGateway {
    private final CategoryRepository repository;

    public CategoryPostgresqlGateway(final CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category create(final Category category) {
        return this.repository.save(CategoryEntity.fromDomain(category)).toDomain();
    }

    @Override
    public void deleteById(String id) {
        if (this.repository.existsById(id))
            this.repository.deleteById(id);

    }

    @Override
    public Optional<Category> findById(String id) {
        return this.repository.findById(id).map(CategoryEntity::toDomain);
    }

    @Override
    public Category update(Category category) {
        return create(category);
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery aQuery) {
        // Cria o objeto Pageable do Spring usando CategorySearchQuery do Domain para manter o desacoplamento
        final Pageable page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        // Busca dinamica pelo criterio terms (name ou description)
        final Specification<CategoryEntity> specifications = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())                                                                                           // Verifica se os termos estão vazios
                .map((final String str) -> {                                                                                                    // Se não estiverem, junta eles
                    final String convertLike = "%" + str + "%";                                                                                 // Adiciona '%' para virar uma expressão LIKE

                    final Specification<CategoryEntity> nameLike = (root, query, cb) ->    // instância direta de Specification, onde root: Entidade, query: Consulta SQL, cb: CriteriaBuilder que cria condições dinâmicas
                            cb.like(                                                                                                            // Aplica a condição LIKE na consulta
                                cb.upper(                                                                                                       // Converte para maiúsculo
                                    root.get("name")                                                                                            // Obtém o campo da entidade, como "name"
                                ),
                                convertLike.toUpperCase()                                                                                       // Converte para maiúsculo
                            );

                    final Specification<CategoryEntity> descriptionLike = (root, query, cb) ->
                            cb.like(
                                cb.upper(
                                    root.get("description")
                                ),
                                convertLike.toUpperCase()
                            );

                    return nameLike.or(descriptionLike);                                                                                        // Utiliza a API de Specification para chamar o método or e juntar os termos
                })
                .orElse(null);  // Se estiverem, faz nada

        final Page<CategoryEntity> pageResult = this.repository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryEntity::toDomain).toList()
        );
    }
}
