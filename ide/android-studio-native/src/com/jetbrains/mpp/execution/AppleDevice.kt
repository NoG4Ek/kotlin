/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package com.jetbrains.mpp.execution

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import com.jetbrains.cidr.execution.CidrCommandLineState
import com.jetbrains.cidr.execution.CidrConsoleBuilder
import com.jetbrains.cidr.execution.deviceSupport.AMDevice
import com.jetbrains.cidr.execution.deviceSupport.AMDeviceUtil
import com.jetbrains.cidr.execution.simulatorSupport.SimulatorConfiguration
import com.jetbrains.cidr.execution.testing.CidrLauncher
import com.jetbrains.cidr.xcode.frameworks.buildSystem.ArchitectureValue
import com.jetbrains.mpp.AppleRunConfiguration
import java.io.File

abstract class AppleDevice(id: String, name: String, osVersion: String) : Device(id, name, "iOS", osVersion) {
    override fun createState(configuration: AppleRunConfiguration, environment: ExecutionEnvironment): CidrCommandLineState =
        CidrCommandLineState(environment, createLauncher(configuration, environment)).also {
            it.consoleBuilder = CidrConsoleBuilder(configuration.project, null, configuration.project.basePath?.let { File(it) })
        }

    protected abstract fun createLauncher(configuration: AppleRunConfiguration, environment: ExecutionEnvironment): CidrLauncher

    abstract fun createCommandLine(bundle: File, project: Project): GeneralCommandLine
    override fun canRun(configuration: RunConfiguration): Boolean =
        configuration is AppleRunConfiguration

    abstract val arch: ArchitectureValue
}

class ApplePhysicalDevice(private val raw: AMDevice) : AppleDevice(
    raw.deviceIdentifier,
    raw.name,
    raw.productVersion ?: "Unknown"
) {
    override fun createLauncher(configuration: AppleRunConfiguration, environment: ExecutionEnvironment): CidrLauncher =
        ApplePhysicalDeviceLauncher(configuration, environment, this, raw)

    override fun createCommandLine(bundle: File, project: Project): GeneralCommandLine {
        val commandLine = GeneralCommandLine()
        AMDeviceUtil.installApplicationInBackgroundAndAcquireDebugInfo(raw, true, bundle, project, commandLine)
        commandLine.exePath += "/" + FileUtil.getNameWithoutExtension(bundle)
        return commandLine
    }

    override val arch: ArchitectureValue
        get() = ArchitectureValue(raw.cpuArchitecture!!)
}


class AppleSimulator(private val raw: SimulatorConfiguration) : AppleDevice(
    raw.udid,
    raw.name,
    raw.version
) {
    override fun createLauncher(configuration: AppleRunConfiguration, environment: ExecutionEnvironment): CidrLauncher =
        AppleSimulatorLauncher(configuration, environment, this, raw)

    override fun createCommandLine(bundle: File, project: Project): GeneralCommandLine =
        GeneralCommandLine(bundle.absolutePath)

    override val arch: ArchitectureValue
        get() = raw.launchArchitecture
}