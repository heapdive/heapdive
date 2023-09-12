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

import java.nio.charset.StandardCharsets

fun rewriteTemplate(template: String, reportBody: String): String {
    val classLoader = AnalyzerProgressIndicator::class.java.classLoader
    fun readResource(name: String): String {
        val stream = classLoader.getResourceAsStream(name)
        checkNotNull(stream) { "$name is not available in ClassLoader resource" }
        return stream.readAllBytes().toString(StandardCharsets.UTF_8)
    }

    return template.replace("""<!-- BEGIN_REPORT -->.*?<!-- END_REPORT -->""".toRegex(RegexOption.DOT_MATCHES_ALL)) {
        """
        <script type="text/javascript">
        window.HEAPDIVE_RESULT = $reportBody;
        </script>
        
        <script type="text/javascript">
        ${readResource("static/main.bundle.js")}
        </script>
        <style>
        ${readResource("static/styles.css")}
        </style>
        """.trimIndent()
    }
}
