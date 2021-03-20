
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.10"
    id("org.jetbrains.kotlin.kapt") version "1.4.10"
    id ("groovy")
    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.10"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("io.micronaut.application") version "1.3.4"
    id("org.flywaydb.flyway") version "7.0.4"
}

version = "0.1"
group = "pl.pollub.bsi"

val kotlinVersion = project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
}

micronaut {
    runtime("netty")
    testRuntime ("spock2")
    processing {
        incremental(true)
        annotations("pl.pollub.bsi.*")
    }
}

dependencies {
    kapt("io.micronaut.security:micronaut-security-annotations")
    implementation("io.micronaut:micronaut-validation")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut:micronaut-runtime")
    implementation("javax.annotation:javax.annotation-api")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.security:micronaut-security")
    implementation("io.micronaut.security:micronaut-security-jwt")
    implementation("io.micronaut.kotlin:micronaut-kotlin-extension-functions")
    implementation("io.micronaut.sql:micronaut-jdbi")
    implementation("org.apache.logging.log4j:log4j-core:2.12.1")
    runtimeOnly("org.apache.logging.log4j:log4j-api:2.12.1")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.12.1")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("org.postgresql:postgresql")
    implementation("io.vavr:vavr:0.10.3")
    implementation("io.vavr:vavr-jackson:0.10.3")
    implementation("io.vavr:vavr-kotlin:0.10.2")
    implementation("org.jdbi:jdbi3-vavr:3.18.0")
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:3.7.1")
    implementation("io.micronaut.flyway:micronaut-flyway")
    annotationProcessor("io.micronaut:micronaut-inject-java")
    testImplementation("org.spockframework:spock-core")
    testImplementation("org.testcontainers:spock")
    testImplementation ("org.testcontainers:postgresql:1.15.2")
    testImplementation("org.testcontainers:testcontainers")
    testRuntimeOnly("com.h2database:h2")
}


application {
    mainClass.set("pl.pollub.bsi.ApplicationKt")
}

java {
    sourceCompatibility = JavaVersion.toVersion("11")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}
