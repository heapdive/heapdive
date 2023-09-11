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
import {HeapSummaryInformation} from "./heapSummaryInformation";
import {AnalysisInformation} from "./analysisInformation";

export class MetadataPane extends React.Component {
    constructor() {
        super();
        this.state = {showPane: false};
    }

    render() {
        const result = this.props.result;
        return <div>
            {this.state.showPane ? (
                <div>
                    <a href="#" onClick={(e) => {
                        e.preventDefault();
                        e.stopPropagation();
                        this.setState({showPane: false});
                        return false;
                    }}>Hide metadata</a>

                    <h2>Heap summary</h2>
                    <div className="container">
                        <HeapSummaryInformation data={result.analysisReport.heapSummary}/>
                    </div>

                    <h2>Analysis information</h2>
                    <div className="container">
                        <AnalysisInformation data={result.metaInfo}/>
                    </div>

                    <h2>Log</h2>
                    <div className="container">
                        <pre>{result.analysisReport.log}</pre>
                    </div>
                </div>
            ) : <a href="#" onClick={() => {
                this.setState({showPane: true});
                return false;
            }}>Show metadata</a>}
        </div>
    }
}
