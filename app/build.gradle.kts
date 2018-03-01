plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion(26)
    buildToolsVersion("27.0.3")

    defaultConfig {
        minSdkVersion(19)
        targetSdkVersion(26)

        versionCode = 1
        versionName = "1.0"
        applicationId = "top.tented.plugindemo"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles("proguard-rules.pro")
        }
    }
}

val ankoVersion = "0.10.4"
val hoshinoVersion = "1.0.5"
val kotlinVersion = "1.2.30"

fun DependencyHandlerScope.android(name : String, version : String) =
        "com.android.support:$name:$version"

fun DependencyHandlerScope.anko(name : String, version : String = ankoVersion) =
        "org.jetbrains.anko:anko-$name:$version"

dependencies {
    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    implementation(android("appcompat-v7", "26.1.0"))
    implementation(android("design", "26.1.0"))
    implementation("com.android.support.constraint:constraint-layout:1.0.2")

    implementation(anko("sdk25"))
    implementation(anko("appcompat-v7"))
    implementation(anko("design"))

    implementation("com.github.HoshinoTented.MainLibrary:KotlinExtra:$hoshinoVersion")

    testImplementation("junit", "junit", "4.12")
}