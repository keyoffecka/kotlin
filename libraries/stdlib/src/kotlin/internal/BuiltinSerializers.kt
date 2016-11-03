/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kotlin.internal

// todo: this does not compile yet

/*

import kotlin.serialization.KInput
import kotlin.serialization.KOutput
import kotlin.serialization.KSerialClassDesc
import kotlin.serialization.KSerializer
import kotlin.serialization.SerializationException

object BooleanSerializer : KSerializer<Boolean> {
    override fun save(output: KOutput, obj: Boolean) = output.writeBooleanValue(obj)
    override fun load(input: KInput): Boolean = input.readBooleanValue()
}

object ByteSerializer : KSerializer<Byte> {
    override fun save(output: KOutput, obj: Byte) = output.writeByteValue(obj)
    override fun load(input: KInput): Byte = input.readByteValue()
}

object ShortSerializer : KSerializer<Short> {
    override fun save(output: KOutput, obj: Short) = output.writeShortValue(obj)
    override fun load(input: KInput): Short = input.readShortValue()
}

object IntSerializer : KSerializer<Int> {
    override fun save(output: KOutput, obj: Int) = output.writeIntValue(obj)
    override fun load(input: KInput): Int = input.readIntValue()
}

object LongSerializer : KSerializer<Long> {
    override fun save(output: KOutput, obj: Long) = output.writeLongValue(obj)
    override fun load(input: KInput): Long = input.readLongValue()
}

object FloatSerializer : KSerializer<Float> {
    override fun save(output: KOutput, obj: Float) = output.writeFloatValue(obj)
    override fun load(input: KInput): Float = input.readFloatValue()
}

object DoubleSerializer : KSerializer<Double> {
    override fun save(output: KOutput, obj: Double) = output.writeDoubleValue(obj)
    override fun load(input: KInput): Double = input.readDoubleValue()
}

object CharSerializer : KSerializer<Char> {
    override fun save(output: KOutput, obj: Char) = output.writeCharValue(obj)
    override fun load(input: KInput): Char = input.readCharValue()
}

object StringSerializer : KSerializer<String> {
    override fun save(output: KOutput, obj: String) = output.writeStringValue(obj)
    override fun load(input: KInput): String =input.readStringValue()
}

class NullableSerializer<T: Any>(private val element: KSerializer<T>) : KSerializer<T?> {
    override fun save(output: KOutput, obj: T?) {
        if (obj != null) {
            output.writeNotNullMark();
            element.save(output, obj)
        } else {
            output.writeNullValue();
        }
    }

    override fun load(input: KInput): T? = if (input.readNotNullMark()) element.load(input) else null
}

const val SIZE_INDEX = KInput.READ_SIZE

class ListSerializer<T>(private val element: KSerializer<T>) : KSerializer<List<T>> {
    override fun save(output: KOutput, obj: List<T>) {
        output.writeBegin(ListClassDesc)
        val size = obj.size
        output.writeElementValue(ListClassDesc, SIZE_INDEX, size)
        for (index in 0..size - 1) {
            output.writeElement(ListClassDesc, index)
            element.save(output, obj[index])
        }
        output.writeEnd(ListClassDesc)
    }

    override fun load(input: KInput): List<T> {
        input.readBegin(ListClassDesc)
        val result = arrayListOf<T>()
        mainLoop@ while (true) {
            val index = input.readElement(ListClassDesc)
            when (index) {
                KInput.READ_ALL -> {
                    val size = input.readIntElementValue(ListClassDesc, SIZE_INDEX)
                    result.ensureCapacity(size)
                    for (i in 0..size - 1)
                        result[i] = element.load(input)
                    break@mainLoop
                }
                KInput.READ_DONE -> {
                    break@mainLoop
                }
                KInput.READ_SIZE -> {
                    val size = input.readIntElementValue(ListClassDesc, SIZE_INDEX)
                    result.ensureCapacity(size)
                }
                else -> {
                    if (result.size == index)
                        result.add(element.load(input))
                    else
                        throw SerializationException("List elements should be in order")
                }
            }

        }
        input.readEnd(ListClassDesc)
        return result
    }
}

object ListClassDesc : KSerialClassDesc {
    override val name: String
        get() = "kotlin.collections.List"

    override val isArray: Boolean
        get() = true

    override fun getElementCount(value: Any?): Int
        = (value as? List<*>)?.size ?: 0

    override fun getElementName(index: Int): String {
        return if (index == SIZE_INDEX) "size" else index.toString()
    }

    override fun getElementIndex(name: String): Int {
        return if (name == "size") SIZE_INDEX else name.toInt()
    }
}
*/
