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

const HighlightText = ({text}) => {
    const highlightedText = text.split('\n').map((line, index) => {
        if (/ROOT: Static field.*CGLIB/.test(line) || /org.springframework.aop.framework/.test(line)) {
            return <span key={index} className="muted">{line}<br/></span>;
        } else if (/lettuce|mysql|jedis/.test(line)) {
            return <span key={index} className="highlight">{line}<br/></span>;
        }
        return <span key={index}>{line}<br/></span>;
    });

    return (
        <pre>{highlightedText}</pre>
    );
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
