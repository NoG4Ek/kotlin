/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin

import org.gradle.api.invocation.Gradle
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters

abstract class BuildFinishedListenerService : BuildService<BuildServiceParameters.None>, AutoCloseable {
    private val actionsOnClose = mutableListOf<() -> Unit>()

    fun onClose(action: () -> Unit) {
        actionsOnClose.add(action)
    }

    override fun close() {
        for (action in actionsOnClose) {
            action()
        }
        actionsOnClose.clear()
    }

    companion object {
        fun getInstance(gradle: Gradle): BuildFinishedListenerService {
            // Use class loader hashcode in case there are multiple class loaders in the same build
            return gradle.sharedServices
                .registerIfAbsent(
                    "build-finished-listener_${BuildFinishedListenerService::class.java.classLoader.hashCode()}",
                    BuildFinishedListenerService::class.java
                ) {}.get()
        }
    }
}