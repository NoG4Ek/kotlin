/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.parcelize.test;

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
@TestMetadata("plugins/parcelize/parcelize-compiler/testData/box")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class ParcelizeIrBoxTestGenerated extends AbstractParcelizeIrBoxTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doTest, TargetBackend.JVM_IR, testDataFilePath);
    }

    public void testAllFilesPresentInBox() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/parcelize/parcelize-compiler/testData/box"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
    }

    @TestMetadata("allPrimitiveTypes.kt")
    public void testAllPrimitiveTypes() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/allPrimitiveTypes.kt");
    }

    @TestMetadata("arraySimple.kt")
    public void testArraySimple() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/arraySimple.kt");
    }

    @TestMetadata("arrays.kt")
    public void testArrays() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/arrays.kt");
    }

    @TestMetadata("binder.kt")
    public void testBinder() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/binder.kt");
    }

    @TestMetadata("boxedTypes.kt")
    public void testBoxedTypes() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/boxedTypes.kt");
    }

    @TestMetadata("bundle.kt")
    public void testBundle() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/bundle.kt");
    }

    @TestMetadata("charSequence.kt")
    public void testCharSequence() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/charSequence.kt");
    }

    @TestMetadata("customNewArray.kt")
    public void testCustomNewArray() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/customNewArray.kt");
    }

    @TestMetadata("customParcelable.kt")
    public void testCustomParcelable() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/customParcelable.kt");
    }

    @TestMetadata("customParcelerScoping.kt")
    public void testCustomParcelerScoping() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/customParcelerScoping.kt");
    }

    @TestMetadata("customSerializerBoxing.kt")
    public void testCustomSerializerBoxing() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/customSerializerBoxing.kt");
    }

    @TestMetadata("customSerializerSimple.kt")
    public void testCustomSerializerSimple() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/customSerializerSimple.kt");
    }

    @TestMetadata("customSerializerWriteWith.kt")
    public void testCustomSerializerWriteWith() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/customSerializerWriteWith.kt");
    }

    @TestMetadata("customSimple.kt")
    public void testCustomSimple() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/customSimple.kt");
    }

    @TestMetadata("enumObject.kt")
    public void testEnumObject() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/enumObject.kt");
    }

    @TestMetadata("enums.kt")
    public void testEnums() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/enums.kt");
    }

    @TestMetadata("exceptions.kt")
    public void testExceptions() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/exceptions.kt");
    }

    @TestMetadata("functions.kt")
    public void testFunctions() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/functions.kt");
    }

    @TestMetadata("generics.kt")
    public void testGenerics() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/generics.kt");
    }

    @TestMetadata("intArray.kt")
    public void testIntArray() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/intArray.kt");
    }

    @TestMetadata("javaInterop.kt")
    public void testJavaInterop() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/javaInterop.kt");
    }

    @TestMetadata("kt19747.kt")
    public void testKt19747() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt19747.kt");
    }

    @TestMetadata("kt19747Deprecated.kt")
    public void testKt19747Deprecated() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt19747Deprecated.kt");
    }

    @TestMetadata("kt19747_2.kt")
    public void testKt19747_2() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt19747_2.kt");
    }

    @TestMetadata("kt19749.kt")
    public void testKt19749() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt19749.kt");
    }

    @TestMetadata("kt20002.kt")
    public void testKt20002() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt20002.kt");
    }

    @TestMetadata("kt20021.kt")
    public void testKt20021() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt20021.kt");
    }

    @TestMetadata("kt20717.kt")
    public void testKt20717() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt20717.kt");
    }

    @TestMetadata("kt25839.kt")
    public void testKt25839() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt25839.kt");
    }

    @TestMetadata("kt26221.kt")
    public void testKt26221() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt26221.kt");
    }

    @TestMetadata("kt36658.kt")
    public void testKt36658() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt36658.kt");
    }

    @TestMetadata("kt39981.kt")
    public void testKt39981() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt39981.kt");
    }

    @TestMetadata("kt41553.kt")
    public void testKt41553() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt41553.kt");
    }

    @TestMetadata("kt41553_2.kt")
    public void testKt41553_2() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt41553_2.kt");
    }

    @TestMetadata("kt46567.kt")
    public void testKt46567() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt46567.kt");
    }

    @TestMetadata("listKinds.kt")
    public void testListKinds() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/listKinds.kt");
    }

    @TestMetadata("listSimple.kt")
    public void testListSimple() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/listSimple.kt");
    }

    @TestMetadata("lists.kt")
    public void testLists() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/lists.kt");
    }

    @TestMetadata("mapKinds.kt")
    public void testMapKinds() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/mapKinds.kt");
    }

    @TestMetadata("mapSimple.kt")
    public void testMapSimple() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/mapSimple.kt");
    }

    @TestMetadata("maps.kt")
    public void testMaps() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/maps.kt");
    }

    @TestMetadata("nestedArrays.kt")
    public void testNestedArrays() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/nestedArrays.kt");
    }

    @TestMetadata("nestedLists.kt")
    public void testNestedLists() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/nestedLists.kt");
    }

    @TestMetadata("nestedMaps.kt")
    public void testNestedMaps() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/nestedMaps.kt");
    }

    @TestMetadata("nestedParcelable.kt")
    public void testNestedParcelable() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/nestedParcelable.kt");
    }

    @TestMetadata("nestedSparseArrays.kt")
    public void testNestedSparseArrays() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/nestedSparseArrays.kt");
    }

    @TestMetadata("newArray.kt")
    public void testNewArray() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/newArray.kt");
    }

    @TestMetadata("newArrayParceler.kt")
    public void testNewArrayParceler() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/newArrayParceler.kt");
    }

    @TestMetadata("nullableTypes.kt")
    public void testNullableTypes() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/nullableTypes.kt");
    }

    @TestMetadata("nullableTypesSimple.kt")
    public void testNullableTypesSimple() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/nullableTypesSimple.kt");
    }

    @TestMetadata("objects.kt")
    public void testObjects() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/objects.kt");
    }

    @TestMetadata("openParcelize.kt")
    public void testOpenParcelize() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/openParcelize.kt");
    }

    @TestMetadata("persistableBundle.kt")
    public void testPersistableBundle() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/persistableBundle.kt");
    }

    @TestMetadata("primitiveTypes.kt")
    public void testPrimitiveTypes() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/primitiveTypes.kt");
    }

    @TestMetadata("sealedClass.kt")
    public void testSealedClass() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/sealedClass.kt");
    }

    @TestMetadata("simple.kt")
    public void testSimple() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/simple.kt");
    }

    @TestMetadata("simpleDeprecated.kt")
    public void testSimpleDeprecated() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/simpleDeprecated.kt");
    }

    @TestMetadata("sparseArrays.kt")
    public void testSparseArrays() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/sparseArrays.kt");
    }

    @TestMetadata("sparseBooleanArray.kt")
    public void testSparseBooleanArray() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/sparseBooleanArray.kt");
    }
}
