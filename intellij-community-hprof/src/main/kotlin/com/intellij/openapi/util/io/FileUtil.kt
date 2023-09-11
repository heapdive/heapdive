package com.intellij.openapi.util.io

import java.io.File


class FileUtil {

    companion object {
        private var ourCanonicalTempPathCache: String? = null

        fun getTempDirectory(): String {
            if (ourCanonicalTempPathCache == null) {
                ourCanonicalTempPathCache = calcCanonicalTempPath()
            }
            return ourCanonicalTempPathCache!!
        }


        private fun calcCanonicalTempPath(): String {
            return File(System.getProperty("java.io.tmpdir")).absolutePath
        }
    }

}
