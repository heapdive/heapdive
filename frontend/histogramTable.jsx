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

export class HistogramTable extends React.Component {
    render() {
        return <table>
            <thead>
            <tr>
                <th>Class</th>
                <th>Count</th>
                <th>Retained Size</th>
            </tr>
            </thead>
            <tbody>
            {this.props.histogram.entries.map(entry => {
                return <tr key={entry.prettyName}>
                    <td>{entry.prettyName}</td>
                    <td>{entry.allTotalInstances}</td>
                    <td><SizeCell bytes={entry.allTotalBytes}/></td>
                </tr>
            })}
            {this.props.additionalRows()}
            </tbody>
        </table>
    }
}
