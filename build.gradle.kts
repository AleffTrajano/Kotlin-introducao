plugins {
    kotlin("jvm") version "2.2.0"
    application
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("br.com.curso.kotlinintroducao.MainKt")
}

tasks.named<org.gradle.api.tasks.JavaExec>("run") {
    standardInput = System.`in`
}

