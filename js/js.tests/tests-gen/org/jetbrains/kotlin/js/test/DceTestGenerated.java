/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.js.test;

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
@TestMetadata("js/js.translator/testData/dce")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class DceTestGenerated extends AbstractDceTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest0(this::doTest, TargetBackend.JS, testDataFilePath);
    }

    public void testAllFilesPresentInDce() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("js/js.translator/testData/dce"), Pattern.compile("(.+)\\.js"), null, TargetBackend.JS, true);
    }

    @TestMetadata("amd.js")
    public void testAmd() throws Exception {
        runTest("js/js.translator/testData/dce/amd.js");
    }

    @TestMetadata("arrayAccess.js")
    public void testArrayAccess() throws Exception {
        runTest("js/js.translator/testData/dce/arrayAccess.js");
    }

    @TestMetadata("commonjs.js")
    public void testCommonjs() throws Exception {
        runTest("js/js.translator/testData/dce/commonjs.js");
    }

    @TestMetadata("cycle.js")
    public void testCycle() throws Exception {
        runTest("js/js.translator/testData/dce/cycle.js");
    }

    @TestMetadata("localVarAndFunction.js")
    public void testLocalVarAndFunction() throws Exception {
        runTest("js/js.translator/testData/dce/localVarAndFunction.js");
    }

    @TestMetadata("typeOf.js")
    public void testTypeOf() throws Exception {
        runTest("js/js.translator/testData/dce/typeOf.js");
    }
}
