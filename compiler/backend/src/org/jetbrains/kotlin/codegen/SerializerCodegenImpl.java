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

package org.jetbrains.kotlin.codegen;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.backend.common.SerializerCodegen;
import org.jetbrains.kotlin.builtins.KotlinBuiltIns;
import org.jetbrains.kotlin.codegen.context.MethodContext;
import org.jetbrains.kotlin.descriptors.ClassDescriptor;
import org.jetbrains.kotlin.descriptors.FunctionDescriptor;
import org.jetbrains.kotlin.descriptors.KSerializationUtil;
import org.jetbrains.kotlin.descriptors.PropertyDescriptor;
import org.jetbrains.kotlin.fir.FirClassOrObject;
import org.jetbrains.kotlin.resolve.BindingContext;
import org.jetbrains.kotlin.resolve.jvm.diagnostics.JvmDeclarationOriginKt;
import org.jetbrains.kotlin.resolve.jvm.jvmSignature.JvmMethodSignature;
import org.jetbrains.org.objectweb.asm.Label;
import org.jetbrains.org.objectweb.asm.MethodVisitor;
import org.jetbrains.org.objectweb.asm.Type;
import org.jetbrains.org.objectweb.asm.commons.InstructionAdapter;

import java.util.List;

class SerializerCodegenImpl extends SerializerCodegen {
    private final ImplementationBodyCodegen codegen;
    private final Type descType = Type.getObjectType("kotlin/serialization/KSerialClassDesc");
    private final Type descImplType = Type.getObjectType("kotlin/internal/SerialClassDescImpl");
    private final Type serialSaverType = Type.getObjectType("kotlin/serialization/KSerialSaver");
    private final Type serialLoaderType = Type.getObjectType("kotlin/serialization/KSerialLoader");

    SerializerCodegenImpl(
            ImplementationBodyCodegen codegen,
            FirClassOrObject klass,
            BindingContext bindingContext
    ) {
        super(klass, bindingContext);
        this.codegen = codegen;
    }

    static void generateSerializerFunctions(ImplementationBodyCodegen codegen) {
        if (!KSerializationUtil.isSerializerCompanion(codegen.descriptor)) return;
        new SerializerCodegenImpl(codegen, codegen.myClass, codegen.bindingContext).generate();
    }

    // todo: store serial desc to static field
    private void generateSerialClassDesc(
            InstructionAdapter iv, int classDescVar,
            List<? extends PropertyDescriptor> properties
    ) {
        iv.anew(descImplType);
        iv.dup();
        iv.aconst(getSerialName());
        iv.invokespecial(descImplType.getInternalName(), "<init>", "(Ljava/lang/String;)V", false);
        iv.store(classDescVar, descImplType);
        for (PropertyDescriptor property : properties) {
            iv.load(classDescVar, descImplType);
            iv.aconst(property.getName().asString());
            iv.invokevirtual(descImplType.getInternalName(), "addElement", "(Ljava/lang/String;)V", false);
        }
    }

    @Override
    protected void generateCompanionSave(
            @NotNull FunctionDescriptor function, @NotNull final List<? extends PropertyDescriptor> properties
    ) {
        codegen.functionCodegen
                .generateMethod(JvmDeclarationOriginKt.OtherOriginFir(codegen.myClass, function), function, new FunctionGenerationStrategy() {
                    @Override
                    public void generateBody(
                            @NotNull MethodVisitor mv,
                            @NotNull FrameMap frameMap,
                            @NotNull JvmMethodSignature signature,
                            @NotNull MethodContext context,
                            @NotNull MemberCodegen<?> parentCodegen
                    ) {
                        // fun save(output: KOutput, obj : T)
                        int outputVar = 1;
                        int objVar = 2;
                        int descVar = 3;
                        InstructionAdapter iv = new InstructionAdapter(mv);
                        generateSerialClassDesc(iv, descVar, properties);
                        Type outputType = signature.getValueParameters().get(0).getAsmType();
                        Type objType = signature.getValueParameters().get(1).getAsmType();
                        // output.writeBegin(classDesc)
                        iv.load(outputVar, outputType);
                        iv.load(descVar, descType);
                        iv.invokeinterface(outputType.getInternalName(), "writeBegin",
                                           "(" + descType.getDescriptor() + ")V");
                        // loop for all properties
                        for (int index = 0; index < properties.size(); index++) {
                            PropertyDescriptor property = properties.get(index);
                            // output.writeXxxElementValue(classDesc, index, value)
                            iv.load(outputVar, outputType);
                            iv.load(descVar, descType);
                            iv.iconst(index);
                            Type propertyType = codegen.typeMapper.mapType(property.getType());
                            SerializationMethodTypeMapper
                                    m = new SerializationMethodTypeMapper(propertyType, property);
                            stackValueSerializerInstance(m.serializer, serialSaverType, iv);
                            codegen.genPropertyOnStack(iv, context, property, objType, objVar);
                            iv.invokeinterface(outputType.getInternalName(), "write" + m.namePart + "ElementValue",
                                               "(" + descType.getDescriptor() + "I" +
                                               (m.serializer != null ? serialSaverType.getDescriptor() : "") +
                                               m.type.getDescriptor() + ")V");
                        }
                        // output.writeEnd(classDesc)
                        iv.load(outputVar, outputType);
                        iv.load(descVar, descType);
                        iv.invokeinterface(outputType.getInternalName(), "writeEnd",
                                           "(" + descType.getDescriptor() + ")V");
                        // return
                        iv.areturn(Type.VOID_TYPE);
                    }
                });
    }

