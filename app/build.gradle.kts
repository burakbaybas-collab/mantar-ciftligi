plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

// İmza anahtarı GitHub sırlarından gelirse kullanılır; yoksa standart debug anahtarı.
val keystoreFile: String? = System.getenv("KEYSTORE_FILE")

android {
    namespace = "com.burakbaybas.mantarciftligi"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.burakbaybas.mantarciftligi"
        minSdk = 26
        targetSdk = 35
        // Her CI derlemesi bir öncekinin üzerine kurulabilsin diye artan sürüm numarası
        versionCode = (System.getenv("GITHUB_RUN_NUMBER") ?: "1").toInt()
        versionName = "1.0." + (System.getenv("GITHUB_RUN_NUMBER") ?: "0")
    }

    signingConfigs {
        if (keystoreFile != null) {
            create("ciftlik") {
                storeFile = file(keystoreFile)
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        debug {
            if (keystoreFile != null) signingConfig = signingConfigs.getByName("ciftlik")
        }
    }

    // Oyun tek dosya: docs/index.html. Web (GitHub Pages) ve Android aynı dosyayı kullanır.
    sourceSets["main"].assets.srcDir(rootProject.file("docs"))

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.webkit)
}
