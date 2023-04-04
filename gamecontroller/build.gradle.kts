plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(33)
    defaultConfig {
        minSdkVersion(21)
    }
    buildTypes {
        getByName("release") {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    // base
    implementation(Dependencies.kotlin.kotlin)
    implementation(Dependencies.coroutines)
    implementation(Dependencies.android.appcompat)

}