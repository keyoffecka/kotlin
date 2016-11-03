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

package kotlin.serialization

open class NamedValueOutput : KOutput {
    // ------- API (override it) -------

    open fun writeNamed(name: String, value: Any) { throw SerializationException("value is not supported for $name") }
    open fun writeNamedNull(name: String) { throw SerializationException("null is not supported for $name") }

    open fun writeNamedNullable(name: String, value: Any?) {
        if (value == null) writeNamedNull(name) else writeNamed(name, value)
    }

    open fun writeNamedBoolean(name: String, value: Boolean) = writeNamed(name, value)
    open fun writeNamedByte(name: String, value: Byte) = writeNamed(name, value)
    open fun writeNamedShort(name: String, value: Short) = writeNamed(name, value)
    open fun writeNamedInt(name: String, value: Int) = writeNamed(name, value)
    open fun writeNamedLong(name: String, value: Long) = writeNamed(name, value)
    open fun writeNamedFloat(name: String, value: Float) = writeNamed(name, value)
    open fun writeNamedDouble(name: String, value: Double) = writeNamed(name, value)
    open fun writeNamedChar(name: String, value: Char) = writeNamed(name, value)
    open fun writeNamedString(name: String, value: String) = writeNamed(name, value)

    open fun <T: Any> writeNamedSerializable(name: String, saver: KSerialSaver<T>, value: T)
            = writeNamed(name, value)

    open fun <T: Any> writeNamedNullableSerializable(name: String, saver: KSerialSaver<T>, value: T?)
            = if (value == null) writeNamedNull(name) else writeNamedSerializable(name, saver, value)

    // ------- implementation -------

    private var lastDesc: KSerialClassDesc? = null
    private var lastIndex: Int = -1

    override fun writeElement(desc: KSerialClassDesc, index: Int) {
        lastDesc = desc
        lastIndex = index
    }

    override fun writeValue(value: Any) { writeElementValue(lastDesc!!, lastIndex, value) }

    override fun writeNullableValue(value: Any?) { writeNullableElementValue(lastDesc!!, lastIndex, value) }

    override fun writeBooleanValue(value: Boolean) { writeNullableElementValue(lastDesc!!, lastIndex, value) }
    override fun writeByteValue(value: Byte) { writeNullableElementValue(lastDesc!!, lastIndex, value) }
    override fun writeShortValue(value: Short) { writeNullableElementValue(lastDesc!!, lastIndex, value) }
    override fun writeIntValue(value: Int) { writeNullableElementValue(lastDesc!!, lastIndex, value) }
    override fun writeLongValue(value: Long) { writeNullableElementValue(lastDesc!!, lastIndex, value) }
    override fun writeFloatValue(value: Float) { writeNullableElementValue(lastDesc!!, lastIndex, value) }
    override fun writeDoubleValue(value: Double) { writeNullableElementValue(lastDesc!!, lastIndex, value) }
    override fun writeCharValue(value: Char) { writeNullableElementValue(lastDesc!!, lastIndex, value) }
    override fun writeStringValue(value: String) { writeNullableElementValue(lastDesc!!, lastIndex, value) }

    override fun <T: Any> writeSerializableValue(saver: KSerialSaver<T>, value: T) {
        writeSerializableElementValue(lastDesc!!, lastIndex, saver, value)
    }

    override fun <T: Any> writeNullableSerializableValue(saver: KSerialSaver<T>, value: T?) {
        writeNullableSerializableElementValue(lastDesc!!, lastIndex, saver, value)
    }

    override fun writeNullValue() {
        writeNullableElementValue(lastDesc!!, lastIndex, null)
    }

    // ---------------

    open fun name(desc: KSerialClassDesc, index: Int) = desc.getElementName(index)

    override fun writeElementValue(desc: KSerialClassDesc, index: Int, value: Any) = writeNamed(name(desc, index), value)
    override fun writeNullableElementValue(desc: KSerialClassDesc, index: Int, value: Any?) = writeNamedNullable(name(desc, index), value)
    override fun writeBooleanElementValue(desc: KSerialClassDesc, index: Int, value: Boolean) = writeNamedBoolean(name(desc, index), value)
    override fun writeByteElementValue(desc: KSerialClassDesc, index: Int, value: Byte) = writeNamedByte(name(desc, index), value)
    override fun writeShortElementValue(desc: KSerialClassDesc, index: Int, value: Short) = writeNamedShort(name(desc, index), value)
    override fun writeIntElementValue(desc: KSerialClassDesc, index: Int, value: Int) = writeNamedInt(name(desc, index), value)
    override fun writeLongElementValue(desc: KSerialClassDesc, index: Int, value: Long) = writeNamedLong(name(desc, index), value)
    override fun writeFloatElementValue(desc: KSerialClassDesc, index: Int, value: Float) = writeNamedFloat(name(desc, index), value)
    override fun writeDoubleElementValue(desc: KSerialClassDesc, index: Int, value: Double) = writeNamedDouble(name(desc, index), value)
    override fun writeCharElementValue(desc: KSerialClassDesc, index: Int, value: Char) = writeNamedChar(name(desc, index), value)
    override fun writeStringElementValue(desc: KSerialClassDesc, index: Int, value: String) = writeNamedString(name(desc, index), value)

