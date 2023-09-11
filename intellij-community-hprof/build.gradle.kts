plugins {
    id("heapdive-kotlin")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("org.slf4j:slf4j-api:1.7.32")

    implementation("com.google.guava:guava:32.1.2-jre")
    implementation("it.unimi.dsi:fastutil:8.5.12")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.21.0")
    testImplementation("org.hamcrest:hamcrest:2.2")
}
