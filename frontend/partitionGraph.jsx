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
import * as d3 from "d3";

export class PartitionGraph extends React.Component {
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

        // Set up SVG dimensions and margins
        const margin = {top: 10, right: 10, bottom: 10, left: 10};
        const height = 600 - margin.top - margin.bottom;
        const width = this.svgRef.current.clientWidth - margin.left - margin.right;

        // Color scale for the nodes based on their value (dark for small values, vivid for large values)
        const color = d3.scaleSequential(d3.interpolatePlasma).domain([0, d3.max(data.children, d => d.value)]);

        // Create the SVG canvas
        const svg = d3.select(this.svgRef.current)
            .append("svg")
            .attr("width", width)
            .attr("height", height)
            .style("font", "10px sans-serif");

        // Create root node from hierarchy data
        const root = d3.hierarchy(data)
            .sum(d => d.value)
            .sort((a, b) => b.value - a.value);

        // Create the partition layout
        d3.partition()
            .size([height, (root.height + 1) * width / 3])
            (root);

        // Define the rectangles for the partition layout
        const rect = svg.selectAll("g")
            .data(root.descendants())
            .enter().append("g")
            .attr("transform", d => `translate(${d.y0},${d.x0})`);

        rect.append("rect")
            .attr("width", d => d.y1 - d.y0)
            .attr("height", d => d.x1 - d.x0)
            .attr("fill", d => color(d.value));

        rect.append("text")
            .attr("x", 4)
            .attr("y", 13)
            .text(d => `${d.data.name} (${formatBytes(d.value)})`);

        // Format bytes into KiB, MiB, or GiB
        function formatBytes(bytes) {
            if (bytes >= Math.pow(1024, 3)) return (bytes / Math.pow(1024, 3)).toFixed(2) + ' GiB';
            else if (bytes >= Math.pow(1024, 2)) return (bytes / Math.pow(1024, 2)).toFixed(2) + ' MiB';
            else if (bytes >= Math.pow(1024, 1)) return (bytes / Math.pow(1024, 1)).toFixed(2) + ' KiB';
            else return bytes + ' B';
        }
    }

    render() {
        return <svg ref={this.svgRef} width={300} height={150}></svg>;
    }
}
