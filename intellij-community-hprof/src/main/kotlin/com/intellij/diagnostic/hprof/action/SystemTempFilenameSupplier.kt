package com.intellij.diagnostic.hprof.action

import com.intellij.diagnostic.hprof.analysis.TempFilenameSupplier
import java.nio.file.Files
import java.nio.file.Path

class SystemTempFilenameSupplier : TempFilenameSupplier {
    override fun getTempFilePath(type: String): Path {
        return Files.createTempFile("heap-dump-analysis-", "-$type.tmp")
    }
}
