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

package heapdive.html

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.intellij.diagnostic.hprof.action.SystemTempFilenameSupplier
import heapdive.html.report.HTMLHProfAnalysis
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.StandardOpenOption

fun renderHProfReport(filePath: Path): String {
    val json = renderHProfJson(filePath)
    return rewriteTemplate(AnalyzerProgressIndicator::class.java.classLoader
            .getResourceAsStream("template.html")!!.readAllBytes()
            .toString(StandardCharsets.UTF_8), json)
}

fun renderHProfJson(filePath: Path): String {
    val reportBody = FileChannel.open(filePath, StandardOpenOption.READ).use { channel ->
        val analysis = HTMLHProfAnalysis(channel, SystemTempFilenameSupplier())
        analysis.onlyStrongReferences = false
        analysis.includeClassesAsRoots = true

        val progress = AnalyzerProgressIndicator()
        val report = analysis.analyze(progress)
        progress.text = "DONE"
        report
    }
    return ObjectMapper()
            .registerModule(JavaTimeModule())
            .writeValueAsString(reportBody)
}
