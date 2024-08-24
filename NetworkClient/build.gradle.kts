plugins {
    `maven-publish`
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.networkclient"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    //Ktor
    api(libs.bundles.ktor.client)

    //Timber
    api(libs.timber)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}



publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/rajputmukesh748/AndroidNetworkClient")
            credentials {
                username = project.findProperty("gpr.user") as String?
                password = project.findProperty("gpr.key") as String?
            }
        }
    }
    publications {
        create<MavenPublication>("release") {
            groupId = "com.rajputmukesh748"
            artifactId = "androidNetworkClient"
            version = "1.0.0"

            afterEvaluate {
                artifact("$buildDir/outputs/aar/NetworkClient-release.aar")
            }

            pom {
                name.set("Android Network Client")
                description.set("A network client library for Android")
                url.set("https://github.com/rajputmukesh748/AndroidNetworkClient")
            }
        }
    }
}
