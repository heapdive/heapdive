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

function processText(text) {
    const lines = text.split('\n');
    const newLines = lines.map((line, index) => {
        if (/(ROOT: Static field.*CGLIB|org.springframework.aop.framework)/.test(line)) {
            return <span key={index} className="muted">${line}<br/></span>;
        }

        const match = line.match(/]\s*([0-9.]+)([KMGTP]?B)/);
        if (match) {
            const sizeInBytes = convertToBytes(parseFloat(match[1]), match[2]);
            const color = getColorBasedOnSize(sizeInBytes);
            return <span key={index} style={{color: color}}>{line}<br/></span>;
        } else {
            return <span>{line}<br/></span>;
        }
    });

    return <div>{newLines}</div>;
}

function convertToBytes(size, unit) {
    const units = {
        'B': 1,
        'KB': 1e3,
        'MB': 1e6,
        'GB': 1e9,
        'TB': 1e12,
        'PB': 1e15,
        'EB': 1e18,
        'ZB': 1e21,
        'YB': 1e24
    };

    return size * units[unit];
}

function getColorBasedOnSize(size) {
    const MAX_SIZE = 100e6; // 100MB
    const MIN_SIZE = 1e6;  // 1MB

    if (size >= MAX_SIZE) return 'red';
    if (size <= MIN_SIZE) return 'black';

    // Gradient logic: linear gradient between black (0) and red (100)
    const ratio = (size - MIN_SIZE) / (MAX_SIZE - MIN_SIZE);
    const r = Math.min(255, Math.round(255 * ratio));
    return `rgb(${r}, 0, 0)`;
}

const HighlightText = ({text}) => {
    return processText(text);
}

export class PerClassInformation extends React.Component {
    render() {
        return <table>
            <tbody>
            {this.props.data.map(item => {
                return <tr key={item.className}>
                    <td style={{"verticalAlign": "top"}}>
                        {item.className} (count: {item.objectCount})
                        <div className={"elapsedNote"}>Elapsed: {item.elapsedMillis}ms</div>
                    </td>
                    <td>
                        <pre><HighlightText text={item.tree}/></pre>
                    </td>
                </tr>
            })}
            </tbody>
        </table>
    }
}
