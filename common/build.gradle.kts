plugins {
    java
}

group = "kg.zhaparov"
version = "0.0.1-SNAPSHOT"

description = "payment-system-common"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.5.7"))
    implementation("com.fasterxml.jackson.core:jackson-databind")
}
