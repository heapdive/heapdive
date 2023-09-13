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

import React from "react";

async function fetchAndUpload(actuatorUrl) {
    try {
        // Fetch the content from the actuator endpoint
        console.log(`Fetching heapdump from actuator url: ${actuatorUrl}`)

        let response = await fetch(actuatorUrl);
        if (!response.ok) {
            throw new Error(`Failed to fetch from ${actuatorUrl}: ${response.statusText}`);
        }

        // Extract the content as a blob
        let blob = await response.blob();

        // Fetch the current origin
        let origin = window.location.origin;

        // Upload the blob to the /upload endpoint
        let uploadResponse = await fetch(`${origin}/upload`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/octet-stream'
            },
            body: blob
        });

        if (!uploadResponse.ok) {
            alert(`Failed to upload data to /upload: ${uploadResponse.statusText}`);
        }

        console.log("Data successfully uploaded!");

        location.href = (await uploadResponse.json())["url"]
    } catch (error) {
        console.error("Error:", error);
    }
}

export class CorsUploadForm extends React.Component {
    state = {
        logMessage: '',
    };

    async doUpload(e) {
        e.stopPropagation()
        e.preventDefault()

        const url = this.urlInput.value;
        if (url && url !== "") {
            this.setState({
                logMessage: `Fetching heapdump from actuator url: ${url}... This may take few minutes... please wait a moment.`
            })
            const log = await fetchAndUpload(url)
            this.setState({logMessage: log});
        } else {
            console.log("URL must be provided")
        }
    }

    render() {

        return <form onSubmit={this.doUpload.bind(this)}>
            <p>
                Using Spring Boot's actuator feature, you can access the <code>/actuator/heapdump</code> endpoint via
                CORS and upload it to Heapdive.
            </p>
            <p>
                For more details, please visit <a
                href="https://github.com/heapdive/spring-boot-actuator-heapdump-cors-demo" target="_blank"
                rel="noopener noreferrer">https://github.com/heapdive/spring-boot-actuator-heapdump-cors-demo</a>.
            </p>

            <input type="url" required={true} placeholder={"http://example.com/actuator/heapdump"}
                   ref={(input) => {
                       this.urlInput = input;
                   }}
            />
            <button onClick={this.doUpload.bind(this)}>Upload</button>
            <div className={"corsUploadForm-log"}>
                {this.state.logMessage}
            </div>
        </form>;
    }
}
