package com.dotflix.infrastructure;

import com.dotflix.infrastructure.castmember.persistence.CastMemberRepository;
import com.dotflix.infrastructure.category.persistence.CategoryRepository;
import com.dotflix.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;

public class PostgreSQLCleanUpExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(final ExtensionContext context) {
        final var appContext = SpringExtension.getApplicationContext(context);

        List.of(
                appContext.getBean(GenreRepository.class),
                appContext.getBean(CategoryRepository.class),
                appContext.getBean(CastMemberRepository.class)
        ).forEach(CrudRepository::deleteAll);
    }
}