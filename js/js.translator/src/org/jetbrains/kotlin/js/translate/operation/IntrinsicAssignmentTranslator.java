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

package org.jetbrains.kotlin.js.translate.operation;

import com.google.dart.compiler.backend.js.ast.JsBinaryOperation;
import com.google.dart.compiler.backend.js.ast.JsBinaryOperator;
import com.google.dart.compiler.backend.js.ast.JsBlock;
import com.google.dart.compiler.backend.js.ast.JsExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.js.translate.context.TranslationContext;
import org.jetbrains.kotlin.js.translate.reference.AccessTranslator;
import org.jetbrains.kotlin.lexer.KtToken;
import org.jetbrains.kotlin.psi.KtBinaryExpression;
import org.jetbrains.kotlin.psi.KtSimpleNameExpression;
import org.jetbrains.kotlin.types.expressions.OperatorConventions;

import static org.jetbrains.kotlin.js.translate.utils.PsiUtils.getOperationToken;
import static org.jetbrains.kotlin.js.translate.utils.PsiUtils.isAssignment;
import static org.jetbrains.kotlin.js.translate.utils.TranslationUtils.translateRightExpression;

public final class IntrinsicAssignmentTranslator extends AssignmentTranslator {
    private JsExpression right;
    private AccessTranslator accessTranslator;

    @NotNull
    public static JsExpression doTranslate(@NotNull KtBinaryExpression expression,
                                           @NotNull TranslationContext context) {
        return (new IntrinsicAssignmentTranslator(expression, context)).translate();
    }

    private IntrinsicAssignmentTranslator(@NotNull KtBinaryExpression expression,
                                          @NotNull TranslationContext context) {
        super(expression, context);

        JsBlock rightBlock = new JsBlock();
        right = translateRightExpression(context, expression, rightBlock);
        accessTranslator = createAccessTranslator(expression.getLeft(), !rightBlock.isEmpty());
        context.addStatementsToCurrentBlockFrom(rightBlock);
    }

    @NotNull
    private JsExpression translate() {
        if (isAssignment(getOperationToken(expression))) {
            return translateAsPlainAssignment();
        }
        return translateAsAssignmentOperation();
    }

    @NotNull
    private JsExpression translateAsAssignmentOperation() {
        if (expression.getLeft() instanceof KtSimpleNameExpression) {
            return translateAsPlainAssignmentOperation();
        }
        return translateAsAssignToCounterpart();
    }

    @NotNull
    private JsExpression translateAsAssignToCounterpart() {
        JsBinaryOperator operator = getCounterpartOperator();
        JsBinaryOperation counterpartOperation =
                new JsBinaryOperation(operator, accessTranslator.translateAsGet(), right);
        return accessTranslator.translateAsSet(counterpartOperation);
    }

    @NotNull
    private JsBinaryOperator getCounterpartOperator() {
        KtToken assignmentOperationToken = getOperationToken(expression);
        assert OperatorConventions.ASSIGNMENT_OPERATIONS.containsKey(assignmentOperationToken);
        KtToken counterpartToken = OperatorConventions.ASSIGNMENT_OPERATION_COUNTERPARTS.get(assignmentOperationToken);
        assert OperatorTable.hasCorrespondingBinaryOperator(counterpartToken) :
                "Unsupported token encountered: " + counterpartToken.toString();
        return OperatorTable.getBinaryOperator(counterpartToken);
    }

    @NotNull
    private JsExpression translateAsPlainAssignmentOperation() {
        JsBinaryOperator operator = getAssignmentOperator();
        return new JsBinaryOperation(operator, accessTranslator.translateAsGet(), right);
    }

    @NotNull
    private JsBinaryOperator getAssignmentOperator() {
        KtToken token = getOperationToken(expression);
        assert OperatorConventions.ASSIGNMENT_OPERATIONS.containsKey(token);
        assert OperatorTable.hasCorrespondingBinaryOperator(token) :
                "Unsupported token encountered: " + token.toString();
        return OperatorTable.getBinaryOperator(token);
    }

    @NotNull
    private JsExpression translateAsPlainAssignment() {
        return accessTranslator.translateAsSet(right);
    }
}
