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

package heapdive.server.service

import heapdive.server.properties.S3Properties
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.HeadObjectRequest
import software.amazon.awssdk.services.s3.model.ListObjectsRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.S3Object
import java.io.InputStream
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class ObjectEntry(
        val key: String,
        val name: String?,
        val lastModified: ZonedDateTime,
)

@EnableConfigurationProperties(S3Properties::class)
@Service
class S3Service(
        private val s3Client: S3Client,
        s3Properties: S3Properties,
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val bucketName = s3Properties.bucketName

    fun fetchItemsForMonth(yearMonth: YearMonth): List<ObjectEntry> {
        val listObjectsRequest = ListObjectsRequest.builder()
                .bucket(bucketName)
                .prefix(yearMonth.format(DateTimeFormatter.ofPattern("yyyyMM")) + "/")
                .build()
        val response = s3Client.listObjects(listObjectsRequest)
        return response.contents()
                .map { obj: S3Object -> buildObjectData(obj.key()) }
                .sortedByDescending { it.lastModified }
    }

    private fun buildObjectData(key: String): ObjectEntry {
        val headObjectResponse = s3Client.headObject(HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build())
        val lastModified = headObjectResponse.lastModified().atZone(ZoneId.systemDefault())
        val name = headObjectResponse.metadata()["name"]
        return ObjectEntry(key, name, lastModified)
    }

    fun fetchItem(s3Path: String): InputStream {
        logger.info("Getting $s3Path from $bucketName")

        val getObjectRequest: GetObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Path)
                .build()
        return s3Client.getObject(getObjectRequest)
    }

    fun uploadFile(report: String, name: String?): String {
        val key = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyyMM")) + "/" + UUID.randomUUID().toString()
        val metadata = if (name == null) {
            emptyMap()
        } else {
            mapOf("name" to name)
        }

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .metadata(metadata)
                        .build(),
                RequestBody.fromString(report)
        )
        logger.info("Wrote file to key: $key")
        return key
    }
}

