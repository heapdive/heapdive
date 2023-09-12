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
import {FlameGraph} from "./flameGraph";
import {PartitionGraph} from "./partitionGraph";

export class ChartInformation extends React.Component {
    render() {
        return <div>
            <h2>Charts</h2>

            <h3>Flame graph</h3>
            <FlameGraph data={this.props.data}/>
            <h3>Partition graph</h3>
            <PartitionGraph data={this.props.data}/>
        </div>
    }
}
