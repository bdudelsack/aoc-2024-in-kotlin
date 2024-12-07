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

tasks.register<Copy>("initDay") {
    var day = "00"

    if(project.hasProperty("day")) {
        day = project.property("day").toString()
    }

    println("Project dir: $projectDir")

    from(fileTree("${projectDir}/template/").files)

    into("${projectDir}/src/main/kotlin/day${day}/")
    rename("DayXX.kt.tmpl", "Day${day}.kt")

    expand("day" to day)
}
