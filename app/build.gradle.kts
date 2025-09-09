plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.pruden.habits"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.pruden.habits"
        minSdk = 24
        targetSdk = 35
        versionCode = 5
        versionName = "1.0 Oficial"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.gson)
    implementation(libs.androidx.activity)
    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.media3.common.ktx)
    ksp(libs.androidx.room.compiler)

    //ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //Glide
    implementation(libs.glide)

    // Corrutinas
    implementation(libs.kotlinx.coroutines.android)

    implementation ("com.github.rubensousa:gravitysnaphelper:2.2.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // https://github.com/PhilJay/MPAndroidChart?tab=readme-ov-file

    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")


    implementation ("me.relex:circleindicator:2.1.6")

}