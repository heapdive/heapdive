/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.diagnostic.hprof.visitors

import com.intellij.diagnostic.hprof.parser.ConstantPoolEntry
import com.intellij.diagnostic.hprof.parser.HProfVisitor
import com.intellij.diagnostic.hprof.parser.HeapDumpRecordType
import com.intellij.diagnostic.hprof.parser.InstanceFieldEntry
import com.intellij.diagnostic.hprof.parser.StaticFieldEntry
import com.intellij.diagnostic.hprof.parser.Type
import com.intellij.diagnostic.hprof.util.FileBackedHashMap
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.util.function.LongUnaryOperator

abstract class RemapIDsVisitor : HProfVisitor() {
    private var currentID = 0

    override fun preVisit() {
        disableAll()
        enable(HeapDumpRecordType.ClassDump)
        enable(HeapDumpRecordType.InstanceDump)
        enable(HeapDumpRecordType.PrimitiveArrayDump)
        enable(HeapDumpRecordType.ObjectArrayDump)

        currentID = 1
    }

    override fun visitPrimitiveArrayDump(arrayObjectId: Long, stackTraceSerialNumber: Long, numberOfElements: Long, elementType: Type, primitiveArrayData: ByteBuffer) {
        addMapping(arrayObjectId, currentID++)
    }

    override fun visitClassDump(classId: Long,
                                stackTraceSerialNumber: Long,
                                superClassId: Long,
                                classloaderClassId: Long,
                                instanceSize: Long,
                                constants: Array<ConstantPoolEntry>,
                                staticFields: Array<StaticFieldEntry>,
                                instanceFields: Array<InstanceFieldEntry>) {
        addMapping(classId, currentID++)
    }

    override fun visitObjectArrayDump(arrayObjectId: Long, stackTraceSerialNumber: Long, arrayClassObjectId: Long, objects: LongArray) {
        addMapping(arrayObjectId, currentID++)
    }

    override fun visitInstanceDump(objectId: Long, stackTraceSerialNumber: Long, classObjectId: Long, bytes: ByteBuffer) {
        addMapping(objectId, currentID++)
    }

    abstract fun addMapping(oldId: Long, newId: Int)

    abstract fun getRemappingFunction(): LongUnaryOperator

    companion object {
        fun createMemoryBased(): RemapIDsVisitor {
            val map = Long2IntOpenHashMap()
            map.put(0, 0)
            return object : RemapIDsVisitor() {
                override fun addMapping(oldId: Long, newId: Int) {
                    map.put(oldId, newId)
                }

                override fun getRemappingFunction(): LongUnaryOperator {
                    return LongUnaryOperator { map.get(it).toLong() }
                }
            }
        }

        fun createFileBased(channel: FileChannel, maxInstanceCount: Long): RemapIDsVisitor {
            val remapIDsMap = FileBackedHashMap.createEmpty(
                    channel,
                    maxInstanceCount, KEY_SIZE, VALUE_SIZE)
            return object : RemapIDsVisitor() {
                override fun addMapping(oldId: Long, newId: Int) {
                    remapIDsMap.put(oldId).putInt(newId)
                }

                override fun getRemappingFunction(): LongUnaryOperator {
                    return LongUnaryOperator { operand ->
                        if (operand == 0L) 0L else {
                            if (remapIDsMap.containsKey(operand))
                                remapIDsMap[operand]!!.int.toLong()
                            else {
                                // heapdive author change this line.

                                /*
                                ChatGPT says:

                                Q. I have a heapdump file and I'm analyzing it. There are objectIDs included in ObjectArrayDump that are not listed in InstanceDump. Is such a thing possible?

                                A. Yes, it's possible. When analyzing a heap dump, there are several reasons why an `objectID` mentioned in an `ObjectArrayDump` might not have a corresponding `InstanceDump`:

                                1. **Reachable vs. Unreachable Objects**: Not all objects in the heap are necessarily reachable from the GC roots. Unreachable objects are slated for garbage collection. Depending on when the heap dump was taken and the state of the application, you might see `objectID`s without a matching `InstanceDump`.

                                2. **Partial Heap Dumps**: Depending on the tool you used to generate the heap dump, and the options you chose, it might be possible that you only have a partial dump. Some tools offer this feature to reduce the size of the dump file by only showing specific types of objects or those above a certain size threshold.

                                3. **Lazy Loading**: Some heap dump analysis tools might implement lazy loading for efficiency. In other words, they might not load the entire heap dump into memory all at once but instead load parts of it as needed. This can sometimes give the appearance of missing information until you explicitly request or search for it.

                                4. **Corruption or Incomplete Dump**: It's also possible, though less likely, that there was an error during the heap dump generation process, resulting in a corrupt or incomplete dump file. This can happen if the JVM or application crashes during the dump or if there is an issue with the dumping tool itself.

                                5. **References to Other Heap Areas**: Remember that the heap dump might contain references to objects that reside in other areas of the memory, like the stack, native heap, or even class metadata. These might not have a direct `InstanceDump` representation in the heap dump file.

                                6. **Reading Tools Discrepancies**: Different tools can interpret heap dump files differently. It's always good to verify findings with another reliable tool to ensure that what you're observing isn't a tool-specific issue.

                                To further diagnose the situation, consider using another heap dump analysis tool to cross-check your findings or regenerate the heap dump if you believe it might be corrupted or incomplete.
                                 */
//                                throw HProfMetadata.RemapException()
                                0L
                            }
                        }
                    }
                }
            }
        }

        fun isSupported(instanceCount: Long): Boolean {
            return FileBackedHashMap.isSupported(instanceCount, KEY_SIZE, VALUE_SIZE)
        }

        private const val KEY_SIZE = 8
        private const val VALUE_SIZE = 4
    }
}
