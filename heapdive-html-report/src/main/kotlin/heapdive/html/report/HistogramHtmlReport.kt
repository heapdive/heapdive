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

package heapdive.html.report

import com.intellij.diagnostic.hprof.analysis.AnalysisConfig
import com.intellij.diagnostic.hprof.histogram.Histogram
import com.intellij.diagnostic.hprof.histogram.HistogramEntry
import heapdive.html.model.HistogramItem
import heapdive.html.model.HistogramReport
import heapdive.html.model.HistogramSummary
import heapdive.html.model.HistogramTable
import kotlin.math.min

class HistogramHtmlReport {
    companion object {
        fun prepareMergedHistogramReport(allHistogram: Histogram,
                                         strongRefHistogram: Histogram,
                                         options: AnalysisConfig.HistogramOptions, unreachableObjectsCount: Long,
                                         unreachableObjectsSize: Long): HistogramReport {
            val mapClassNameToEntrySecondary = HashMap<String, HistogramEntry>()
            strongRefHistogram.entries.forEach {
                mapClassNameToEntrySecondary[it.classDefinition.name] = it
            }

            val allHistogramSummary = getSummaryLine(allHistogram, "All")
            val strongRefHistogramSummary = getSummaryLine(strongRefHistogram, "Strong-Ref")

            val byCountEntries = allHistogram.entries.map { entry ->
                val entry2 = mapClassNameToEntrySecondary[entry.classDefinition.name]
                HistogramItem(
                        entry.totalInstances,
                        entry.totalBytes,
                        entry2?.totalInstances ?: 0,
                        entry2?.totalBytes ?: 0,
                        entry.classDefinition.prettyName
                )
            }
            val byCount = HistogramTable(
                    limit = options.classByCountLimit,
                    removedCount = if (byCountEntries.size > options.classByCountLimit) {
                        byCountEntries.size - options.classByCountLimit
                    } else {
                        0
                    },
                    entries = byCountEntries.take(options.classByCountLimit),
            )

            val classCountInByBytesSection = min(allHistogram.entries.size, options.classBySizeLimit)
            val entriesByBytes = allHistogram.entries.sortedByDescending { it.totalBytes }
            val entries = (0 until classCountInByBytesSection).map { i ->
                val entry: HistogramEntry = entriesByBytes[i]
                val entry2 = mapClassNameToEntrySecondary[entry.classDefinition.name]
                HistogramItem(
                        entry.totalInstances,
                        entry.totalBytes,
                        entry2?.totalInstances ?: 0,
                        entry2?.totalBytes ?: 0,
                        entry.classDefinition.prettyName
                )
            }
            val bySize = HistogramTable(
                    limit = classCountInByBytesSection,
                    removedCount = if (entriesByBytes.size > classCountInByBytesSection) {
                        entriesByBytes.size - classCountInByBytesSection
                    } else {
                        0
                    },
                    entries = entries,
            )
            return HistogramReport(
                    byCount,
                    bySize,
                    allHistogramSummary,
                    strongRefHistogramSummary,
                    unreachableObjectsCount,
                    unreachableObjectsSize
            )
        }

        private fun getSummaryLine(histogram: Histogram,
                                   histogramName: String): HistogramSummary {
            val (totalInstances, totalBytes) = histogram.getTotals()
            return HistogramSummary(
                    histogramName,
                    totalInstances,
                    totalBytes,
                    histogram.entries.size,
                    histogram.instanceCount)
        }
    }
}
