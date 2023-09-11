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

export class HeapSummaryInformation extends React.Component {
    render() {
        const {data} = this.props;

        return <table>
            <tbody>
            <tr>
                <th>Analysis completed! Visited instances</th>
                <td>{data.visitedInstancesCount}(time: {data.elapsed} ms)</td>
            </tr>
            <tr>
                <th>Update sizes time</th>
                <td>{data.updateSizesTime} ms</td>
            </tr>
            <tr>
                <th>Leaves found</th>
                <td>{data.leavesFound}</td>
            </tr>
            <tr>
                <th>Class count</th>
                <td>{data.classCount}</td>
            </tr>
            <tr>
                <th>Finalizable size</th>
                <td><SizeCell bytes={data.finalizableSize}/></td>
            </tr>
            <tr>
                <th>Soft-reachable size</th>
                <td><SizeCell bytes={data.softReachableSize}/></td>
            </tr>
            <tr>
                <th>Weak-reachable size</th>
                <td><SizeCell bytes={data.weakReachableSize}/></td>
            </tr>
            <tr>
                <th>Reachable only from disposer tree</th>
                <td>{data.reachableOnlyFromDisposerTree}</td>
            </tr>
            </tbody>
        </table>
    }
}
