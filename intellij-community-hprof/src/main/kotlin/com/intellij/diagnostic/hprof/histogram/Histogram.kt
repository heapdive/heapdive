/*
 * Copyright (C) 2018 The Android Open Source Project
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
package com.intellij.diagnostic.hprof.histogram

import com.intellij.diagnostic.hprof.classstore.ClassStore
import com.intellij.diagnostic.hprof.parser.HProfEventBasedParser
import com.intellij.diagnostic.hprof.util.HeapReportUtils.toPaddedShortStringAsCount
import com.intellij.diagnostic.hprof.util.HeapReportUtils.toPaddedShortStringAsSize
import com.intellij.diagnostic.hprof.util.TruncatingPrintBuffer
import com.intellij.diagnostic.hprof.visitors.HistogramVisitor

class Histogram(val entries: List<HistogramEntry>, val instanceCount: Long) {

    fun getTotals(): Pair<Long, Long> {
        var totalInstances = 0L
        var totalBytes = 0L
        entries.forEach {
            totalBytes += it.totalBytes
            totalInstances += it.totalInstances
        }
        return Pair(totalInstances, totalBytes)
    }

    val bytesCount: Long = getTotals().second

    fun prepareReport(name: String, topClassCount: Int): String {
        val result = StringBuilder()
        result.appendLine("Histogram. Top $topClassCount by instance count:")
        val appendToResult = { s: String -> result.appendLine(s); Unit }
        var counter = 1

        TruncatingPrintBuffer(topClassCount, 0, appendToResult).use { buffer ->
            entries.forEach { entry ->
                buffer.println(formatEntryLine(counter, entry))
                counter++
            }
        }
        result.appendLine(getSummaryLine(this, name))
        result.appendLine()
        result.appendLine("Top 10 by bytes count:")
        val entriesByBytes = entries.sortedByDescending { it.totalBytes }
        for (i in 0 until 10) {
            val entry = entriesByBytes[i]
            result.appendLine(formatEntryLine(i + 1, entry))
        }
        return result.toString()
    }

    companion object {
        fun create(parser: HProfEventBasedParser, classStore: ClassStore): Histogram {
            val histogramVisitor = HistogramVisitor(classStore)
            parser.accept(histogramVisitor, "histogram")
            return histogramVisitor.createHistogram()
        }

        private fun getSummaryLine(histogram: Histogram,
                                   histogramName: String): String {
            val (totalInstances, totalBytes) = histogram.getTotals()
            return String.format("Total - %10s: %s %s %d classes (Total instances: %d)",
                    histogramName,
                    toPaddedShortStringAsCount(totalInstances),
                    toPaddedShortStringAsSize(totalBytes),
                    histogram.entries.size,
                    histogram.instanceCount)
        }

        private fun formatEntryLine(counter: Int, entry: HistogramEntry): String {
            return String.format("%5d: [%s/%s] %s",
                    counter,
                    toPaddedShortStringAsCount(entry.totalInstances),
                    toPaddedShortStringAsSize(entry.totalBytes),
                    entry.classDefinition.prettyName)
        }

    }
}
