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

package heapdive

import heapdive.html.renderHProfReport
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.writeText

class HeapdiveApplication {
    fun run(filePath: Path, output: String?) {
        val report = renderHProfReport(filePath)

        // dump!
        if (output != null) {
            Paths.get(output).writeText(report)
        } else {
            println(report)
        }
    }
}

fun main(args: Array<String>) {
    val parser = ArgParser("heapdive-cli")
    val input by parser.option(ArgType.String, shortName = "i", description = "Input file").required()
    val output by parser.option(ArgType.String, shortName = "o", description = "Output file name")
    parser.parse(args)

    println("Start $input to $output")

    val app = HeapdiveApplication()
    app.run(Paths.get(input), output)
}
