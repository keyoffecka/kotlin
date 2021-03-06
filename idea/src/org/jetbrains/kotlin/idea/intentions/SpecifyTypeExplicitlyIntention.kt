/*
 * Copyright 2010-2015 JetBrains s.r.o.
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

package org.jetbrains.kotlin.idea.intentions

import com.intellij.codeInsight.intention.LowPriorityAction
import com.intellij.codeInsight.template.*
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import org.jetbrains.kotlin.builtins.KotlinBuiltIns
import org.jetbrains.kotlin.descriptors.CallableDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.idea.caches.resolve.analyze
import org.jetbrains.kotlin.idea.caches.resolve.getResolutionFacade
import org.jetbrains.kotlin.idea.caches.resolve.resolveToDescriptorIfAny
import org.jetbrains.kotlin.idea.core.ShortenReferences
import org.jetbrains.kotlin.idea.util.IdeDescriptorRenderers
import org.jetbrains.kotlin.idea.util.application.runWriteAction
import org.jetbrains.kotlin.idea.util.getResolutionScope
import org.jetbrains.kotlin.idea.util.getResolvableApproximations
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.containingClassOrObject
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.getElementTextWithContext
import org.jetbrains.kotlin.psi.psiUtil.startOffset
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.lazy.BodyResolveMode
import org.jetbrains.kotlin.types.ErrorUtils
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.isFlexible
import org.jetbrains.kotlin.utils.ifEmpty

class SpecifyTypeExplicitlyIntention :
        SelfTargetingRangeIntention<KtCallableDeclaration>(KtCallableDeclaration::class.java, "Specify type explicitly"),
        LowPriorityAction {

    override fun applicabilityRange(element: KtCallableDeclaration): TextRange? {
        if (element.containingFile is KtCodeFragment) return null
        if (element is KtFunctionLiteral) return null // TODO: should KtFunctionLiteral be KtCallableDeclaration at all?
        if (element is KtConstructor<*>) return null
        if (element.typeReference != null) return null

        if (getTypeForDeclaration(element).isError) return null

        if (element is KtNamedFunction && element.hasBlockBody()) return null

        text = if (element is KtFunction) "Specify return type explicitly" else "Specify type explicitly"

        val initializer = (element as? KtWithExpressionInitializer)?.initializer
        return if (initializer != null) {
            TextRange(element.startOffset, initializer.startOffset - 1)
        }
        else {
            TextRange(element.startOffset, element.endOffset)
        }
    }

    override fun applyTo(element: KtCallableDeclaration, editor: Editor?) {
        val type = getTypeForDeclaration(element)
        addTypeAnnotation(editor, element, type)
    }

    companion object {

        private val PropertyDescriptor.getterType: KotlinType?
            get() = getter?.returnType?.let { if (it.isError) null else it }

        private val PropertyDescriptor.setterType: KotlinType?
            get() = setter?.valueParameters?.firstOrNull()?.type?.let { if (it.isError) null else it }

        fun dangerousFlexibleTypeOrNull(
                declaration: KtCallableDeclaration, publicAPIOnly: Boolean, reportPlatformArguments: Boolean
        ): KotlinType? {
            when (declaration) {
                is KtFunction -> if (declaration.isLocal || declaration.hasDeclaredReturnType()) return null
                is KtProperty -> if (declaration.isLocal || declaration.typeReference != null) return null
                else -> return null
            }

            if (declaration.containingClassOrObject?.isLocal() ?: false) return null

            val callable = declaration.resolveToDescriptorIfAny() as? CallableDescriptor ?: return null
            if (publicAPIOnly && !callable.visibility.isPublicAPI) return null
            val type = callable.returnType ?: return null
            if (reportPlatformArguments) {
                if (!type.isFlexibleRecursive()) return null
            }
            else {
                if (!type.isFlexible()) return null
            }
            return type
        }

        fun getTypeForDeclaration(declaration: KtCallableDeclaration): KotlinType {
            val descriptor = declaration.analyze()[BindingContext.DECLARATION_TO_DESCRIPTOR, declaration]
            val type = (descriptor as? CallableDescriptor)?.returnType
            if (type != null && type.isError && descriptor is PropertyDescriptor) {
                return descriptor.getterType ?: descriptor.setterType ?: ErrorUtils.createErrorType("null type")
            }
            return type ?: ErrorUtils.createErrorType("null type")
        }

        fun createTypeExpressionForTemplate(exprType: KotlinType, contextElement: KtElement): Expression? {
            val resolutionFacade = contextElement.getResolutionFacade()
            val bindingContext = resolutionFacade.analyze(contextElement, BodyResolveMode.PARTIAL)
            val scope = contextElement.getResolutionScope(bindingContext, resolutionFacade)
            val types = exprType.getResolvableApproximations(scope, true).toList().ifEmpty { return null }
            return object : ChooseValueExpression<KotlinType>(types, types.first()) {
                override fun getLookupString(element: KotlinType) = IdeDescriptorRenderers.SOURCE_CODE_SHORT_NAMES_IN_TYPES.renderType(element)
                override fun getResult(element: KotlinType) = IdeDescriptorRenderers.SOURCE_CODE.renderType(element)
            }
        }

        fun addTypeAnnotation(editor: Editor?, declaration: KtCallableDeclaration, exprType: KotlinType) {
            if (editor != null) {
                addTypeAnnotationWithTemplate(editor, declaration, exprType)
            }
            else {
                declaration.setType(exprType)
            }
        }

        fun createTypeReferencePostprocessor(declaration: KtCallableDeclaration): TemplateEditingAdapter {
            return object : TemplateEditingAdapter() {
                override fun templateFinished(template: Template?, brokenOff: Boolean) {
                    val typeRef = declaration.typeReference
                    if (typeRef != null && typeRef.isValid) {
                        runWriteAction { ShortenReferences.DEFAULT.process(typeRef) }
                    }
                }
            }
        }

        private fun addTypeAnnotationWithTemplate(editor: Editor, declaration: KtCallableDeclaration, exprType: KotlinType) {
            assert(!exprType.isError) { "Unexpected error type, should have been checked before: " + declaration.getElementTextWithContext() + ", type = " + exprType }

            val project = declaration.project
            val expression = createTypeExpressionForTemplate(exprType, declaration) ?: return

            declaration.setType(KotlinBuiltIns.FQ_NAMES.any.asString())

            PsiDocumentManager.getInstance(project).commitAllDocuments()
            PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(editor.document)

            val newTypeRef = declaration.typeReference!!
            val builder = TemplateBuilderImpl(newTypeRef)
            builder.replaceElement(newTypeRef, expression)

            editor.caretModel.moveToOffset(newTypeRef.node.startOffset)

            TemplateManager.getInstance(project).startTemplate(editor, builder.buildInlineTemplate(), createTypeReferencePostprocessor(declaration))
        }
    }
}

