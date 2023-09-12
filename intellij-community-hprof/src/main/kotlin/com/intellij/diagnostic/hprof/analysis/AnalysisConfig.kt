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
package com.intellij.diagnostic.hprof.analysis

class AnalysisConfig(
        val perClassOptions: PerClassOptions,
        val histogramOptions: HistogramOptions = HistogramOptions(),
        val traverseOptions: TraverseOptions = TraverseOptions(),
        val dominatorTreeOptions: DominatorTreeOptions = DominatorTreeOptions()
) {

    class PerClassOptions(
            val classNames: List<String>,
            val treeDisplayOptions: TreeDisplayOptions = TreeDisplayOptions.default()
    )

    class TreeDisplayOptions(
            val minimumObjectSize: Int = 30_000_000,
            val minimumObjectCount: Int = 50_000,
            val minimumSubgraphSize: Long = 100_000_000,
            val minimumObjectCountPercent: Int = 15,
            val maximumTreeDepth: Int = 80,
            val maximumIndent: Int = 40,
            val minimumPaths: Int = 2,
            val headLimit: Int = 100,
            val tailLimit: Int = 25,
            val smartIndent: Boolean = true,
            val showSize: Boolean = true
    ) {
        companion object {
            fun default(): TreeDisplayOptions = TreeDisplayOptions()
            fun all(smartIndent: Boolean = true,
                    showSize: Boolean = true): TreeDisplayOptions =
                    TreeDisplayOptions(minimumObjectSize = 0,
                            minimumObjectCount = 0,
                            minimumSubgraphSize = 0,
                            minimumObjectCountPercent = 0,
                            headLimit = Int.MAX_VALUE,
                            tailLimit = 0,
                            minimumPaths = Int.MAX_VALUE,
                            smartIndent = smartIndent,
                            showSize = showSize)
        }
    }

    class HistogramOptions(
            val includeByCount: Boolean = true,
            val includeBySize: Boolean = true,
            val classByCountLimit: Int = 50,
            val classBySizeLimit: Int = 10
    )

    class TraverseOptions(
            val onlyStrongReferences: Boolean = false,
            val includeClassesAsRoots: Boolean = true,
            val includeDisposerRelationships: Boolean = true,
            val includeFieldInformation: Boolean = true
    )

    class DominatorTreeOptions(
            val includeDominatorTree: Boolean = true,
            val maxDominatorIterations: Int = 2_000,
            val minNodeSize: Int = 100_000,
            val maxDepth: Int = 30,
            val headLimit: Int = 5_000,
            val diskSpaceThreshold: Long = 500_000_000L
    )
}
