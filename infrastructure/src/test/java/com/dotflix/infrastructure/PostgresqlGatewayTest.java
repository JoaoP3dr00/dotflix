package com.dotflix.infrastructure;

import com.dotflix.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ComponentScan(
        basePackages = "com.dotflix.infrastructure.category",
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[PostgresqlGateway]")   // Inicia todas as classes que terminam com PostgresqlGateway além das classes do DataJpaTest
        }
)
@SpringBootTest(classes = WebServerConfig.class)
@ActiveProfiles("test")
@ExtendWith(PostgreSQLCleanUpExtension.class)
//@Tag("integrationTest")
public @interface PostgresqlGatewayTest {
}