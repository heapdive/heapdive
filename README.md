# heapdive

HeapDive is a heap dump analyzer based on IntelliJ IDEA's heap dump analyzer.

HeapDive is constructed by the following components:

- heapdive-server
    - A server which receives heap dump files and analyze it. and then, stores the analyzed report to S3.
    - Show the analyzed report on the web browser.
- heapdive-cli
    - CLI tool to generate heap dump analyze report.
    - This tool generates single HTML file without dependency.

Generated report doesn't contain any Heap data itself. You can share the report with your co-workers.

## Hacking

Run webpack in watch mode

    npm run start

## LICENSE

    Copyright 2023 Tokuhiro Matsuno
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
           http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
