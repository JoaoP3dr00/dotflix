package com.dotflix.application.category;

import com.dotflix.application.category.dto.CreateCategoryDTO;
import com.dotflix.domain.category.Category;
import com.dotflix.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {
    // 1. Happy way test
    // 2. Invalid property test (name)
    // 3. Create inactive category test
    // 4. Teste simulando um erro genérico vindo do gateway

    @Test
    public void createCategoryUseCaseTest(){
        final String expectedName = "Filmes";
        final String expectedDescription = "Description";
        final boolean expectedIsActive = true;

        final CreateCategoryDTO categoryDTO = new CreateCategoryDTO(expectedName, expectedDescription, expectedIsActive);

        final CategoryGateway categoryGateway = Mockito.mock(CategoryGateway.class);

        Mockito.when(categoryGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final CreateCategoryUseCase useCase = new CreateCategoryUseCase(categoryGateway);

        final Category actualOutput = useCase.execute(categoryDTO);

        Assertions.assertNotNull(actualOutput);

        Mockito.verify(categoryGateway, Mockito.times(1)).create(Mockito.argThat(category -> {
            return Objects.equals(expectedName, category.getName())
                    && Objects.equals(expectedDescription, category.getDescription())
                    && Objects.equals(expectedIsActive, category.getIsActive())
                    && Objects.nonNull(category.getId())
                    && Objects.nonNull(category.getCreatedAt())
                    && Objects.nonNull(category.getUpdatedAt())
                    && Objects.isNull(category.getDeletedAt());
        }));
    }

    @Test
    public void createCategoryUseCaseWithInvalidUsernameTest(){
        // Vídeo 2072 tem outros testes e aprofundamento no mockito
    }
}
