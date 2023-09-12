/*
 *     Copyright 2023 Tokuhiro Matsuno
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *            http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

plugins {
    id("heapdive-kotlin")
    application
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":intellij-community-hprof"))
    implementation(project(":heapdive-html-report"))

    implementation(libs.fastutil)

    implementation(libs.guava)

    implementation(libs.commons.text)

    implementation(libs.logback)
    implementation(libs.slf4j)

    implementation(libs.kotlinx.cli)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.assertj.core)
}

application {
    mainClass.set("heapdive.HeapdiveApplicationKt")
}
