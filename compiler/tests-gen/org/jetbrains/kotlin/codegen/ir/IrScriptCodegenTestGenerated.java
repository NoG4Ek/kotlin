/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.codegen.ir;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("compiler/testData/codegen/script")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class IrScriptCodegenTestGenerated extends AbstractIrScriptCodegenTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doTest, TargetBackend.JVM_IR, testDataFilePath);
    }

    @TestMetadata("adder.kts")
    public void testAdder() throws Exception {
        runTest("compiler/testData/codegen/script/adder.kts");
    }

    public void testAllFilesPresentInScript() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/codegen/script"), Pattern.compile("^(.+)\\.kts$"), null, TargetBackend.JVM_IR, true);
    }

    @TestMetadata("classLiteralInsideFunction.kts")
    public void testClassLiteralInsideFunction() throws Exception {
        runTest("compiler/testData/codegen/script/classLiteralInsideFunction.kts");
    }

    @TestMetadata("destructuringDeclaration.kts")
    public void testDestructuringDeclaration() throws Exception {
        runTest("compiler/testData/codegen/script/destructuringDeclaration.kts");
    }

    @TestMetadata("destructuringDeclarationUnderscore.kts")
    public void testDestructuringDeclarationUnderscore() throws Exception {
        runTest("compiler/testData/codegen/script/destructuringDeclarationUnderscore.kts");
    }

    @TestMetadata("empty.kts")
    public void testEmpty() throws Exception {
        runTest("compiler/testData/codegen/script/empty.kts");
    }

    @TestMetadata("helloWorld.kts")
    public void testHelloWorld() throws Exception {
        runTest("compiler/testData/codegen/script/helloWorld.kts");
    }

    @TestMetadata("inline.kts")
    public void testInline() throws Exception {
        runTest("compiler/testData/codegen/script/inline.kts");
    }

    @TestMetadata("kt20707.kts")
    public void testKt20707() throws Exception {
        runTest("compiler/testData/codegen/script/kt20707.kts");
    }

    @TestMetadata("kt22029.kts")
    public void testKt22029() throws Exception {
        runTest("compiler/testData/codegen/script/kt22029.kts");
    }

    @TestMetadata("localDelegatedProperty.kts")
    public void testLocalDelegatedProperty() throws Exception {
        runTest("compiler/testData/codegen/script/localDelegatedProperty.kts");
    }

    @TestMetadata("localDelegatedPropertyNoExplicitType.kts")
    public void testLocalDelegatedPropertyNoExplicitType() throws Exception {
        runTest("compiler/testData/codegen/script/localDelegatedPropertyNoExplicitType.kts");
    }

    @TestMetadata("localFunction.kts")
    public void testLocalFunction() throws Exception {
        runTest("compiler/testData/codegen/script/localFunction.kts");
    }

    @TestMetadata("outerCapture.kts")
    public void testOuterCapture() throws Exception {
        runTest("compiler/testData/codegen/script/outerCapture.kts");
    }

    @TestMetadata("parameter.kts")
    public void testParameter() throws Exception {
        runTest("compiler/testData/codegen/script/parameter.kts");
    }

    @TestMetadata("parameterArray.kts")
    public void testParameterArray() throws Exception {
        runTest("compiler/testData/codegen/script/parameterArray.kts");
    }

    @TestMetadata("parameterClosure.kts")
    public void testParameterClosure() throws Exception {
        runTest("compiler/testData/codegen/script/parameterClosure.kts");
    }

    @TestMetadata("parameterLong.kts")
    public void testParameterLong() throws Exception {
        runTest("compiler/testData/codegen/script/parameterLong.kts");
    }

    @TestMetadata("secondLevelFunction.kts")
    public void testSecondLevelFunction() throws Exception {
        runTest("compiler/testData/codegen/script/secondLevelFunction.kts");
    }

    @TestMetadata("secondLevelFunctionClosure.kts")
    public void testSecondLevelFunctionClosure() throws Exception {
        runTest("compiler/testData/codegen/script/secondLevelFunctionClosure.kts");
    }

    @TestMetadata("secondLevelVal.kts")
    public void testSecondLevelVal() throws Exception {
        runTest("compiler/testData/codegen/script/secondLevelVal.kts");
    }

    @TestMetadata("simpleClass.kts")
    public void testSimpleClass() throws Exception {
        runTest("compiler/testData/codegen/script/simpleClass.kts");
    }

    @TestMetadata("string.kts")
    public void testString() throws Exception {
        runTest("compiler/testData/codegen/script/string.kts");
    }

    @TestMetadata("topLevelFunction.kts")
    public void testTopLevelFunction() throws Exception {
        runTest("compiler/testData/codegen/script/topLevelFunction.kts");
    }

    @TestMetadata("topLevelFunctionClosure.kts")
    public void testTopLevelFunctionClosure() throws Exception {
        runTest("compiler/testData/codegen/script/topLevelFunctionClosure.kts");
    }

    @TestMetadata("topLevelLocalDelegatedProperty.kts")
    public void testTopLevelLocalDelegatedProperty() throws Exception {
        runTest("compiler/testData/codegen/script/topLevelLocalDelegatedProperty.kts");
    }

    @TestMetadata("topLevelPropertiesWithGetSet.kts")
    public void testTopLevelPropertiesWithGetSet() throws Exception {
        runTest("compiler/testData/codegen/script/topLevelPropertiesWithGetSet.kts");
    }

    @TestMetadata("topLevelProperty.kts")
    public void testTopLevelProperty() throws Exception {
        runTest("compiler/testData/codegen/script/topLevelProperty.kts");
    }

    @TestMetadata("topLevelPropertyWithProvideDelegate.kts")
    public void testTopLevelPropertyWithProvideDelegate() throws Exception {
        runTest("compiler/testData/codegen/script/topLevelPropertyWithProvideDelegate.kts");
    }

    @TestMetadata("topLevelTypealias.kts")
    public void testTopLevelTypealias() throws Exception {
        runTest("compiler/testData/codegen/script/topLevelTypealias.kts");
    }
}
