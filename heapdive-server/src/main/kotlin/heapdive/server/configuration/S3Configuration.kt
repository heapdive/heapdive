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

package heapdive.server.configuration

import heapdive.server.properties.S3Properties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(S3Properties::class)
class S3Configuration(
        private val s3Properties: S3Properties,
) {
    @Bean
    fun s3client(): S3Client? {
        val builder = S3Client.builder()
                .region(Region.of(s3Properties.region))
        if (s3Properties.endpoint != null) {
            builder.endpointOverride(URI(s3Properties.endpoint))
        }

        return builder
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(s3Properties.accessKey, s3Properties.secretKey)
                        )
                )
                .build()
    }
}
