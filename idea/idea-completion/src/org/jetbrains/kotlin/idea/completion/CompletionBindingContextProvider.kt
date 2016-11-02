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

package org.jetbrains.kotlin.idea.completion

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import org.jetbrains.annotations.TestOnly
import org.jetbrains.kotlin.idea.analysis.analyzeInContext
import org.jetbrains.kotlin.idea.project.KotlinCodeBlockModificationListener
import org.jetbrains.kotlin.idea.resolve.ResolutionFacade
import org.jetbrains.kotlin.idea.util.getResolutionScope
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.psiUtil.parents
import org.jetbrains.kotlin.psi.psiUtil.parentsWithSelf
import org.jetbrains.kotlin.psi.psiUtil.siblings
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.CompositeBindingContext
import org.jetbrains.kotlin.resolve.bindingContextUtil.getDataFlowInfoBefore
import org.jetbrains.kotlin.resolve.calls.context.ContextDependency
import org.jetbrains.kotlin.resolve.calls.smartcasts.DataFlowInfo
import org.jetbrains.kotlin.resolve.lazy.BodyResolveMode
import org.jetbrains.kotlin.resolve.scopes.LexicalScope
import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstance
import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstanceOrNull
import java.lang.ref.SoftReference
import java.util.*

class CompletionBindingContextProvider(project: Project) {
    @TestOnly
    internal var TEST_LOG: StringBuilder? = null

    companion object {
        fun getInstance(project: Project): CompletionBindingContextProvider
                = project.getComponent(CompletionBindingContextProvider::class.java)
    }

    private class CompletionData(
            val inStatement: KtExpression,
            val block: KtBlockExpression,
            val prevStatement: KtExpression?,
            val psiElementsBeforeAndAfter: List<PsiElementData>,
            val bindingContext: BindingContext,
            val statementResolutionScope: LexicalScope,
            val statementDataFlowInfo: DataFlowInfo
    )

    private data class PsiElementData(val element: PsiElement, val level: Int)

    private class DataHolder {
        private var reference: SoftReference<CompletionData>? = null

        var data: CompletionData?
            get() = reference?.get()
            set(value) { reference = value?.let { SoftReference(it) } }
    }

    private var prevCompletionDataCache: CachedValue<DataHolder> = CachedValuesManager.getManager(project).createCachedValue(
            { CachedValueProvider.Result.create(DataHolder(), PsiModificationTracker.OUT_OF_CODE_BLOCK_MODIFICATION_COUNT) },
            false)


    fun getBindingContext(position: PsiElement, resolutionFacade: ResolutionFacade): BindingContext {
        assert(!position.isPhysical) // position is in synthetic file

        val inStatement = position.findStatementInBlock()
        val block = inStatement?.parent as KtBlockExpression?
        val prevStatement = inStatement?.siblings(forward = false, withItself = false)?.firstIsInstanceOrNull<KtExpression>()
        val modificationScope = inStatement?.let { KotlinCodeBlockModificationListener.getInsideCodeBlockModificationScope(it) }

        val psiElementsBeforeAndAfter = modificationScope?.let { collectPsiElementsBeforeAndAfter(modificationScope, inStatement) }

        fun doNonCachedResolve(): BindingContext {
            return resolutionFacade.analyze(position.parentsWithSelf.firstIsInstance<KtElement>(), BodyResolveMode.FULL)
        }

        val prevCompletionData = prevCompletionDataCache.value.data
        val bindingContext = if (prevCompletionData == null) {
            TEST_LOG?.append("No up-to-date data from previous completion\n")
            doNonCachedResolve()
        }
        else if (block != prevCompletionData.block) {
            TEST_LOG?.append("Not in the same block\n")
            doNonCachedResolve()
        }
        else if (prevStatement != prevCompletionData.prevStatement) {
            TEST_LOG?.append("Previous statement is not the same\n")
            doNonCachedResolve()
        }
        else if (psiElementsBeforeAndAfter != prevCompletionData.psiElementsBeforeAndAfter) {
            TEST_LOG?.append("PSI-tree has changed inside current scope\n")
            doNonCachedResolve()
        }
        else {
            TEST_LOG?.append("Statement position is the same - analyzing only one statement:\n${inStatement.text.prependIndent("    ")}\n")

            //TODO: expected type?
            val statementContext = inStatement.analyzeInContext(scope = prevCompletionData.statementResolutionScope,
                                                                contextExpression = block,
                                                                dataFlowInfo = prevCompletionData.statementDataFlowInfo,
                                                                isStatement = true,
                                                                contextDependency = ContextDependency.DEPENDENT)
            CompositeBindingContext.create(listOf(statementContext, prevCompletionData.bindingContext)) //TODO: too many CompositeBindingContext's - may be performance problem
        }

        prevCompletionDataCache.value.data = if (block != null && modificationScope != null) {
            val resolutionScope = inStatement.getResolutionScope(bindingContext, resolutionFacade)
            val dataFlowInfo = bindingContext.getDataFlowInfoBefore(inStatement)
            CompletionData(inStatement, block, prevStatement, psiElementsBeforeAndAfter!!, bindingContext, resolutionScope, dataFlowInfo)
        }
        else {
            null
        }

        return bindingContext
    }

    private fun collectPsiElementsBeforeAndAfter(scope: PsiElement, statement: KtExpression): List<PsiElementData> {
        return ArrayList<PsiElementData>().apply { addElementsInTree(scope, 0, statement) }
    }

    private fun MutableList<PsiElementData>.addElementsInTree(root: PsiElement, initialLevel: Int, skipSubtree: PsiElement) {
        if (root == skipSubtree) return
        add(PsiElementData(root, initialLevel))
        var child = root.firstChild
        while (child != null) {
            if (child !is PsiWhiteSpace && child !is PsiComment && child !is PsiErrorElement) {
                addElementsInTree(child, initialLevel + 1, skipSubtree)
            }
            child = child.nextSibling
        }
    }

    private fun PsiElement.findStatementInBlock(): KtExpression? {
        return parents.filterIsInstance<KtExpression>().firstOrNull { it.parent is KtBlockExpression }
    }
}