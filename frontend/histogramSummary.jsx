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
import {CountCell} from "./countCell";
import {SizeCell} from "./sizeCell";

export class HistogramSummary extends React.Component {
    render() {
        console.log(this.props)
        return <div>
            Total - {this.props.summary.histogramName}:
            <CountCell count={this.props.summary.totalInstances}/>&nbsp;
            <SizeCell bytes={this.props.summary.totalBytes}/>&nbsp;
            {this.props.summary.classCount} classes (Total instances: {this.props.summary.instanceCount})
        </div>
    }
}
