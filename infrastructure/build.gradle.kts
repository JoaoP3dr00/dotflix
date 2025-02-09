buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:11.3.1")
    }
}

plugins {
    id("java")
    id("application")
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.flywaydb.flyway") version "11.3.1"
}

group = "com.dotflix.infrastructure"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation("org.flywaydb:flyway-core:11.3.1")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.postgresql:postgresql:42.7.5")


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.flywaydb:flyway-core")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:11.3.1")
    runtimeOnly("com.h2database:h2")
}

flyway {
    driver = "org.postgresql.Driver"
    url = "jdbc:postgresql://localhost:5431/dotflix"
    user = "postgres"
    password = "postgres"
    cleanDisabled = false;
}

tasks.test {
    useJUnitPlatform()
}