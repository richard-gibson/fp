import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.10"
    idea
    id("org.jetbrains.kotlin.kapt") version "1.3.10"
}

group = "co.instil"
version = "1.0-SNAPSHOT"

val arrowVersion = "0.8.2-SNAPSHOT"
//val arrowVersion = "0.8.1"


val kotlintestVersion = "2.0.7"

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://oss.jfrog.org/oss-snapshot-local/")
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))

    compile(group = "io.arrow-kt", name = "arrow-core",                                 version = arrowVersion)
    compile(group = "io.arrow-kt", name = "arrow-syntax",                               version = arrowVersion)
    compile(group = "io.arrow-kt", name = "arrow-typeclasses",                          version = arrowVersion)
    compile(group = "io.arrow-kt", name = "arrow-data",                                 version = arrowVersion)
    compile(group = "io.arrow-kt", name = "arrow-query-language",                       version = arrowVersion)
    compile(group = "io.arrow-kt", name = "arrow-instances-core",                       version = arrowVersion)
    compile(group = "io.arrow-kt", name = "arrow-instances-data",                       version = arrowVersion)

    compile(group = "io.arrow-kt", name = "arrow-free",                                 version = arrowVersion)
    compile(group = "io.arrow-kt", name = "arrow-mtl",                                  version = arrowVersion)
    compile(group = "io.arrow-kt", name = "arrow-effects",                              version = arrowVersion)
    compile(group = "io.arrow-kt", name = "arrow-effects-instances",                    version = arrowVersion)
    compile(group = "io.arrow-kt", name = "arrow-effects-rx2",                          version = arrowVersion)
    compile(group = "io.arrow-kt", name = "arrow-effects-rx2-instances",                version = arrowVersion)
    compile(group = "io.arrow-kt", name = "arrow-effects-reactor",                      version = arrowVersion)
    compile(group = "io.arrow-kt", name = "arrow-effects-reactor-instances",            version = arrowVersion)
    compile(group = "io.arrow-kt", name = "arrow-effects-kotlinx-coroutines",           version = arrowVersion)
    compile(group = "io.arrow-kt", name = "arrow-effects-kotlinx-coroutines-instances", version = arrowVersion)
    compile(group = "io.arrow-kt", name = "arrow-optics",                               version = arrowVersion)

    kapt(group = "io.arrow-kt",    name = "arrow-annotations-processor",                version = arrowVersion)

    testCompile(group = "io.arrow-kt",   name = "arrow-test",                           version = arrowVersion)
    testCompile(group = "io.kotlintest", name = "kotlintest",                           version = kotlintestVersion)

}

idea {
    module {
        sourceDirs + files(
                "build/generated/source/kapt/main",
                "build/generated/source/kapt/debug",
                "build/generated/source/kapt/release",
                "build/generated/source/kaptKotlin/main",
                "build/generated/source/kaptKotlin/debug",
                "build/generated/source/kaptKotlin/release",
                "build/tmp/kapt/main/kotlinGenerated"
        )
        generatedSourceDirs + files(
                "build/generated/source/kapt/main",
                "build/generated/source/kapt/debug",
                "build/generated/source/kapt/release",
                "build/generated/source/kaptKotlin/main",
                "build/generated/source/kaptKotlin/debug",
                "build/generated/source/kaptKotlin/release",
                "build/tmp/kapt/main/kotlinGenerated"
        )
    }
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
