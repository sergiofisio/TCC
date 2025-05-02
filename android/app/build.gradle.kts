plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.emotionharmony"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.emotionharmony"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 35
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

    packaging {
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/LICENSE")
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/license.txt")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add("META-INF/NOTICE.txt")
        resources.excludes.add("META-INF/notice.txt")
        resources.excludes.add("META-INF/INDEX.LIST")
    }
}

dependencies {
    implementation(libs.google.auth.library.oauth2.http)
    implementation(libs.play.services.location)

    implementation(libs.protobuf.java)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.okhttp)

    implementation(libs.json)
    implementation(libs.gson)

    implementation(libs.libraries.bom)
    implementation(libs.google.cloud.texttospeech)
}
