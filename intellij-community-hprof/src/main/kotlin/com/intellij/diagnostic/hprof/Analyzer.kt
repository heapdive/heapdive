/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.diagnostic.hprof

import com.intellij.diagnostic.hprof.action.SystemTempFilenameSupplier
import com.intellij.diagnostic.hprof.analysis.AnalysisContext
import com.intellij.diagnostic.hprof.analysis.AnalyzeGraph
import com.intellij.diagnostic.hprof.analysis.HProfAnalysis
import com.intellij.diagnostic.hprof.util.ListProvider
import com.intellij.openapi.progress.ProgressIndicator
import java.nio.channels.FileChannel
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.system.exitProcess

internal class AnalyzerProgressIndicator : ProgressIndicator() {
    private val myStartMillis = System.currentTimeMillis()
    private val myFormat: DateFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)

    init {
        myFormat.timeZone = TimeZone.getTimeZone("UTC")
    }

    override var text: String
        get() = super.text
        set(text) {
            super.text = text
            print(text)
        }

    override var text2: String
        get() = super.text2
        set(text) {
            super.text2 = text
            print("  $text")
        }

    private fun print(text: String) {
        var elapsedMs = System.currentTimeMillis() - myStartMillis
        if (elapsedMs < 0) {
            elapsedMs = 0
        }
        System.out.printf("[%s] %s...%n", myFormat.format(elapsedMs), text)
    }
}

fun main(args: Array<String>) {
    if (args.isEmpty() || args.size != 1) {
        println()
        println("Usage: java -jar heapdive-cli.jar <hprof file>")
        exitProcess(1)
    }

    val analysisCallback = { analysisContext: AnalysisContext, listProvider: ListProvider, progressIndicator: ProgressIndicator ->
        AnalyzeGraph(analysisContext, listProvider).analyze(progressIndicator).mainReport.toString()
    }
    val hprofPath = Paths.get(args[0])
    var report: String
    FileChannel.open(hprofPath, StandardOpenOption.READ).use { channel ->
        val analysis = HProfAnalysis(channel, SystemTempFilenameSupplier(), analysisCallback)
        analysis.onlyStrongReferences = false
        analysis.includeClassesAsRoots = true

        val progress = AnalyzerProgressIndicator()
        report = analysis.analyze(progress)
        progress.text = "DONE"
    }
    println(report)
}
