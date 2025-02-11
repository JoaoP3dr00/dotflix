plugins {
    id("java")
}

group = "com.dotflix.application"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    // https://mvnrepository.com/artifact/org.mockito/mockito-junit-jupiter
    //testImplementation("org.mockito:mockito-junit-jupiter:5.0.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.15.2")
    //testImplementation("org.mockito:mockito-all:1.10.19")
    //testImplementation("org.mockito:mockito-core:5.15.2")
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}