    @Override
    protected void generateCompanionLoad(
            @NotNull FunctionDescriptor function, @NotNull final List<? extends PropertyDescriptor> properties
    ) {
        codegen.functionCodegen
                .generateMethod(JvmDeclarationOriginKt.OtherOriginFir(codegen.myClass, function), function, new FunctionGenerationStrategy() {
                    @Override
                    public void generateBody(
                            @NotNull MethodVisitor mv,
                            @NotNull FrameMap frameMap,
                            @NotNull JvmMethodSignature signature,
                            @NotNull MethodContext context,
                            @NotNull MemberCodegen<?> parentCodegen
                    ) {
                        // fun load(input: KInput): T
                        int inputVar = 1;
                        int descVar = 2;
                        int indexVar = 3;
                        int readAllVar = 4;
                        int propsStartVar = 5;
                        InstructionAdapter iv = new InstructionAdapter(mv);
                        generateSerialClassDesc(iv, descVar, properties);
                        Type objType = signature.getReturnType();
                        Type inputType = signature.getValueParameters().get(0).getAsmType();
                        // boolean readAll = false
                        iv.iconst(0);
                        iv.store(readAllVar, Type.BOOLEAN_TYPE);
                        // initialize all prop vars
                        int propVar = propsStartVar;
                        for (PropertyDescriptor property : properties) {
                            Type propertyType = codegen.typeMapper.mapType(property.getType());
                            stackValueDefault(propertyType, iv);
                            iv.store(propVar, propertyType);
                            propVar += propertyType.getSize();
                        }
                        // input.readBegin(classDesc)
                        iv.load(inputVar, inputType);
                        iv.load(descVar, descType);
                        iv.invokeinterface(inputType.getInternalName(), "readBegin",
                                           "(" + descType.getDescriptor() + ")V");
                        // readElement: int index = input.readElement(classDesc)
                        Label readElementLabel = new Label();
                        iv.visitLabel(readElementLabel);
                        iv.load(inputVar, inputType);
                        iv.load(descVar, descType);
                        iv.invokeinterface(inputType.getInternalName(), "readElement",
                                           "(" + descType.getDescriptor() + ")I");
                        iv.store(indexVar, Type.INT_TYPE);
                        // switch(index)
                        Label readAllLabel = new Label();
                        Label readEndLabel = new Label();
                        Label[] labels = new Label[properties.size() + 2];
                        labels[0] = readAllLabel; // READ_ALL
                        labels[1] = readEndLabel; // READ_DONE
                        for (int i = 0; i < properties.size(); i++) {
                            labels[i + 2] = new Label();
                        }
                        iv.load(indexVar, Type.INT_TYPE);
                        // todo: readEnd is currently default, should probably throw exception instead
                        iv.tableswitch(-2, properties.size() - 1, readEndLabel, labels);
                        // readAll: readAll := true
                        iv.visitLabel(readAllLabel);
                        iv.iconst(1);
                        iv.store(readAllVar, Type.BOOLEAN_TYPE);
                        // loop for all properties
                        propVar = propsStartVar;
                        for (int i = 0; i < properties.size(); i++) {
                            PropertyDescriptor property = properties.get(i);
                            // labelI: propX := input.readXxxValue(value)
                            iv.visitLabel(labels[i + 2]);
                            iv.load(inputVar, inputType);
                            iv.load(descVar, descType);
                            iv.iconst(i);
                            Type propertyType = codegen.typeMapper.mapType(property.getType());
                            SerializationMethodTypeMapper
                                    m = new SerializationMethodTypeMapper(propertyType, property);
                            stackValueSerializerInstance(m.serializer, serialLoaderType, iv);
                            iv.invokeinterface(inputType.getInternalName(), "read" + m.namePart + "ElementValue",
                                               "(" + descType.getDescriptor() + "I" +
                                               (m.serializer != null ? serialLoaderType.getDescriptor() : "")
                                               + ")" + m.type.getDescriptor());
                            StackValue.coerce(m.type, propertyType, iv);
                            iv.store(propVar, propertyType);
                            propVar += propertyType.getSize();
                            // if (readAll == false) goto readElement
                            iv.load(readAllVar, Type.BOOLEAN_TYPE);
                            iv.iconst(0);
                            iv.ificmpeq(readElementLabel);
                        }
                        // readEnd: input.readEnd(classDesc)
                        iv.visitLabel(readEndLabel);
                        iv.load(inputVar, inputType);
                        iv.load(descVar, descType);
                        iv.invokeinterface(inputType.getInternalName(), "readEnd",
                                           "(" + descType.getDescriptor() + ")V");
                        // create object
                        iv.anew(objType);
                        iv.dup();
                        StringBuilder constructorDesc = new StringBuilder("(");
                        propVar = propsStartVar;
                        for (PropertyDescriptor property : properties) {
                            Type propertyType = codegen.typeMapper.mapType(property.getType());
                            constructorDesc.append(propertyType.getDescriptor());
                            iv.load(propVar, propertyType);
                            propVar += propertyType.getSize();
                        }
                        constructorDesc.append(")V");
                        iv.invokespecial(objType.getInternalName(), "<init>", constructorDesc.toString(), false);
                        // return
                        iv.areturn(objType);
                    }
                });
    }

