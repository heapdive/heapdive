// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.util.lang

import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.nio.ByteBuffer
import kotlin.concurrent.Volatile

// we need to unmap buffer immediately without waiting until GC does this job; otherwise further modifications of the created file
// will fail with AccessDeniedException
object ByteBufferCleaner {
    @Volatile
    private var cleaner: MethodHandle? = null

    fun unmapBuffer(buffer: ByteBuffer) {
        if (!buffer.isDirect) {
            return
        }
        var cleaner = cleaner
        try {
            if (cleaner == null) {
                cleaner = byteBufferCleaner
            }
            cleaner.invokeExact(buffer)
        } catch (e: Exception) {
            throw e
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    @get:Synchronized
    private val byteBufferCleaner: MethodHandle
        get() {
            var cleaner = cleaner
            if (cleaner != null) {
                return cleaner
            }
            val unsafeClass = ClassLoader.getPlatformClassLoader().loadClass("sun.misc.Unsafe")
            val lookup = MethodHandles.privateLookupIn(unsafeClass, MethodHandles.lookup())
            val unsafe = lookup.findStaticGetter(unsafeClass, "theUnsafe", unsafeClass).invoke()
            cleaner = lookup.findVirtual(unsafeClass, "invokeCleaner", MethodType.methodType(Void.TYPE, ByteBuffer::class.java)).bindTo(unsafe)
            ByteBufferCleaner.cleaner = cleaner
            return cleaner
        }
}
