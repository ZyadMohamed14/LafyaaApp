plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-kapt") // Apply the Kotlin Kapt plugin
    id("kotlin-parcelize")
    id("com.google.protobuf") version "0.9.4" apply true
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
   //id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.ecommerceapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ecommerceapp"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField(
                "String",
                "clientServerId",
                "\"896087317228-cettgh6nplpo8cmlmqq1hv09a56h09jg.apps.googleusercontent.com\""
            )

        }
        /*
        buildTypes {
        //...
            buildTypes.each {
                it.buildConfigField 'String', 'APP_KEY_1', AppKey
                it.resValue 'string', 'APP_KEY_2', AppKey
            }
        }
         */
        forEach {
            it.buildConfigField(
                "String",
                "clientServerId",
                "\"896087317228-cettgh6nplpo8cmlmqq1hv09a56h09jg.apps.googleusercontent.com\""
            )
            it.resValue(
                "string",
                "facebook_app_id",
                "\"455875720438484\""
            )
            it.resValue(
                "string",
                "fb_login_protocol_scheme",
                "\"fb455875720438484\""
            )
            it.resValue(
                "string",
                "facebook_client_token",
                "\"4555735fef5d5b1640fa730f20e4090e\""
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
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {


    implementation ("com.stripe:stripe-android:20.48.6")
    implementation ("com.google.firebase:firebase-dynamic-links-ktx:21.1.0")
    implementation("androidx.activity:activity:1.9.3")
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
   // annotationProcessor("androidx.room:room-compiler:$room_version")
    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
    implementation ("androidx.room:room-ktx:$room_version")

   implementation("androidx.compose.ui:ui-graphics")
   implementation("com.valentinilk.shimmer:compose-shimmer:1.0.3")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation ("com.google.accompanist:accompanist-placeholder-material:0.36.0")


    //compose

    val composeBom = platform("androidx.compose:compose-bom:2023.10.01")
    implementation(composeBom)

    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.activity:activity-compose:1.9.1")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    implementation("androidx.compose.runtime:runtime-livedata")


    implementation("com.google.accompanist:accompanist-pager:0.32.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.32.0")
    implementation("io.coil-kt:coil:2.6.0")
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("org.jetbrains.compose:compose-gradle-plugin:1.6.11")
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.navigation:navigation-compose:2.8.4")
    implementation("com.google.code.gson:gson:2.8.8")
    // splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")
  //animation
    implementation("com.airbnb.android:lottie-compose:6.0.0")

    // third party libraries
    implementation("com.github.pwittchen:reactivenetwork-rx2:3.0.8")
    implementation("com.squareup.retrofit2:retrofit:2.11.0'")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")

    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    implementation("com.facebook.shimmer:shimmer:0.5.0")


    //Android Sdk
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
   implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("com.facebook.android:facebook-login:16.0.0")
  //  implementation("com.google.firebase:firebase-auth:22.3.1")



    //Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

kapt {
    correctErrorTypes = true
}
