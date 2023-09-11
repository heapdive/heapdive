package com.intellij.util

import java.io.PrintWriter
import java.io.StringWriter

class ExceptionUtil {
    companion object {
        fun getThrowableText(t: Throwable): String {
            val writer = StringWriter()
            t.printStackTrace(PrintWriter(writer));
            return writer.buffer.toString();
        }
    }
}
