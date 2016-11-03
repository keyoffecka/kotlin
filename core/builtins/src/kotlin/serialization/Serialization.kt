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

import kotlin.reflect.KClass

@Suppress("ANNOTATION_CLASS_WITH_BODY")
annotation class KSerializable(
    val value: KClass<out KSerializer<*>> = KSerializer::class // it means -- use default serializer by default
) {
    annotation class Name(val value: String)
    annotation class Tag(val value: Int)
}

interface KSerialClassDesc {
    val name: String
    val isArray: Boolean
    fun getElementCount(value: Any?): Int
    fun getElementName(index: Int): String
    fun getElementIndex(name: String): Int
}

interface KSerialSaver<in T> {
    fun save(output: KOutput, obj : T)
}

interface KSerialLoader<out T> {
    fun load(input: KInput): T
}

interface KSerializer<T>: KSerialSaver<T>, KSerialLoader<T>

// todo: it shall extend "RuntimeException"
class SerializationException(s: String) : Throwable(s)


// ========================================================================================================================


interface KOutput {
    // ------- top-level API (use it) -------

    fun <T: Any> write(saver: KSerialSaver<T>, obj : T) {
        saver.save(this, obj)
    }

    fun <T: Any> writeNullable(saver: KSerialSaver<T>, obj : T?) {
        if (obj == null) {
            writeNullValue()
        } else {
            writeNotNullMark()
            saver.save(this, obj)
        }
    }

    // ------- implementation API -------

    // it is always invoked before writeXxxValue
    fun writeElement(desc: KSerialClassDesc, index: Int) {}

    // override for a special representation of nulls if needed (empty object by default)
    fun writeNullValue(): Unit { throw SerializationException("null is not supported") }
    fun writeNotNullMark() {}

    // writes an arbitrary non-null value
    fun writeValue(value: Any) { throw SerializationException("value is not supported") }

    fun writeNullableValue(value: Any?): Unit {
        if (value == null) {
            writeNullValue()
        } else {
            writeNotNullMark()
            writeValue(value)
        }
    }

    // type-specific value-based output, override for performance and custom type representations
    fun writeBooleanValue(value: Boolean) = writeValue(value)
    fun writeByteValue(value: Byte) = writeValue(value)
    fun writeShortValue(value: Short) = writeValue(value)
    fun writeIntValue(value: Int) = writeValue(value)
    fun writeLongValue(value: Long) = writeValue(value)
    fun writeFloatValue(value: Float) = writeValue(value)
    fun writeDoubleValue(value: Double) = writeValue(value)
    fun writeCharValue(value: Char) = writeValue(value)
    fun writeStringValue(value: String) = writeValue(value)

    // recursive serialization. Override for deep recursive serialization -- replace with impl with
    //    = saver.save(this, value)
    fun <T: Any> writeSerializableValue(saver: KSerialSaver<T>, value: T) = writeNullableValue(value)

    fun <T: Any> writeNullableSerializableValue(saver: KSerialSaver<T>, value: T?) {
        if (value == null) {
            writeNullValue()
        } else {
            writeNotNullMark()
            writeSerializableValue(saver, value)
        }
    }

    // -------------------------------------------------------------------------------------
    // methods below this line are invoked by compiler-generated KSerializer implementation

    // object delimiter api (override if needed to prefix/suffix representation, do nothing by default)
    fun writeBegin(desc: KSerialClassDesc) {}
    fun writeEnd(desc: KSerialClassDesc) {}

    fun writeElementValue(desc: KSerialClassDesc, index: Int, value: Any) { writeElement(desc, index); writeValue(value) }

    fun writeNullableElementValue(desc: KSerialClassDesc, index: Int, value: Any?) { writeElement(desc, index); writeNullableValue(value) }

    fun writeBooleanElementValue(desc: KSerialClassDesc, index: Int, value: Boolean) { writeElement(desc, index); writeBooleanValue(value) }
    fun writeByteElementValue(desc: KSerialClassDesc, index: Int, value: Byte) { writeElement(desc, index); writeByteValue(value) }
    fun writeShortElementValue(desc: KSerialClassDesc, index: Int, value: Short) { writeElement(desc, index); writeShortValue(value) }
    fun writeIntElementValue(desc: KSerialClassDesc, index: Int, value: Int) { writeElement(desc, index); writeIntValue(value) }
    fun writeLongElementValue(desc: KSerialClassDesc, index: Int, value: Long) { writeElement(desc, index); writeLongValue(value) }
    fun writeFloatElementValue(desc: KSerialClassDesc, index: Int, value: Float) { writeElement(desc, index); writeFloatValue(value) }
    fun writeDoubleElementValue(desc: KSerialClassDesc, index: Int, value: Double) { writeElement(desc, index); writeDoubleValue(value) }
    fun writeCharElementValue(desc: KSerialClassDesc, index: Int, value: Char) { writeElement(desc, index); writeCharValue(value) }
    fun writeStringElementValue(desc: KSerialClassDesc, index: Int, value: String) { writeElement(desc, index); writeStringValue(value) }

