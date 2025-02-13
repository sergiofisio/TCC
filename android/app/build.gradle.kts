plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.emotionharmony"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.emotionharmony"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    signingConfigs {
        create("release") {
            storeFile = file("android")
            storePassword = "123456"
            keyAlias = "emotion"
            keyPassword = "123456"
        }
    }

    // 🚀 Garante que os arquivos em assets/ sejam incluídos no APK
    sourceSets {
        named("main") {
            assets.srcDirs("src/main/assets")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    implementation("org.json:json:20210307")
}
