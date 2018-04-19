val packageName = "top.tented.plugindemo"
val code = 1
val version = "1.0"

val kotlinVersion : String by rootProject.extra
val ankoVersion = "0.10.4"
val hoshinoVersion = "1.0.7-02"

fun DependencyHandlerScope.hoshino(name : String, version : String = hoshinoVersion) =
        "com.github.HoshinoTented.MainLibrary:$name:$version"

fun DependencyHandlerScope.android(name : String, version : String) =
        "com.android.support:$name:$version"

fun DependencyHandlerScope.anko(name : String, version : String = ankoVersion) =
        "org.jetbrains.anko:anko-$name:$version"

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion(26)
    buildToolsVersion("27.0.3")

    signingConfigs {
        create("release") {
            storeFile = File(ext["store_file"].toString())
            storePassword = ext["store_password"].toString()
            keyAlias = ext["key_alias"].toString()
            keyPassword = ext["key_password"].toString()
        }
    }

    defaultConfig {
        minSdkVersion(19)
        targetSdkVersion(26)

        versionCode = code
        versionName = version
        applicationId = packageName
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs["release"]
            proguardFiles("proguard-rules.pro")
        }
    }
}

dependencies {
    //Kotlin
    implementation(kotlin("stdlib-jdk8", kotlinVersion))

    //Android
    implementation("com.android.support.constraint", "constraint-layout", "1.0.2")
    implementation(android("appcompat-v7", "26.1.0"))
    implementation(android("design", "26.1.0"))

    //Anko
    implementation(anko("sdk25"))
    implementation(anko("appcompat-v7"))
    implementation(anko("design"))

    //Extra
    implementation(hoshino("KotlinExtra"))

    //Test
    testImplementation("junit", "junit", "4.12")
}

task<Copy>("buildRelease") {
    from("build/outputs/apk/release/")
    into("release/")
    include("app-release.apk")
    rename("app-release.apk", "$packageName-v$version.apk")
}.dependsOn("assembleRelease")