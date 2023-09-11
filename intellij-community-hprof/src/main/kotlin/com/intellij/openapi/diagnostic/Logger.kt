package com.intellij.openapi.diagnostic

import org.slf4j.Logger

class Logger {
    companion object {
        fun <T> getInstance(java: Class<T>): Logger {
            return org.slf4j.LoggerFactory.getLogger(java)
        }
    }
}

