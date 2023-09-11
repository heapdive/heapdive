package com.intellij.diagnostic.hprof.action

import com.intellij.diagnostic.hprof.analysis.HProfAnalysis
import java.nio.file.Files
import java.nio.file.Path

class SystemTempFilenameSupplier : HProfAnalysis.TempFilenameSupplier {
    override fun getTempFilePath(type: String): Path {
        return Files.createTempFile("heap-dump-analysis-", "-$type.tmp")
    }
}
