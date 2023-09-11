import org.gradle.api.tasks.testing.logging.TestExceptionFormat

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
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.spring") version "1.9.10"
    id("com.github.node-gradle.node") version "7.0.0"
}

allprojects {
    group = "heapdive"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    // verbose output for GitHub actions
    tasks.test {
        testLogging {
            events("passed", "skipped", "failed")
            exceptionFormat = TestExceptionFormat.FULL
            showStandardStreams = true
        }
    }
}

tasks.clean {
    doLast {
        delete("heapdive-html-report/src/main/resources/static/main.bundle.js")
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
    dependsOn("createSampleHprof")
}

