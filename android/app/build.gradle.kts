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

    // ✅ **Correção: Excluindo arquivos duplicados do META-INF**
    packaging {
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/LICENSE")
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/license.txt")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add("META-INF/NOTICE.txt")
        resources.excludes.add("META-INF/notice.txt")
        resources.excludes.add("META-INF/INDEX.LIST") // 🔴 **Adicionando essa linha**
    }
}

dependencies {
    implementation("com.google.auth:google-auth-library-oauth2-http:1.18.0")

    implementation("com.google.protobuf:protobuf-java:3.21.7")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    implementation("org.json:json:20210307")
    implementation("com.google.code.gson:gson:2.8.9")

    implementation(platform("com.google.cloud:libraries-bom:26.29.0"))
    implementation("com.google.cloud:google-cloud-texttospeech:2.33.0")
}
