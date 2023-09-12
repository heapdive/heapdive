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

import {flamegraph} from "d3-flame-graph";
import * as d3 from "d3";
import React from "react";

export class FlameGraph extends React.Component {
    constructor(props) {
        super(props);
        this.svgRef = React.createRef();
    }

    componentDidMount() {
        this.drawChart();
    }

    componentDidUpdate() {
        this.drawChart();
    }

    drawChart() {
        const {data} = this.props;

        const chart = flamegraph()
            .width(960);

        const svg = d3.select(this.svgRef.current);
        svg.datum(data).call(chart);
    }

    render() {
        return <svg ref={this.svgRef} width={300} height={150}></svg>;
    }
}
