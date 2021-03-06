// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    // val kotlin_version by extra("1.4.30")
    repositories {
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-alpha08")
        classpath(kotlin("gradle-plugin", version = "1.4.30"))

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}