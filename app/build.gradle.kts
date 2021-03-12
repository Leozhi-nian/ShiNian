plugins {
    id("com.android.application")
    kotlin("android")
    // id("kotlin-android")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    defaultConfig {
        applicationId = "com.leozhi.shinian"
        minSdkVersion(23)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        register("release") {
            keyAlias = "leozhi"
            keyPassword = "19980819"
            storeFile = file("file:///home/leozhi/Leozhi/Code/Android/KeyStore/aivoice.jks")
            storePassword = "19980819"
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    android.applicationVariants.all {
        val buildType: String = this.buildType.name
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                this.outputFileName = "拾念_V${defaultConfig.versionName}_$buildType.apk"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.activity:activity:1.3.0-alpha02")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.fragment:fragment:1.3.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.android.material:material:1.2.1")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.2")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.3.0-rc01")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.0-rc01")
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0")
    // Koin
    implementation("org.koin:koin-core:2.2.2")
    implementation("org.koin:koin-androidx-viewmodel:2.2.2")
    // 协程
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2")

    implementation("com.hi-dhl:binding:1.0.7")
    implementation(project(mapOf("path" to ":common")))
    implementation("androidx.recyclerview:recyclerview:1.2.0-beta01")
}