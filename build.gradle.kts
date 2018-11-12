import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.0"
}

group = "co.instil"
version = "1.0-SNAPSHOT"

val arrow_version = "0.8.1"

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))


    compile("io.arrow-kt:arrow-core:$arrow_version")
    compile("io.arrow-kt:arrow-syntax:$arrow_version")
    compile("io.arrow-kt:arrow-typeclasses:$arrow_version")
    compile("io.arrow-kt:arrow-data:$arrow_version")
    compile("io.arrow-kt:arrow-query-language:$arrow_version")
    compile("io.arrow-kt:arrow-instances-core:$arrow_version")
    compile("io.arrow-kt:arrow-instances-data:$arrow_version")
    kapt("io.arrow-kt:arrow-annotations-processor:$arrow_version")

    compile("io.arrow-kt:arrow-free:$arrow_version")
    compile("io.arrow-kt:arrow-mtl:$arrow_version")
    compile("io.arrow-kt:arrow-effects:$arrow_version")
    compile("io.arrow-kt:arrow-effects-instances:$arrow_version")
    compile("io.arrow-kt:arrow-effects-rx2:$arrow_version")
    compile("io.arrow-kt:arrow-effects-reactor:$arrow_version")
    compile("io.arrow-kt:arrow-effects-kotlinx-coroutines:$arrow_version")
    compile("io.arrow-kt:arrow-optics:$arrow_version")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}