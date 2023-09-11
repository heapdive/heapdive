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

package heapdive.html

import com.intellij.openapi.progress.ProgressIndicator
import org.slf4j.LoggerFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class AnalyzerProgressIndicator : ProgressIndicator() {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val myStartMillis = System.currentTimeMillis()
    private val myFormat: DateFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)

    init {
        myFormat.timeZone = TimeZone.getTimeZone("UTC")
    }

    override var text: String
        get() = super.text
        set(text) {
            super.text = text
            logger.info(text)
        }

    override var text2: String
        get() = super.text2
        set(text) {
            super.text2 = text
            logger.info("  $text")
        }

    private fun print(text: String) {
        var elapsedMs = System.currentTimeMillis() - myStartMillis
        if (elapsedMs < 0) {
            elapsedMs = 0
        }
        logger.info("[${myFormat.format(elapsedMs)}] $text")
    }
}
