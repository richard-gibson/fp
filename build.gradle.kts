import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.10"
}

group = "co.instil"
version = "1.0-SNAPSHOT"

val arrow_version = "0.8.2-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://oss.jfrog.org/oss-snapshot-local/")
}

dependencies {
    compile(kotlin("stdlib-jdk8"))

    compile(group = "io.arrow-kt", name = "arrow-core", version = arrow_version)
    compile(group = "io.arrow-kt", name = "arrow-syntax", version = arrow_version)
    compile(group = "io.arrow-kt", name = "arrow-typeclasses", version = arrow_version)
    compile(group = "io.arrow-kt", name = "arrow-data", version = arrow_version)
    compile(group = "io.arrow-kt", name = "arrow-query-language", version = arrow_version)
    compile(group = "io.arrow-kt", name = "arrow-instances-core", version = arrow_version)
    compile(group = "io.arrow-kt", name = "arrow-instances-data", version = arrow_version)
    kapt(group = "io.arrow-kt", name = "arrow-annotations-processor", version = arrow_version)

    compile("io.arrow-kt:arrow-free:$arrow_version")
    compile("io.arrow-kt:arrow-mtl:$arrow_version")
    compile("io.arrow-kt:arrow-effects:$arrow_version")
    compile("io.arrow-kt:arrow-effects-instances:$arrow_version")
    compile("io.arrow-kt:arrow-effects-rx2:$arrow_version")
    compile("io.arrow-kt:arrow-effects-rx2-instances:$arrow_version")
    compile("io.arrow-kt:arrow-effects-reactor:$arrow_version")
    compile("io.arrow-kt:arrow-effects-reactor-instances:$arrow_version")
    compile("io.arrow-kt:arrow-effects-kotlinx-coroutines:$arrow_version")
    compile("io.arrow-kt:arrow-effects-kotlinx-coroutines-instances:$arrow_version")
    compile("io.arrow-kt:arrow-optics:$arrow_version")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}