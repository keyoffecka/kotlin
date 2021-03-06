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

package org.jetbrains.kotlin.idea.refactoring.inline;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("idea/testData/refactoring/inline")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class InlineTestGenerated extends AbstractInlineTest {
    public void testAllFilesPresentInInline() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/refactoring/inline"), Pattern.compile("^(\\w+)\\.kt$"), true);
    }

    @TestMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class InlineVariableOrProperty extends AbstractInlineTest {
        public void testAllFilesPresentInInlineVariableOrProperty() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/refactoring/inline/inlineVariableOrProperty"), Pattern.compile("^(\\w+)\\.kt$"), true);
        }

        @TestMetadata("Basic.kt")
        public void testBasic() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/Basic.kt");
            doTest(fileName);
        }

        @TestMetadata("BasicCaretOnDeclaration.kt")
        public void testBasicCaretOnDeclaration() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/BasicCaretOnDeclaration.kt");
            doTest(fileName);
        }

        @TestMetadata("ifInQualifiedExpression.kt")
        public void testIfInQualifiedExpression() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/ifInQualifiedExpression.kt");
            doTest(fileName);
        }

        @TestMetadata("InFunctionLiteral.kt")
        public void testInFunctionLiteral() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/InFunctionLiteral.kt");
            doTest(fileName);
        }

        @TestMetadata("lessAndGreaterInCallArgs.kt")
        public void testLessAndGreaterInCallArgs() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/lessAndGreaterInCallArgs.kt");
            doTest(fileName);
        }

        @TestMetadata("MethodReference.kt")
        public void testMethodReference() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/MethodReference.kt");
            doTest(fileName);
        }

        @TestMetadata("MultipleInitializers.kt")
        public void testMultipleInitializers() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/MultipleInitializers.kt");
            doTest(fileName);
        }

        @TestMetadata("MultipleUsages.kt")
        public void testMultipleUsages() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/MultipleUsages.kt");
            doTest(fileName);
        }

        @TestMetadata("noUsages.kt")
        public void testNoUsages() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/noUsages.kt");
            doTest(fileName);
        }

        @TestMetadata("Parameter.kt")
        public void testParameter() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/Parameter.kt");
            doTest(fileName);
        }

        @TestMetadata("semicolon.kt")
        public void testSemicolon() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/semicolon.kt");
            doTest(fileName);
        }

        @TestMetadata("semicolonWithSpacesAndComments.kt")
        public void testSemicolonWithSpacesAndComments() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/semicolonWithSpacesAndComments.kt");
            doTest(fileName);
        }

        @TestMetadata("SeparateInitializer.kt")
        public void testSeparateInitializer() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/SeparateInitializer.kt");
            doTest(fileName);
        }

        @TestMetadata("SeparateInitializerInTry.kt")
        public void testSeparateInitializerInTry() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/SeparateInitializerInTry.kt");
            doTest(fileName);
        }

        @TestMetadata("UsedInAssignment.kt")
        public void testUsedInAssignment() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/UsedInAssignment.kt");
            doTest(fileName);
        }

        @TestMetadata("ValWithoutInitializer.kt")
        public void testValWithoutInitializer() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/ValWithoutInitializer.kt");
            doTest(fileName);
        }

        @TestMetadata("varNoWrite.kt")
        public void testVarNoWrite() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/varNoWrite.kt");
            doTest(fileName);
        }

        @TestMetadata("varWithAssignment.kt")
        public void testVarWithAssignment() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/varWithAssignment.kt");
            doTest(fileName);
        }

        @TestMetadata("varWithInc.kt")
        public void testVarWithInc() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/varWithInc.kt");
            doTest(fileName);
        }

        @TestMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class AddParenthesis extends AbstractInlineTest {
            public void testAllFilesPresentInAddParenthesis() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis"), Pattern.compile("^(\\w+)\\.kt$"), true);
            }

            @TestMetadata("ArrayAccess.kt")
            public void testArrayAccess() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/ArrayAccess.kt");
                doTest(fileName);
            }

            @TestMetadata("ArrayAccessDontAdd.kt")
            public void testArrayAccessDontAdd() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/ArrayAccessDontAdd.kt");
                doTest(fileName);
            }

            @TestMetadata("Binary.kt")
            public void testBinary() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/Binary.kt");
                doTest(fileName);
            }

            @TestMetadata("BinaryDontAdd.kt")
            public void testBinaryDontAdd() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/BinaryDontAdd.kt");
                doTest(fileName);
            }

            @TestMetadata("Call.kt")
            public void testCall() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/Call.kt");
                doTest(fileName);
            }

            @TestMetadata("CallDontAdd.kt")
            public void testCallDontAdd() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/CallDontAdd.kt");
                doTest(fileName);
            }

            @TestMetadata("Callee.kt")
            public void testCallee() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/Callee.kt");
                doTest(fileName);
            }

            @TestMetadata("ColonDontAdd.kt")
            public void testColonDontAdd() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/ColonDontAdd.kt");
                doTest(fileName);
            }

            @TestMetadata("If.kt")
            public void testIf() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/If.kt");
                doTest(fileName);
            }

            @TestMetadata("IfIntoArrayAccess.kt")
            public void testIfIntoArrayAccess() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/IfIntoArrayAccess.kt");
                doTest(fileName);
            }

            @TestMetadata("IfIntoArrayAccessBrackets.kt")
            public void testIfIntoArrayAccessBrackets() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/IfIntoArrayAccessBrackets.kt");
                doTest(fileName);
            }

            @TestMetadata("IfIntoBinaryLeft.kt")
            public void testIfIntoBinaryLeft() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/IfIntoBinaryLeft.kt");
                doTest(fileName);
            }

            @TestMetadata("IfIntoBinaryRight.kt")
            public void testIfIntoBinaryRight() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/IfIntoBinaryRight.kt");
                doTest(fileName);
            }

            @TestMetadata("IfIntoBinaryRightLeft.kt")
            public void testIfIntoBinaryRightLeft() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/IfIntoBinaryRightLeft.kt");
                doTest(fileName);
            }

            @TestMetadata("IfIntoBinaryRightLeftDontAdd.kt")
            public void testIfIntoBinaryRightLeftDontAdd() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/IfIntoBinaryRightLeftDontAdd.kt");
                doTest(fileName);
            }

            @TestMetadata("IfIntoPostfix.kt")
            public void testIfIntoPostfix() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/IfIntoPostfix.kt");
                doTest(fileName);
            }

            @TestMetadata("IfIntoPrefix.kt")
            public void testIfIntoPrefix() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/IfIntoPrefix.kt");
                doTest(fileName);
            }

            @TestMetadata("IsDontAdd.kt")
            public void testIsDontAdd() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/IsDontAdd.kt");
                doTest(fileName);
            }

            @TestMetadata("IsIntoCall.kt")
            public void testIsIntoCall() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/IsIntoCall.kt");
                doTest(fileName);
            }

            @TestMetadata("LeftAssociative.kt")
            public void testLeftAssociative() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/LeftAssociative.kt");
                doTest(fileName);
            }

            @TestMetadata("LeftAssociativeBoolean.kt")
            public void testLeftAssociativeBoolean() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/LeftAssociativeBoolean.kt");
                doTest(fileName);
            }

            @TestMetadata("LeftAssociativeDontAdd.kt")
            public void testLeftAssociativeDontAdd() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/LeftAssociativeDontAdd.kt");
                doTest(fileName);
            }

            @TestMetadata("PostfixIntoPrefix.kt")
            public void testPostfixIntoPrefix() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/PostfixIntoPrefix.kt");
                doTest(fileName);
            }

            @TestMetadata("PrefixIntoPostfix.kt")
            public void testPrefixIntoPostfix() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/PrefixIntoPostfix.kt");
                doTest(fileName);
            }

            @TestMetadata("Qualified.kt")
            public void testQualified() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/Qualified.kt");
                doTest(fileName);
            }

            @TestMetadata("QualifiedDontAdd.kt")
            public void testQualifiedDontAdd() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/QualifiedDontAdd.kt");
                doTest(fileName);
            }

            @TestMetadata("StringTemplate.kt")
            public void testStringTemplate() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/StringTemplate.kt");
                doTest(fileName);
            }

            @TestMetadata("StringTemplateAlreadyInBraces.kt")
            public void testStringTemplateAlreadyInBraces() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/StringTemplateAlreadyInBraces.kt");
                doTest(fileName);
            }

            @TestMetadata("StringTemplateDontAdd.kt")
            public void testStringTemplateDontAdd() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/StringTemplateDontAdd.kt");
                doTest(fileName);
            }

            @TestMetadata("UnaryIntoBinary.kt")
            public void testUnaryIntoBinary() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/addParenthesis/UnaryIntoBinary.kt");
                doTest(fileName);
            }
        }

        @TestMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateParameterTypes")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class ExplicateParameterTypes extends AbstractInlineTest {
            public void testAllFilesPresentInExplicateParameterTypes() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateParameterTypes"), Pattern.compile("^(\\w+)\\.kt$"), true);
            }

            @TestMetadata("EnoughDontExplicate.kt")
            public void testEnoughDontExplicate() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateParameterTypes/EnoughDontExplicate.kt");
                doTest(fileName);
            }

            @TestMetadata("ErrorTypes.kt")
            public void testErrorTypes() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateParameterTypes/ErrorTypes.kt");
                doTest(fileName);
            }

            @TestMetadata("It.kt")
            public void testIt() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateParameterTypes/It.kt");
                doTest(fileName);
            }

            @TestMetadata("ItMultiLine.kt")
            public void testItMultiLine() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateParameterTypes/ItMultiLine.kt");
                doTest(fileName);
            }

            @TestMetadata("Parenthesized.kt")
            public void testParenthesized() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateParameterTypes/Parenthesized.kt");
                doTest(fileName);
            }

            @TestMetadata("Simplest.kt")
            public void testSimplest() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateParameterTypes/Simplest.kt");
                doTest(fileName);
            }

            @TestMetadata("TrivialDontExplicate.kt")
            public void testTrivialDontExplicate() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateParameterTypes/TrivialDontExplicate.kt");
                doTest(fileName);
            }
        }

        @TestMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateTypeArgument")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class ExplicateTypeArgument extends AbstractInlineTest {
            public void testAllFilesPresentInExplicateTypeArgument() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateTypeArgument"), Pattern.compile("^(\\w+)\\.kt$"), true);
            }

            @TestMetadata("DeeperNestedCall.kt")
            public void testDeeperNestedCall() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateTypeArgument/DeeperNestedCall.kt");
                doTest(fileName);
            }

            @TestMetadata("EnoughDontExplicate.kt")
            public void testEnoughDontExplicate() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateTypeArgument/EnoughDontExplicate.kt");
                doTest(fileName);
            }

            @TestMetadata("ExplicateForSome.kt")
            public void testExplicateForSome() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateTypeArgument/ExplicateForSome.kt");
                doTest(fileName);
            }

            @TestMetadata("InStringTemplate.kt")
            public void testInStringTemplate() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateTypeArgument/InStringTemplate.kt");
                doTest(fileName);
            }

            @TestMetadata("NestedCall.kt")
            public void testNestedCall() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateTypeArgument/NestedCall.kt");
                doTest(fileName);
            }

            @TestMetadata("Parenthesized.kt")
            public void testParenthesized() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateTypeArgument/Parenthesized.kt");
                doTest(fileName);
            }

            @TestMetadata("Qualified.kt")
            public void testQualified() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateTypeArgument/Qualified.kt");
                doTest(fileName);
            }

            @TestMetadata("Simplest.kt")
            public void testSimplest() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateTypeArgument/Simplest.kt");
                doTest(fileName);
            }

            @TestMetadata("TrivialDontExplicate.kt")
            public void testTrivialDontExplicate() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/explicateTypeArgument/TrivialDontExplicate.kt");
                doTest(fileName);
            }
        }

        @TestMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/property")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class Property extends AbstractInlineTest {
            public void testAllFilesPresentInProperty() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/refactoring/inline/inlineVariableOrProperty/property"), Pattern.compile("^(\\w+)\\.kt$"), true);
            }

            @TestMetadata("Basic.kt")
            public void testBasic() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/property/Basic.kt");
                doTest(fileName);
            }

            @TestMetadata("ClassObjectProperty.kt")
            public void testClassObjectProperty() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/property/ClassObjectProperty.kt");
                doTest(fileName);
            }

            @TestMetadata("ExtensionProperty.kt")
            public void testExtensionProperty() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/property/ExtensionProperty.kt");
                doTest(fileName);
            }

            @TestMetadata("InstanceProperty.kt")
            public void testInstanceProperty() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/property/InstanceProperty.kt");
                doTest(fileName);
            }

            @TestMetadata("keepImport.kt")
            public void testKeepImport() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/property/keepImport.kt");
                doTest(fileName);
            }

            @TestMetadata("multiplePackages.kt")
            public void testMultiplePackages() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/property/multiplePackages.kt");
                doTest(fileName);
            }

            @TestMetadata("ObjectProperty.kt")
            public void testObjectProperty() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/property/ObjectProperty.kt");
                doTest(fileName);
            }

            @TestMetadata("QualifiedUsage.kt")
            public void testQualifiedUsage() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/property/QualifiedUsage.kt");
                doTest(fileName);
            }

            @TestMetadata("removeImport.kt")
            public void testRemoveImport() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/property/removeImport.kt");
                doTest(fileName);
            }

            @TestMetadata("WithGetter.kt")
            public void testWithGetter() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/property/WithGetter.kt");
                doTest(fileName);
            }

            @TestMetadata("WithInitializerAndGetter.kt")
            public void testWithInitializerAndGetter() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/property/WithInitializerAndGetter.kt");
                doTest(fileName);
            }
        }

        @TestMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/stringTemplates")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class StringTemplates extends AbstractInlineTest {
            @TestMetadata("addBraces.kt")
            public void testAddBraces() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/stringTemplates/addBraces.kt");
                doTest(fileName);
            }

            public void testAllFilesPresentInStringTemplates() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/refactoring/inline/inlineVariableOrProperty/stringTemplates"), Pattern.compile("^(\\w+)\\.kt$"), true);
            }

            @TestMetadata("blockEntry.kt")
            public void testBlockEntry() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/stringTemplates/blockEntry.kt");
                doTest(fileName);
            }

            @TestMetadata("empty.kt")
            public void testEmpty() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/stringTemplates/empty.kt");
                doTest(fileName);
            }

            @TestMetadata("nonEmpty.kt")
            public void testNonEmpty() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/stringTemplates/nonEmpty.kt");
                doTest(fileName);
            }

            @TestMetadata("nonRawToRaw.kt")
            public void testNonRawToRaw() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/stringTemplates/nonRawToRaw.kt");
                doTest(fileName);
            }

            @TestMetadata("rawString.kt")
            public void testRawString() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/stringTemplates/rawString.kt");
                doTest(fileName);
            }

            @TestMetadata("rawToNonRaw.kt")
            public void testRawToNonRaw() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("idea/testData/refactoring/inline/inlineVariableOrProperty/stringTemplates/rawToNonRaw.kt");
                doTest(fileName);
            }
        }
    }
}
