// DO NOT EDIT MANUALLY!
// Generated by org/jetbrains/kotlin/generators/arguments/GenerateGradleOptions.kt
package org.jetbrains.kotlin.gradle.dsl

interface KotlinJvmOptions  : org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions {

    /**
     * Include Kotlin runtime into the resulting JAR
     * Default value: false
     */
    @Deprecated(message = "This option has no effect and will be removed in a future release.", level = DeprecationLevel.ERROR)
     var includeRuntime: kotlin.Boolean

    /**
     * Generate metadata for Java 1.8 reflection on method parameters
     * Default value: false
     */
     var javaParameters: kotlin.Boolean

    /**
     * Include a custom JDK from the specified location into the classpath instead of the default JAVA_HOME
     * Default value: null
     */
    @Deprecated(message = "This option is not working well with Gradle caching and will be removed in the future.", level = DeprecationLevel.WARNING)
     var jdkHome: kotlin.String?

    /**
     * Target version of the generated JVM bytecode (1.6 (DEPRECATED), 1.8, 9, 10, 11, 12, 13, 14, 15 or 16), default is 1.8
     * Possible values: "1.6", "1.8", "9", "10", "11", "12", "13", "14", "15", "16"
     * Default value: "1.8"
     */
     var jvmTarget: kotlin.String

    /**
     * Name of the generated .kotlin_module file
     * Default value: null
     */
     var moduleName: kotlin.String?

    /**
     * Don't automatically include the Java runtime into the classpath
     * Default value: false
     */
     var noJdk: kotlin.Boolean

    /**
     * Don't automatically include Kotlin reflection into the classpath
     * Default value: true
     */
    @Deprecated(message = "This option has no effect and will be removed in a future release.", level = DeprecationLevel.ERROR)
     var noReflect: kotlin.Boolean

    /**
     * Don't automatically include the Kotlin/JVM stdlib and Kotlin reflection into the classpath
     * Default value: true
     */
    @Deprecated(message = "This option has no effect and will be removed in a future release.", level = DeprecationLevel.ERROR)
     var noStdlib: kotlin.Boolean

    /**
     * Use the IR backend. This option has no effect unless the language version less than 1.5 is used
     * Default value: false
     */
    @Deprecated(message = "This option has no effect and will be removed in a future release.", level = DeprecationLevel.WARNING)
     var useIR: kotlin.Boolean

    /**
     * Use the old JVM backend
     * Default value: false
     */
     var useOldBackend: kotlin.Boolean
}
