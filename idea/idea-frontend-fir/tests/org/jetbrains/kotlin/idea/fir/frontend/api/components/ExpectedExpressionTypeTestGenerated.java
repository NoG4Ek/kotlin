/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.fir.frontend.api.components;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link GenerateNewCompilerTests.kt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("idea/idea-frontend-fir/testData/components/expectedExpressionType")
@TestDataPath("$PROJECT_ROOT")
public class ExpectedExpressionTypeTestGenerated extends AbstractExpectedExpressionTypeTest {
    @Test
    public void testAllFilesPresentInExpectedExpressionType() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("idea/idea-frontend-fir/testData/components/expectedExpressionType"), Pattern.compile("^(.+)\\.kt$"), null, true);
    }

    @Test
    @TestMetadata("functionExpressionBody.kt")
    public void testFunctionExpressionBody() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/functionExpressionBody.kt");
    }

    @Test
    @TestMetadata("functionExpressionBodyQualified.kt")
    public void testFunctionExpressionBodyQualified() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/functionExpressionBodyQualified.kt");
    }

    @Test
    @TestMetadata("functionExpressionBodyWithTypeFromRHS.kt")
    public void testFunctionExpressionBodyWithTypeFromRHS() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/functionExpressionBodyWithTypeFromRHS.kt");
    }

    @Test
    @TestMetadata("functionExpressionBodyWithoutExplicitType.kt")
    public void testFunctionExpressionBodyWithoutExplicitType() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/functionExpressionBodyWithoutExplicitType.kt");
    }

    @Test
    @TestMetadata("functionLambdaParam.kt")
    public void testFunctionLambdaParam() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/functionLambdaParam.kt");
    }

    @Test
    @TestMetadata("functionNamedlParam.kt")
    public void testFunctionNamedlParam() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/functionNamedlParam.kt");
    }

    @Test
    @TestMetadata("functionParamWithTypeParam.kt")
    public void testFunctionParamWithTypeParam() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/functionParamWithTypeParam.kt");
    }

    @Test
    @TestMetadata("functionPositionalParam.kt")
    public void testFunctionPositionalParam() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/functionPositionalParam.kt");
    }

    @Test
    @TestMetadata("functionPositionalParamQualified.kt")
    public void testFunctionPositionalParamQualified() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/functionPositionalParamQualified.kt");
    }

    @Test
    @TestMetadata("ifCondition.kt")
    public void testIfCondition() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/ifCondition.kt");
    }

    @Test
    @TestMetadata("ifConditionQualified.kt")
    public void testIfConditionQualified() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/ifConditionQualified.kt");
    }

    @Test
    @TestMetadata("infixFunctionAsRegularCallParam.kt")
    public void testInfixFunctionAsRegularCallParam() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/infixFunctionAsRegularCallParam.kt");
    }

    @Test
    @TestMetadata("infixFunctionParam.kt")
    public void testInfixFunctionParam() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/infixFunctionParam.kt");
    }

    @Test
    @TestMetadata("infixFunctionParamQualified.kt")
    public void testInfixFunctionParamQualified() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/infixFunctionParamQualified.kt");
    }

    @Test
    @TestMetadata("propertyDeclaration.kt")
    public void testPropertyDeclaration() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/propertyDeclaration.kt");
    }

    @Test
    @TestMetadata("propertyDeclarationQualified.kt")
    public void testPropertyDeclarationQualified() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/propertyDeclarationQualified.kt");
    }

    @Test
    @TestMetadata("propertyDeclarationWithTypeFromRHS.kt")
    public void testPropertyDeclarationWithTypeFromRHS() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/propertyDeclarationWithTypeFromRHS.kt");
    }

    @Test
    @TestMetadata("propertyDeclarationWithoutExplicitType.kt")
    public void testPropertyDeclarationWithoutExplicitType() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/propertyDeclarationWithoutExplicitType.kt");
    }

    @Test
    @TestMetadata("returnFromFunction.kt")
    public void testReturnFromFunction() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/returnFromFunction.kt");
    }

    @Test
    @TestMetadata("returnFromFunctionQualifiedReceiver.kt")
    public void testReturnFromFunctionQualifiedReceiver() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/returnFromFunctionQualifiedReceiver.kt");
    }

    @Test
    @TestMetadata("returnFromFunctionQualifiedSelector.kt")
    public void testReturnFromFunctionQualifiedSelector() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/returnFromFunctionQualifiedSelector.kt");
    }

    @Test
    @TestMetadata("returnFromLambda.kt")
    public void testReturnFromLambda() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/returnFromLambda.kt");
    }

    @Test
    @TestMetadata("variableAssignment.kt")
    public void testVariableAssignment() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/variableAssignment.kt");
    }

    @Test
    @TestMetadata("variableAssignmentQualified.kt")
    public void testVariableAssignmentQualified() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/variableAssignmentQualified.kt");
    }

    @Test
    @TestMetadata("whileCondition.kt")
    public void testWhileCondition() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/whileCondition.kt");
    }

    @Test
    @TestMetadata("whileConditionQualified.kt")
    public void testWhileConditionQualified() throws Exception {
        runTest("idea/idea-frontend-fir/testData/components/expectedExpressionType/whileConditionQualified.kt");
    }
}