    fun <T: Any> writeSerializableElementValue(desc: KSerialClassDesc, index: Int, saver: KSerialSaver<T>, value: T) {
        writeElement(desc, index)
        writeSerializableValue(saver, value)
    }

    fun <T: Any> writeNullableSerializableElementValue(desc: KSerialClassDesc, index: Int, saver: KSerialSaver<T>, value: T?) {
        writeElement(desc, index)
        writeNullableSerializableValue(saver, value)
    }
}

interface KInput {
    // ------- top-level API (use it) -------

    fun <T: Any> read(loader: KSerialLoader<T>): T = loader.load(this)

    fun <T: Any> readNullable(loader: KSerialLoader<T>): T? = if (readNotNullMark()) read(loader) else null

    // ------- implementation API -------

    // returns true if the following value is not null, false if not null (consumes null value)
    fun readNotNullMark(): Boolean = true

    fun readValue(): Any { throw SerializationException("value is not supported") }

    fun readNullableValue(): Any? = if (readNotNullMark()) readValue() else null

    // type-specific value-based input, override for performance and custom type representations
    fun readBooleanValue(): Boolean = readValue() as Boolean
    fun readByteValue(): Byte = readValue() as Byte
    fun readShortValue(): Short = readValue() as Short
    fun readIntValue(): Int = readValue() as Int
    fun readLongValue(): Long = readValue() as Long
    fun readFloatValue(): Float = readValue() as Float
    fun readDoubleValue(): Double = readValue() as Double
    fun readCharValue(): Char = readValue() as Char
    fun readStringValue(): String = readValue() as String

    // recursive serialization. Override for deep recursive serialization -- replace with impl with
    //    = loader.load(this)
    @Suppress("UNCHECKED_CAST")
    fun <T: Any> readSerializableValue(loader: KSerialLoader<T>): T = readValue() as T

    fun <T: Any> readNullableSerializableValue(loader: KSerialLoader<T>): T?
            = if(readNotNullMark()) readSerializableValue(loader) else null

    // -------------------------------------------------------------------------------------
    // methods below this line are invoked by compiler-generated KSerializer implementation

    // object delimiter api (override if needed to prefix/suffix representation)
    fun readBegin(desc: KSerialClassDesc) {}
    fun readEnd(desc: KSerialClassDesc) {}

    // readElement results
    companion object {
        const val READ_DONE = -1
        const val READ_ALL = -2
        const val READ_SIZE = -3 // special element for arrays/lists
    }

    // unordered read api, must override to read props in arbitrary order
    fun readElement(desc: KSerialClassDesc): Int = READ_ALL

    fun readElementValue(desc: KSerialClassDesc, index: Int): Any = readValue()
    fun readNullableElementValue(desc: KSerialClassDesc, index: Int): Any? = readNullableValue()

    // type-specific value-based input, override for performance and custom type representations
    fun readBooleanElementValue(desc: KSerialClassDesc, index: Int): Boolean = readBooleanValue()
    fun readByteElementValue(desc: KSerialClassDesc, index: Int): Byte = readByteValue()
    fun readShortElementValue(desc: KSerialClassDesc, index: Int): Short = readShortValue()
    fun readIntElementValue(desc: KSerialClassDesc, index: Int): Int = readIntValue()
    fun readLongElementValue(desc: KSerialClassDesc, index: Int): Long = readLongValue()
    fun readFloatElementValue(desc: KSerialClassDesc, index: Int): Float = readFloatValue()
    fun readDoubleElementValue(desc: KSerialClassDesc, index: Int): Double = readDoubleValue()
    fun readCharElementValue(desc: KSerialClassDesc, index: Int): Char = readCharValue()
    fun readStringElementValue(desc: KSerialClassDesc, index: Int): String = readStringValue()

    fun <T: Any> readSerializableElementValue(desc: KSerialClassDesc, index: Int, loader: KSerialLoader<T>): T
            = readSerializableValue(loader)

    fun <T: Any> readNullableSerializableElementValue(desc: KSerialClassDesc, index: Int, loader: KSerialLoader<T>): T?
            = readNullableSerializableValue(loader)
}

