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

@file:JvmName("SerializationCodegenUtil")

package org.jetbrains.kotlin.codegen

import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.descriptorUtil.module
import org.jetbrains.kotlin.serialization.deserialization.findClassAcrossModuleDependencies
import org.jetbrains.org.objectweb.asm.Type

private val Type.builtInSerializer: String? get() = when (this.className) {
    "java.lang.Boolean" -> "BooleanSerializer"
    "java.lang.Byte" -> "ByteSerializer"
    "java.lang.Short" -> "ShortSerializer"
    "java.lang.Integer" -> "IntSerializer"
    "java.lang.Long" -> "LongSerializer"
    "java.lang.Float" -> "FloatSerializer"
    "java.lang.Double" -> "DoubleSerializer"
    "java.lang.Character" -> "CharSerializer"
    "java.lang.String" -> "StringSerializer"
    "java.util.List" -> "ListSerializer"
    else -> null
}

private val packageFqName = FqName("kotlin.internal.serialization")

fun findBuiltInSerializer(type: Type, property: PropertyDescriptor): ClassDescriptor? {
    val name = type.builtInSerializer ?: return null
    return property.module.findClassAcrossModuleDependencies(ClassId(packageFqName, Name.identifier(name)))
}
