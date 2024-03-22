buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }
}
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("org.sonarqube") version "3.5.0.2730"
    id("com.google.gms.google-services") version "4.4.1" apply false
}

sonarqube {
  properties {
    property("sonar.projectKey", "JahidHasanCO_BMI-Calculator")
    property("sonar.organization", "jahidhasanco")
    property("sonar.host.url", "https://sonarcloud.io")
  }
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}