package com.dotflix.infrastructure;

import com.dotflix.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@SpringBootTest(classes = WebServerConfig.class)
@ExtendWith(PostgreSQLCleanUpExtension.class)
@Tag("integrationTest")
public @interface IntegrationTest {
}
