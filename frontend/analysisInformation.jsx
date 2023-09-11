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
import {SizeCell} from "./sizeCell";

export class AnalysisInformation extends React.Component {
    render() {
        return <div>
            <table>
                <thead>
                <tr>
                    <th>Type</th>
                    <th>Duration</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Prepare files duration</td>
                    <td>{this.props.data.prepareFilesDuration} ms</td>
                </tr>
                <tr>
                    <td>Analysis duration</td>
                    <td>{this.props.data.analysisDuration} ms</td>
                </tr>
                <tr>
                    <td>TOTAL DURATION</td>
                    <td>{this.props.data.totalDuration} ms</td>
                </tr>
                </tbody>
            </table>

            <table>
                <thead>
                <tr>
                    <th>Temporary File</th>
                    <th>Size</th>
                </tr>
                </thead>
                <tbody>
                {Object.keys(this.props.data.tempFiles).map(file => (
                    <tr key={file}>
                        <td>{file}</td>
                        <td><SizeCell bytes={this.props.data.tempFiles[file]}/></td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    }
}
