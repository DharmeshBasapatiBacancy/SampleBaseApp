import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-kapt")
}

android {
    namespace = "com.bacancy.samplebaseapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bacancy.samplebaseapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "STRIPE_PUBLISHABLE_KEY", "\"${getTestStripeKey()}\"")
            buildConfigField("String", "STRIPE_ACCOUNT_ID", "\"${getTestStripeAccountId()}\"")
        }
        release {
            buildConfigField("String", "STRIPE_PUBLISHABLE_KEY", "\"${getLiveStripeKey()}\"")
            buildConfigField("String", "STRIPE_ACCOUNT_ID", "\"${getLiveStripeAccountId()}\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro" 
            )
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
        buildConfig = true
    }
}

fun getTestStripeKey(): String {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    return properties.getProperty("stripe.test.publishable.key") ?: ""
}

fun getLiveStripeKey(): String {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    return properties.getProperty("stripe.live.publishable.key") ?: ""
}

fun getTestStripeAccountId(): String {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    return properties.getProperty("stripe.test.account.id") ?: ""
}

fun getLiveStripeAccountId(): String {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    return properties.getProperty("stripe.live.account.id") ?: ""
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.8.0")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.lifecycle:lifecycle-process:2.8.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("androidx.biometric:biometric:1.1.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("io.coil-kt:coil:2.6.0")
    val koinVersion = "3.4.0"
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-android:$koinVersion")
    val retrofitVersion = "2.11.0"
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    implementation("com.stripe:stripe-android:21.2.1")
    implementation("com.google.android.gms:play-services-wallet:19.4.0")
}