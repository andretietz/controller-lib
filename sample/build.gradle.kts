plugins {
  id("com.android.application")
  kotlin("android")
}

android {
  compileSdkVersion(33)
  defaultConfig {
    applicationId = "com.andretietz.controllerlib.demo"
    minSdkVersion(21)
    versionCode = 1
    versionName = "1.1.1"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  buildFeatures {
    viewBinding = true
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}

dependencies {

  implementation(project(":gamecontroller"))

  // base
  implementation(Dependencies.kotlin.kotlin)
  implementation(Dependencies.coroutines)
  implementation(Dependencies.android.appcompat)
//  implementation(Dependencies.android.fragment)
//  implementation(Dependencies.android.constraintLayout)
//  implementation(Dependencies.android.recyclerView)
  // android lifecycle
  implementation(Dependencies.android.lifecycle.runtime)
//  implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:${Versions.lifecycle}")
  // logging
  implementation(Dependencies.android.timber)

  // di
//  implementation(Dependencies.android.hilt)
//  implementation("com.google.dagger:dagger-android:${Versions.dagger}")

  // codegen

//  kapt(Dependencies.android.hiltCodegen)
//  kapt("com.google.dagger:dagger-android-processor:${Versions.dagger}")
//  kapt(Dependencies.android.)

  // test
  testImplementation(Dependencies.test.junit)
  testImplementation(Dependencies.test.assertj)
}
//
//kapt {
//  correctErrorTypes = true
//}
