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
    kotlin("jvm")
    id("com.github.node-gradle.node") version "7.1.0"
}

allprojects {
    group = "heapdive"
    version = if (project.hasProperty("releaseVersion")) {
        project.findProperty("releaseVersion").toString()
    } else {
        "0.0.1-SNAPSHOT"
    }

    repositories {
        mavenCentral()
    }
}

tasks.register("createSampleHprof") {
    doLast {
        exec {
            commandLine("python3", "demo/simple/make-dump.py", "Hi")
        }
    }
}

tasks.named("build") {
    dependsOn("npmInstall", "npm_run_build")
    dependsOn("createSampleHprof")
}

