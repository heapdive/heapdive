plugins {
    id("heapdive-kotlin")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(libs.logback)
    implementation(libs.slf4j)

    implementation(libs.guava) // used for Stopwatch
    implementation(libs.fastutil)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.assertj.core)
}
