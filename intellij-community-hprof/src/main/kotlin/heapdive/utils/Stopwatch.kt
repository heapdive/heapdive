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

package heapdive.utils

class Stopwatch {
    private var startedAt: Long? = null
    private var stoppedAt: Long? = null

    fun reset(): Stopwatch {
        this.startedAt = null
        this.stoppedAt = null
        return this
    }

    fun start(): Stopwatch {
        this.startedAt = System.currentTimeMillis()
        return this
    }

    fun stop(): Stopwatch {
        this.stoppedAt = System.currentTimeMillis()
        return this
    }

    /**
     * Return elapsed time in MilliSeconds.
     */
    fun elapsedMillis(): Long {
        checkNotNull(this.startedAt) { "The stopwatch is not started yet." }
        return (this.stoppedAt ?: System.currentTimeMillis()) - this.startedAt!!
    }

    override fun toString(): String {
        return if (startedAt == null) {
            "Not started yet."
        } else {
            "${elapsedMillis()} [ms]"
        }
    }

    companion object {
        fun createUnstarted(): Stopwatch {
            return Stopwatch()
        }

        fun createStarted(): Stopwatch {
            return Stopwatch().start()
        }
    }
}
