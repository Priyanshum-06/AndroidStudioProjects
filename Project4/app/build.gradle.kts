//plugins {
//    id("com.google.gms.google-services")
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.google.gms.google.services)
//}

plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // Only this one plugin block
}

android {
    namespace = "com.example.project4"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.project4"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.firebase.auth)
    implementation (libs.firebase.auth)
    implementation ("com.google.android.gms:play-services-auth:21.3.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}