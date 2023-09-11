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

package heapdive.server.controller

import heapdive.html.renderHProfJson
import heapdive.server.service.S3Service
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.util.UriComponentsBuilder
import java.io.File
import java.io.FileOutputStream
import java.time.YearMonth


@Controller
class HeapdiveController(
        private val s3Service: S3Service,
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val cacheKey = System.currentTimeMillis()

    @GetMapping("/")
    fun top(model: Model, request: HttpServletRequest, uriComponentsBuilder: UriComponentsBuilder): String {
        val yms = listOf(
                YearMonth.now(),
                YearMonth.now().minusMonths(1)
        )
        model.addAttribute("ymItems", yms.map { it to s3Service.fetchItemsForMonth(it) })

        val uploadUrl = uriComponentsBuilder.path("/upload").build().toUriString()
        model.addAttribute("uploadUrl", uploadUrl)

        return "top"
    }

    @PostMapping("/upload")
    fun upload(
            request: HttpServletRequest,
            uriComponentsBuilder: UriComponentsBuilder
    ): ResponseEntity<String> {
        // Create a temporary file
        val tempFile = File.createTempFile("prefix-", "-suffix")
        tempFile.deleteOnExit() // The file will be deleted when the JVM exits

        try {
            request.inputStream.use { inputStream ->
                FileOutputStream(tempFile).use { fos ->
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        fos.write(buffer, 0, bytesRead)
                    }
                }
            }

            logger.info("Uploaded file: content-length: ${request.contentLengthLong} tempfile=${tempFile.absolutePath}(size=${tempFile.length()})")

            val name = request.getHeader("x-name")

            // Generate heapdump report from hprof file.
            val report: String = renderHProfJson(tempFile.toPath())

            // and store it to the S3 bucket.
            val key = s3Service.uploadFile(report, name)
            val url = uriComponentsBuilder.path("/report/$key").build().toUriString()
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"url\":\"$url\"}\n")
        } finally {
            tempFile.delete()
        }
    }

    @GetMapping("/result/{yyyymm}/{s3key}")
    fun fetchFile(@PathVariable yyyymm: String, @PathVariable s3key: String): ResponseEntity<InputStreamResource> {
        val s3Path = "$yyyymm/$s3key"
        val inputStream = s3Service.fetchItem(s3Path)

        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML)
                .body(InputStreamResource(inputStream))
    }

    @GetMapping("/report/{yyyymm}/{s3key}")
    fun report(@PathVariable yyyymm: String, @PathVariable s3key: String, model: Model): String {
        model.addAttribute("cacheKey", cacheKey)
        return "report"
    }
}
