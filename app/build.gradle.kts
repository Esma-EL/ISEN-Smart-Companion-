plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    id("kotlin-parcelize")
    id ("kotlin-kapt")
}

android {
    namespace = "fr.isen.elakrimi.isensmartcompanion"
    compileSdk = 35

    defaultConfig {
        applicationId = "fr.isen.elakrimi.isensmartcompanion"
        minSdk = 25
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    kapt("androidx.room:room-compiler:2.6.1") // ✅ Vérifie cette ligne
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Convertisseur Gson
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.ai.client.generativeai:generativeai:0.6.0") // 🔥 SDK Gemini AI
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("androidx.navigation:navigation-compose:2.4.0")
    implementation ("androidx.room:room-runtime:2.4.3")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation ("androidx.room:room-runtime:2.5.0") // Assure-toi que cette version correspond à celle que tu utilises
    implementation ("androidx.room:room-ktx:2.5.0") // Ajoute cette ligne pour la prise en charge des Coroutines
    implementation ("com.google.firebase:firebase-database:20.0.3")
    implementation ("com.google.firebase:firebase-auth:21.0.3")

    // Coroutine support for Room (si tu utilises les coroutines)
    //implementation ("androidx.room:room-coroutines:2.3.0")
}