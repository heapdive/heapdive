package com.intellij.diagnostic.hprof.util

import com.intellij.openapi.progress.ProgressIndicator

class PartialProgressIndicator(progress: ProgressIndicator,
                               private val start: Double,
                               private val duration: Double) : ProgressIndicator() {
}
