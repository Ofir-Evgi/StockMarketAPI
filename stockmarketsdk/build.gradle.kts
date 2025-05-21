plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "com.example.stockmarketsdk"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}




dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.volley)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation(libs.mpandroidchart)
}


afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.github.Ofir-Evgi"
                artifactId = "stockmarketsdk"
                version = "1.0.0"

                artifact(tasks.getByName("bundleReleaseAar"))

                pom {
                    name.set("Stock Market SDK")
                    description.set("A stock market library for Android.")
                    url.set("https://github.com/Ofir-Evgi/StockMarketAPI")

                    withXml {
                        val dependenciesNode = asNode().appendNode("dependencies")
                        configurations.api.get().dependencies.forEach { dependency ->
                            val depNode = dependenciesNode.appendNode("dependency")
                            depNode.appendNode("groupId", dependency.group)
                            depNode.appendNode("artifactId", dependency.name)
                            depNode.appendNode("version", dependency.version)
                            depNode.appendNode("scope", "compile")
                        }
                        configurations.implementation.get().dependencies.forEach { dependency ->
                            val depNode = dependenciesNode.appendNode("dependency")
                            depNode.appendNode("groupId", dependency.group)
                            depNode.appendNode("artifactId", dependency.name)
                            depNode.appendNode("version", dependency.version)
                            depNode.appendNode("scope", "runtime")
                        }
                    }
                }
            }
        }
    }
}
