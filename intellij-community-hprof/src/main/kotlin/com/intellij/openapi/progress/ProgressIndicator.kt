package com.intellij.openapi.progress

import kotlin.properties.Delegates

open class ProgressIndicator {
    open lateinit var text: String
    open lateinit var text2: String
    var fraction by Delegates.notNull<Double>()
}
