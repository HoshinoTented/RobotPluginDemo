import org.gradle.internal.impldep.org.eclipse.jgit.util.Paths

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

val packageName = "top.tented.plugindemo"
val version = "1.0"
val code = 1

val ankoVersion = "0.10.4"
val hoshinoVersion = "1.0.5"
val kotlinVersion = "1.2.30"

fun DependencyHandlerScope.hoshino(name : String, version : String = hoshinoVersion) =
        "com.github.HoshinoTented.MainLibrary:$name:$version"

fun DependencyHandlerScope.android(name : String, version : String) =
        "com.android.support:$name:$version"

fun DependencyHandlerScope.anko(name : String, version : String = ankoVersion) =
        "org.jetbrains.anko:anko-$name:$version"

dependencies {
    //Kotlin
    implementation(kotlin("stdlib-jdk8", kotlinVersion))

    //Android
    implementation(android("appcompat-v7", "26.1.0"))
    implementation(android("design", "26.1.0"))
    implementation("com.android.support.constraint:constraint-layout:1.0.2")

    //Anko
    implementation(anko("sdk25"))
    implementation(anko("appcompat-v7"))
    implementation(anko("design"))

    //Extra
    implementation(hoshino("KotlinExtra"))

    //Test
    testImplementation("junit", "junit", "4.12")
}

/**
 * if(isLinux or isMac && hasDir("release")) {
 *      runTask(this)
 * } else throw Exception(...)
 */
task<Exec>("buildRelease") {
    commandLine(
            "cp",
            "-rf",
            "./build/outputs/apk/release/app-release.apk",
            "./release/$packageName-$version.apk"
    )
}.dependsOn("assembleRelease")