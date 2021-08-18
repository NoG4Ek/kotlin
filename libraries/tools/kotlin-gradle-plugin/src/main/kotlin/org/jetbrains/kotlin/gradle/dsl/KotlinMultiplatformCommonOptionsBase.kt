// DO NOT EDIT MANUALLY!
// Generated by org/jetbrains/kotlin/generators/arguments/GenerateGradleOptions.kt
package org.jetbrains.kotlin.gradle.dsl

internal abstract class KotlinMultiplatformCommonOptionsBase : org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformCommonOptions {

    private var allWarningsAsErrorsField: kotlin.Boolean? = null
    override var allWarningsAsErrors: kotlin.Boolean
        get() = allWarningsAsErrorsField ?: false
        set(value) {
            allWarningsAsErrorsField = value
        }

    private var suppressWarningsField: kotlin.Boolean? = null
    override var suppressWarnings: kotlin.Boolean
        get() = suppressWarningsField ?: false
        set(value) {
            suppressWarningsField = value
        }

    private var verboseField: kotlin.Boolean? = null
    override var verbose: kotlin.Boolean
        get() = verboseField ?: false
        set(value) {
            verboseField = value
        }

    override var apiVersion: kotlin.String? = null

    override var languageVersion: kotlin.String? = null

    private var useFirField: kotlin.Boolean? = null
    override var useFir: kotlin.Boolean
        get() = useFirField ?: false
        set(value) {
            useFirField = value
        }

    internal open fun updateArguments(args: org.jetbrains.kotlin.cli.common.arguments.K2MetadataCompilerArguments) {
        allWarningsAsErrorsField?.let { args.allWarningsAsErrors = it }
        suppressWarningsField?.let { args.suppressWarnings = it }
        verboseField?.let { args.verbose = it }
        apiVersion?.let { args.apiVersion = it }
        languageVersion?.let { args.languageVersion = it }
        useFirField?.let { args.useFir = it }
    }
}

internal fun org.jetbrains.kotlin.cli.common.arguments.K2MetadataCompilerArguments.fillDefaultValues() {
    allWarningsAsErrors = false
    suppressWarnings = false
    verbose = false
    apiVersion = null
    languageVersion = null
    useFir = false
}
