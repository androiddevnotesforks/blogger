plugins {
    id ("com.android.application")
    id ("kotlin-android")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
    id ("com.google.gms.google-services")
}

android {
    compileSdk = 33


    defaultConfig {
        applicationId = "com.peterchege.blogger"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName= "1.0"

        testInstrumentationRunner= "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary= true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility= JavaVersion.VERSION_1_8
        targetCompatibility= JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"

    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
        kotlinCompilerVersion = "1.5.21"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation ("androidx.core:core-ktx:1.9.0")
    implementation ("androidx.appcompat:appcompat:1.5.1")
    implementation ("com.google.android.material:material:1.7.0")
    implementation ("androidx.compose.ui:ui:1.4.0-alpha03")
    implementation ("androidx.compose.material:material:1.4.0-alpha03")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.4.0-alpha03")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation ("androidx.activity:activity-compose:1.6.1")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.4")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.4.0-alpha03")
    debugImplementation ("androidx.compose.ui:ui-tooling:1.4.0-alpha03")



    // retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    // view model
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")

    //coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // dagger hilt
    implementation ("com.google.dagger:hilt-android:2.44.2")
    kapt ("com.google.dagger:hilt-android-compiler:2.44.2")
//    implementation "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
    kapt ("androidx.hilt:hilt-compiler:1.0.0")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation ("androidx.navigation:navigation-compose:2.5.3")
    // coil
    implementation ("io.coil-kt:coil-compose:2.2.2")

    //fcm
    implementation ("com.google.firebase:firebase-messaging:23.1.1")

    // room 
    implementation ("androidx.room:room-runtime:2.4.3")
    kapt ("androidx.room:room-compiler:2.4.3")

    // Kotlin Extensions and Coroutines support for Room
    implementation ("androidx.room:room-ktx:2.4.3")

//    implementation "androidx.compose.runtime:runtime-livedata:1.3.0-alpha03"
    implementation ("androidx.compose.material:material-icons-extended:1.4.0-alpha03")

    //glide
    implementation ("dev.chrisbanes.accompanist:accompanist-glide:0.5.1")

    //pager
    implementation ("com.google.accompanist:accompanist-pager:0.24.3-alpha")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.24.3-alpha")
    // swipe refresh
    implementation ("com.google.accompanist:accompanist-swiperefresh:0.24.2-alpha")
    // landscapist
    implementation ("com.github.skydoves:landscapist-glide:1.4.8")

    implementation ("com.google.accompanist:accompanist-flowlayout:0.28.0")

    implementation ("com.github.skydoves:landscapist-glide:1.4.8")


    implementation("androidx.work:work-runtime-ktx:2.7.1")
}