    // todo: move to StackValue?
    private void stackValueDefault(Type type, InstructionAdapter iv) {
        switch (type.getSort()) {
            case Type.BOOLEAN:
            case Type.BYTE:
            case Type.SHORT:
            case Type.CHAR:
            case Type.INT:
                iv.iconst(0);
                break;
            case Type.LONG:
                iv.lconst(0);
                break;
            case Type.FLOAT:
                iv.fconst(0);
                break;
            case Type.DOUBLE:
                iv.dconst(0);
                break;
            default:
                iv.aconst(null);
                break;
        }
    }

    private void stackValueSerializerInstance(ClassDescriptor serializer, Type type, InstructionAdapter iv) {
        if (serializer == null) {
            return;
        }
        //todo: handle cases when serializer is not a singleton
        StackValue.singleton(serializer, codegen.typeMapper).put(type, iv);
    }

    static class SerializationMethodTypeMapper {
        final String namePart;
        final Type type;
        ClassDescriptor serializer;

        public SerializationMethodTypeMapper(Type type, PropertyDescriptor property) {
            switch (type.getSort()) {
                case Type.BOOLEAN:
                case Type.BYTE:
                case Type.SHORT:
                case Type.INT:
                case Type.LONG:
                case Type.FLOAT:
                case Type.DOUBLE:
                case Type.CHAR:
                    String name = type.getClassName();
                    namePart = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                    this.type = type;
                    break;
                case Type.OBJECT:
                    // check for explicit serialization annotation on this property
                    serializer = KSerializationUtil.toClassDescriptor(KSerializationUtil.getPropertySerializer(property));
                    if (serializer == null) {
                        // no explicit serializer for this property. Is it string?
                        if (KotlinBuiltIns.isString(property.getType())) {
                            namePart = "String";
                            this.type = Type.getType("Ljava/lang/String;");
                            return;
                        }
                        // check for serializer defined on the class
                        serializer = KSerializationUtil.toClassDescriptor(KSerializationUtil.getClassSerializer(property.getType()));
                        // if serializer for class is not defined, then see if there is a builtin serializer
                        if (serializer == null)
                            serializer = SerializationCodegenUtil.findStandardSerializer(type, property);
                    }
                    // other classes
                    namePart = (property.getType().isMarkedNullable() ? "Nullable" : "") +
                            (serializer != null ? "Serializable" : "");
                    this.type = Type.getType("Ljava/lang/Object;");
                    break;
                default:
                    throw new AssertionError(); // should not happen
            }

        }
    }
}
