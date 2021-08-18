/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.js.test.compatibility.binary;

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
@TestMetadata("compiler/testData/binaryCompatibility/klibEvolution")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class JsKlibBinaryCompatibilityTestGenerated extends AbstractJsKlibBinaryCompatibilityTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest0(this::doTest, TargetBackend.JS_IR, testDataFilePath);
    }

    @TestMetadata("addAbstractMemberBody.kt")
    public void testAddAbstractMemberBody() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/addAbstractMemberBody.kt");
    }

    @TestMetadata("addCompanionObject.kt")
    public void testAddCompanionObject() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/addCompanionObject.kt");
    }

    @TestMetadata("addDefaultImplementations.kt")
    public void testAddDefaultImplementations() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/addDefaultImplementations.kt");
    }

    @TestMetadata("addEnumClassMember.kt")
    public void testAddEnumClassMember() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/addEnumClassMember.kt");
    }

    @TestMetadata("addLateinitToVar.kt")
    public void testAddLateinitToVar() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/addLateinitToVar.kt");
    }

    @TestMetadata("addOpenToClass.kt")
    public void testAddOpenToClass() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/addOpenToClass.kt");
    }

    @TestMetadata("addOpenToMember.kt")
    public void testAddOpenToMember() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/addOpenToMember.kt");
    }

    @TestMetadata("addOrRemoveConst.kt")
    public void testAddOrRemoveConst() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/addOrRemoveConst.kt");
    }

    @TestMetadata("addOrRemoveInitBlock.kt")
    public void testAddOrRemoveInitBlock() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/addOrRemoveInitBlock.kt");
    }

    @TestMetadata("addOverloads.kt")
    public void testAddOverloads() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/addOverloads.kt");
    }

    @TestMetadata("addParameterDefaulValue.kt")
    public void testAddParameterDefaulValue() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/addParameterDefaulValue.kt");
    }

    @TestMetadata("addProeprtyAccessor.kt")
    public void testAddProeprtyAccessor() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/addProeprtyAccessor.kt");
    }

    @TestMetadata("addingSealedClassMember.kt")
    public void testAddingSealedClassMember() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/addingSealedClassMember.kt");
    }

    public void testAllFilesPresentInKlibEvolution() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/binaryCompatibility/klibEvolution"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JS_IR, true);
    }

    @TestMetadata("changeBaseClassOrder.kt")
    public void testChangeBaseClassOrder() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/changeBaseClassOrder.kt");
    }

    @TestMetadata("changeCompanionToNestedObject.kt")
    public void testChangeCompanionToNestedObject() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/changeCompanionToNestedObject.kt");
    }

    @TestMetadata("changeConstInitialization.kt")
    public void testChangeConstInitialization() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/changeConstInitialization.kt");
    }

    @TestMetadata("changeNamesOfTypeParameters.kt")
    public void testChangeNamesOfTypeParameters() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/changeNamesOfTypeParameters.kt");
    }

    @TestMetadata("changeObjectToCompanion.kt")
    public void testChangeObjectToCompanion() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/changeObjectToCompanion.kt");
    }

    @TestMetadata("changeParameterDefaultValue.kt")
    public void testChangeParameterDefaultValue() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/changeParameterDefaultValue.kt");
    }

    @TestMetadata("changePropertyFromValToVar.kt")
    public void testChangePropertyFromValToVar() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/changePropertyFromValToVar.kt");
    }

    @TestMetadata("changePropertyInitialization.kt")
    public void testChangePropertyInitialization() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/changePropertyInitialization.kt");
    }

    @TestMetadata("constructorParameterMarkValVar.kt")
    public void testConstructorParameterMarkValVar() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/constructorParameterMarkValVar.kt");
    }

    @TestMetadata("deleteOverrideMember.kt")
    public void testDeleteOverrideMember() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/deleteOverrideMember.kt");
    }

    @TestMetadata("deletePrivateMembers.kt")
    public void testDeletePrivateMembers() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/deletePrivateMembers.kt");
    }

    @TestMetadata("inlineBodyChange.kt")
    public void testInlineBodyChange() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/inlineBodyChange.kt");
    }

    @TestMetadata("inlineFunction.kt")
    public void testInlineFunction() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/inlineFunction.kt");
    }

    @TestMetadata("makeFunctionInfixOrTailrec.kt")
    public void testMakeFunctionInfixOrTailrec() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/makeFunctionInfixOrTailrec.kt");
    }

    @TestMetadata("moreSpecificBaseClass.kt")
    public void testMoreSpecificBaseClass() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/moreSpecificBaseClass.kt");
    }

    @TestMetadata("moveMemberUpInHierarchy.kt")
    public void testMoveMemberUpInHierarchy() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/moveMemberUpInHierarchy.kt");
    }

    @TestMetadata("newFakeOverride.kt")
    public void testNewFakeOverride() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/newFakeOverride.kt");
    }

    @TestMetadata("newOverrideMember.kt")
    public void testNewOverrideMember() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/newOverrideMember.kt");
    }

    @TestMetadata("removeAbstractFromClass.kt")
    public void testRemoveAbstractFromClass() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/removeAbstractFromClass.kt");
    }

    @TestMetadata("removeInfixOrTailrecFromFunction.kt")
    public void testRemoveInfixOrTailrecFromFunction() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/removeInfixOrTailrecFromFunction.kt");
    }

    @TestMetadata("removeLateinitFromVar.kt")
    public void testRemoveLateinitFromVar() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/removeLateinitFromVar.kt");
    }

    @TestMetadata("removePropertyAccessor.kt")
    public void testRemovePropertyAccessor() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/removePropertyAccessor.kt");
    }

    @TestMetadata("renameArguments.kt")
    public void testRenameArguments() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/renameArguments.kt");
    }

    @TestMetadata("reorderClassConstructors.kt")
    public void testReorderClassConstructors() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/reorderClassConstructors.kt");
    }

    @TestMetadata("turnClassIntoDataClass.kt")
    public void testTurnClassIntoDataClass() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/turnClassIntoDataClass.kt");
    }

    @TestMetadata("widenSuperMemberVisibility.kt")
    public void testWidenSuperMemberVisibility() throws Exception {
        runTest("compiler/testData/binaryCompatibility/klibEvolution/widenSuperMemberVisibility.kt");
    }
}
