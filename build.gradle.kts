buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.androidGradle)
        classpath(libs.kotlin.gradle)
        classpath(libs.hiltGradle)
        classpath(libs.navigation.safeargs)
        classpath(libs.google.servises)
    }
}

tasks {
    register("clean", Delete::class) {
        delete(buildDir)
        delete(File(buildDir.parent, "build-cache"))
    }
}