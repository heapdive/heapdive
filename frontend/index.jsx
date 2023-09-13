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

import React from 'react';
import {App} from "./app";

import "./css/main.css";
import 'd3-flame-graph/dist/d3-flamegraph.css'
import {createRoot} from "react-dom/client";
import {CorsUploadForm} from "./corsUploadForm";

function getApiUrl() {
    let path = new URL(location.href).pathname;
    let reportRegex = /^\/report\/(\d{6}\/[^/]+)$/;
    let match = path.match(reportRegex);
    if (match) {
        return "/result/" + match[1];
    } else {
        return "sample.hprof.analyzed.json";
    }
}

async function getResult() {
    const url = getApiUrl();
    const response = await fetch(url);
    const json = await response.json();
    console.log(json)
    return json;
}

function renderChart(result, id) {
    const chart = document.getElementById(id);
    if (!chart) return;

    const flameGraphContainer = document.createElement("div");
    flameGraphContainer.id = "flameGraphContainer";
    chart.appendChild(flameGraphContainer);

    const partitionGraphTitle = document.createElement("h3");
    partitionGraphTitle.innerText = "Partition Graph";
    chart.appendChild(partitionGraphTitle);

    const partitionGraphContainer = document.createElement("div");
    partitionGraphContainer.id = "treeGraphContainer";
    partitionGraphContainer.style.minHeight = "600px";
    chart.appendChild(partitionGraphContainer);

    // renderFlameGraph(result, "#" + flameGraphContainer.id)
    renderPartitions(result.analysisReport.flameGraph, "#" + partitionGraphContainer.id)
}


// Following code is generated by the ChatGPT.
//
// I want to render Partitions graph using D3(using d3.partition).
// - write comments in English
// - write in JavaScript
// - render the value as a bytes using GiB, MiB or KiB.
// - write your code as a function.
// - the function takes 2 argument. 1st argument is the data, that have a format of the d3's hierarchy. 2nd argument is the selector of the target node.
// - function name should be `renderPartitions`
// - show the graph as... height is same as the parent-node. width should be scrollable, it always show the full content.
// - for each node, could you coloring each node? I want to apply the vivid color for large node, and dark color for the nodes, that have a small value.
/**
 * Render an icicle diagram based on hierarchical data.
 *
 * @param {Object} hierarchyData - Hierarchical data in D3's hierarchy format.
 * @param {string} targetSelector - The selector for the target node to append the SVG.
 */
function renderPartitions(hierarchyData, targetSelector) {
}

async function main() {
    const reportRootElement = document.getElementById('report-root');
    if (reportRootElement) {
        await renderReport(reportRootElement);
    }

    const corsUploadForm = document.getElementById('cors-upload-form');
    if (corsUploadForm) {
        renderCorsUploadForm(corsUploadForm)
    }
}

function renderCorsUploadForm(corsUploadForm) {
    const root = createRoot(corsUploadForm);
    root.render(<CorsUploadForm/>);
}


async function renderReport(targetElement) {
    const result = window.HEAPDIVE_RESULT ? window.HEAPDIVE_RESULT : await getResult();
    console.log(result)

    renderChart(result, "chart");

    const root = createRoot(targetElement);
    root.render(<App result={result}/>);
}

document.addEventListener("DOMContentLoaded", () => {
    main()
});

