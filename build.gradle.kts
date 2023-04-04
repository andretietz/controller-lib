buildscript {
  repositories {
    // TODO: remove after new build is published
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    gradlePluginPortal()
  }

  dependencies {
    classpath(kotlin("gradle-plugin", version = Versions.kotlin))
    classpath(Dependencies.gradle.plugin.android)
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
  }
}
