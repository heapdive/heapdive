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
import {SizeHistogramInformation} from "./sizeHistogramInformation";
import {PerClassInformation} from "./perClassInformation";
import {ByCountHistogramInformation} from "./byCountHistogramInformation";
import {MetadataPane} from "./metadataPane";
import {ChartInformation} from "./chartInformation";

export class App extends React.Component {
    render() {
        const result = this.props.result;

        return <div>
            <ChartInformation data={result.analysisReport.dominatorTree || result.analysisReport.flameGraph}/>

            <SizeHistogramInformation bySizeHistogram={result.analysisReport.histogram.bySizeHistogram}/>

            <h2>Instances of each nominated class</h2>
            <div className="container">
                <PerClassInformation data={result.analysisReport.perClass}/>
            </div>

            <ByCountHistogramInformation data={result.analysisReport.histogram}/>

            <MetadataPane result={result}/>
        </div>
    }
}
