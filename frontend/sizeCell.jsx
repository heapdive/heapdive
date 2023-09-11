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

export class SizeCell extends React.Component {
    render() {
        const {bytes} = this.props;
        const color = this.bytesToColor(bytes);

        return (
            <span style={{color: color, textAlign: "right"}}>
                            {this.formatBytes(bytes)}
                        </span>
        );
    }

    bytesToColor(value) {
        // ここでは例として100MBを最大の赤色とする
        const maxBytes = 100 * 1024 * 1024;

        const intensity = Math.min(value / maxBytes, 1);
        const redValue = Math.floor(intensity * 255);

        return `rgb(${redValue}, 0, 0)`;
    }


    formatBytes(bytes) {
        if (bytes >= 1024 * 1024 * 1024) {
            return (bytes / (1024 * 1024 * 1024)).toFixed(2) + ' GiB';
        } else if (bytes >= 1024 * 1024) {
            return (bytes / (1024 * 1024)).toFixed(2) + ' MiB';
        } else if (bytes >= 1024) {
            return (bytes / 1024).toFixed(2) + ' KiB';
        } else {
            return bytes + ' B';
        }
    }

}