    override fun <T: Any> writeSerializableElementValue(desc: KSerialClassDesc, index: Int, saver: KSerialSaver<T>, value: T)
            = writeNamedSerializable(name(desc, index), saver, value)

    override fun <T: Any> writeNullableSerializableElementValue(desc: KSerialClassDesc, index: Int, saver: KSerialSaver<T>, value: T?)
            = writeNamedNullableSerializable(name(desc, index), saver, value)
}

open class NamedValueInput : KInput {
    // ------- API (override it) -------

    open fun readNamed(name: String): Any { throw SerializationException("value is not supported for $name") }
    open fun readNamedNotNullMark(name: String): Boolean = true

    open fun readNamedNullable(name: String): Any? = if (readNamedNotNullMark(name)) readNamed(name) else null

    open fun readNamedBoolean(name: String): Boolean = readNamed(name) as Boolean
    open fun readNamedByte(name: String): Byte = readNamed(name) as Byte
    open fun readNamedShort(name: String): Short = readNamed(name) as Short
    open fun readNamedInt(name: String): Int = readNamed(name) as Int
    open fun readNamedLong(name: String): Long = readNamed(name) as Long
    open fun readNamedFloat(name: String): Float = readNamed(name) as Float
    open fun readNamedDouble(name: String): Double = readNamed(name) as Double
    open fun readNamedChar(name: String): Char = readNamed(name) as Char
    open fun readNamedString(name: String): String = readNamed(name) as String

    @Suppress("UNCHECKED_CAST")
    open fun <T: Any> readNamedSerializable(name: String, loader: KSerialLoader<T>): T
            = readNamed(name) as T

    open fun <T: Any> readNamedNullableSerializable(name: String, loader: KSerialLoader<T>): T?
            = if (readNamedNotNullMark(name)) readNamedSerializable(name, loader) else null

    // ------- implementation -------
    
    private var lastDesc: KSerialClassDesc? = null
    private var lastIndex: Int = -1

    override fun readNotNullMark(): Boolean = true

    override fun readValue(): Any = readElementValue(lastDesc!!, lastIndex)

    override fun readNullableValue(): Any? = readNullableElementValue(lastDesc!!, lastIndex)

    override fun readBooleanValue(): Boolean = readBooleanElementValue(lastDesc!!, lastIndex)
    override fun readByteValue(): Byte = readByteElementValue(lastDesc!!, lastIndex)
    override fun readShortValue(): Short = readShortElementValue(lastDesc!!, lastIndex)
    override fun readIntValue(): Int = readIntElementValue(lastDesc!!, lastIndex)
    override fun readLongValue(): Long = readLongElementValue(lastDesc!!, lastIndex)
    override fun readFloatValue(): Float = readFloatElementValue(lastDesc!!, lastIndex)
    override fun readDoubleValue(): Double = readDoubleElementValue(lastDesc!!, lastIndex)
    override fun readCharValue(): Char = readCharElementValue(lastDesc!!, lastIndex)
    override fun readStringValue(): String = readStringElementValue(lastDesc!!, lastIndex)

    override fun <T: Any> readSerializableValue(loader: KSerialLoader<T>): T 
            = readSerializableElementValue(lastDesc!!, lastIndex, loader)

    override fun <T: Any> readNullableSerializableValue(loader: KSerialLoader<T>): T?
            = readNullableSerializableElementValue(lastDesc!!, lastIndex, loader)

    // ---------------

    open fun name(desc: KSerialClassDesc, index: Int) = desc.getElementName(index)

    override fun readElementValue(desc: KSerialClassDesc, index: Int): Any = readNamed(name(desc, index))
    override fun readNullableElementValue(desc: KSerialClassDesc, index: Int): Any? = readNamedNullable(name(desc, index))
    override fun readBooleanElementValue(desc: KSerialClassDesc, index: Int): Boolean = readNamedBoolean(name(desc, index))
    override fun readByteElementValue(desc: KSerialClassDesc, index: Int): Byte = readNamedByte(name(desc, index))
    override fun readShortElementValue(desc: KSerialClassDesc, index: Int): Short = readNamedShort(name(desc, index))
    override fun readIntElementValue(desc: KSerialClassDesc, index: Int): Int = readNamedInt(name(desc, index))
    override fun readLongElementValue(desc: KSerialClassDesc, index: Int): Long = readNamedLong(name(desc, index))
    override fun readFloatElementValue(desc: KSerialClassDesc, index: Int): Float = readNamedFloat(name(desc, index))
    override fun readDoubleElementValue(desc: KSerialClassDesc, index: Int): Double = readNamedDouble(name(desc, index))
    override fun readCharElementValue(desc: KSerialClassDesc, index: Int): Char = readNamedChar(name(desc, index))
    override fun readStringElementValue(desc: KSerialClassDesc, index: Int): String = readNamedString(name(desc, index))

    override fun <T: Any> readSerializableElementValue(desc: KSerialClassDesc, index: Int, loader: KSerialLoader<T>): T
            = readNamedSerializable(name(desc, index), loader)

    override fun <T: Any> readNullableSerializableElementValue(desc: KSerialClassDesc, index: Int, loader: KSerialLoader<T>): T?
            = readNamedNullableSerializable(name(desc, index), loader)
}