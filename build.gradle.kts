buildscript {
    repositories {
        google()
        jcenter()
//        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:3.0.1")
        classpath(kotlin("gradle-plugin", "1.2.30"))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}