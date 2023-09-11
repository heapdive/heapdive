# intellij-community-hprof

This directory contains code from the IntelliJ Community Edition project, which is licensed under the Apache License,
Version 2.0.
The code is used to analyze a heap dump file.

See https://github.com/JetBrains/intellij-community/tree/master/platform/platform-impl/src/com/intellij/diagnostic/hprof

This code base is taken from git hash 3a08ca3f08a6bed769f554b6edb27918cd1def62.

## Code taking process

    mkdir -p intellij-community-hprof/src/main/kotlin/com/intellij/diagnostic/hprof
    cp -a ../intellij-community/platform/platform-impl/src/com/intellij/diagnostic/hprof/* intellij-community-hprof/src/main/kotlin/com/intellij/diagnostic/hprof 

Add fastutil and guava as dependencies.

And I done the following process:

- Use org.slf4j.Logger instead of com.intellij.openapi.diagnostic.Logger
- Do the following process for each package:
    - action
        - AnalysisRunnable, heapDumpActions, HeapDumpSnapshotRunnable are tightly coupled with IntelliJ IDEA GUI.
        - Copy SystemTempFilenameSupplier in AnalysisRunnable. It's required component.
    - analysis
        - Can't compile AnalyzeGraph, AnalyzeDisposer, AnalyzeClassloaderReferencesGraph, LiveInstanceStats.
        - these classes are tightly coupled with IntelliJ IDEA GUI.
        - HProfAnalysis is required class.
            - add DiagnosticBundle
    - classstore
    - histogram
    - navigator
    - parser
    - util
        - HProfReadBufferSliding depends on ByteBufferCleaner. Copy from the original source.
    - visitors
