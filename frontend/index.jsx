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
import {flamegraph} from "d3-flame-graph";
import * as d3 from "d3";
import {App} from "./app";

import "./css/main.css";
import 'd3-flame-graph/dist/d3-flamegraph.css'
import {createRoot} from "react-dom/client";

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

function renderFlameGraph(result, selector) {
    const flamegraphData = result.analysisReport.flameGraph;

    const chart = flamegraph()
        .width(960);

    d3.select(selector)
        .datum(flamegraphData)
        .call(chart);
}

async function main() {
    const result = window.HEAPDIVE_RESULT ? window.HEAPDIVE_RESULT : await getResult();
    console.log(result)

    renderFlameGraph(result, "#chart")

    const root = createRoot(document.getElementById('root'));
    root.render(<App result={result}/>);
}

document.addEventListener("DOMContentLoaded", () => {
    main()
});

