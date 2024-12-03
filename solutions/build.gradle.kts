import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
   kotlin("jvm")
}

group = "de.bdudelsack"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation("com.github.ajalt.mordant:mordant:3.0.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
