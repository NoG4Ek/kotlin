/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.frontend.api.fir.components

import org.jetbrains.kotlin.fir.declarations.FirCallableDeclaration
import org.jetbrains.kotlin.fir.declarations.FirFile
import org.jetbrains.kotlin.fir.declarations.FirResolvePhase
import org.jetbrains.kotlin.fir.declarations.FirVariable
import org.jetbrains.kotlin.fir.expressions.FirExpression
import org.jetbrains.kotlin.fir.resolve.calls.ImplicitReceiverValue
import org.jetbrains.kotlin.fir.resolve.inference.receiverType
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.idea.fir.low.level.api.api.LowLevelFirApiFacadeForResolveOnAir.getTowerContextProvider
import org.jetbrains.kotlin.idea.fir.low.level.api.api.getOrBuildFirFile
import org.jetbrains.kotlin.idea.fir.low.level.api.api.getOrBuildFirOfType
import org.jetbrains.kotlin.idea.fir.low.level.api.resolver.ResolutionParameters
import org.jetbrains.kotlin.idea.fir.low.level.api.resolver.SingleCandidateResolutionMode
import org.jetbrains.kotlin.idea.fir.low.level.api.resolver.SingleCandidateResolver
import org.jetbrains.kotlin.idea.fir.low.level.api.util.getElementTextInContext
import org.jetbrains.kotlin.idea.frontend.api.components.KtCompletionCandidateChecker
import org.jetbrains.kotlin.idea.frontend.api.components.KtExtensionApplicabilityResult
import org.jetbrains.kotlin.idea.frontend.api.fir.KtFirAnalysisSession
import org.jetbrains.kotlin.idea.frontend.api.fir.symbols.KtFirSymbol
import org.jetbrains.kotlin.idea.frontend.api.fir.utils.weakRef
import org.jetbrains.kotlin.idea.frontend.api.symbols.KtCallableSymbol
import org.jetbrains.kotlin.idea.frontend.api.tokens.ValidityToken
import org.jetbrains.kotlin.idea.frontend.api.withValidityAssertion
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtSimpleNameExpression

internal class KtFirCompletionCandidateChecker(
    analysisSession: KtFirAnalysisSession,
    override val token: ValidityToken,
) : KtCompletionCandidateChecker(), KtFirAnalysisSessionComponent {
    override val analysisSession: KtFirAnalysisSession by weakRef(analysisSession)

    override fun checkExtensionFitsCandidate(
        firSymbolForCandidate: KtCallableSymbol,
        originalFile: KtFile,
        nameExpression: KtSimpleNameExpression,
        possibleExplicitReceiver: KtExpression?,
    ): KtExtensionApplicabilityResult = withValidityAssertion {
        require(firSymbolForCandidate is KtFirSymbol<*>)
        return firSymbolForCandidate.firRef.withFir(
            phase = FirResolvePhase.IMPLICIT_TYPES_BODY_RESOLVE
        ) { declaration ->
            check(declaration is FirCallableDeclaration)
            checkExtension(declaration, originalFile, nameExpression, possibleExplicitReceiver)
        }
    }

    private fun checkExtension(
        candidateSymbol: FirCallableDeclaration,
        originalFile: KtFile,
        nameExpression: KtSimpleNameExpression,
        possibleExplicitReceiver: KtExpression?,
    ): KtExtensionApplicabilityResult {
        val file = originalFile.getOrBuildFirFile(firResolveState)
        val explicitReceiverExpression = possibleExplicitReceiver?.getOrBuildFirOfType<FirExpression>(firResolveState)
        val resolver = SingleCandidateResolver(firResolveState.rootModuleSession, file)
        val implicitReceivers = getImplicitReceivers(originalFile, file, nameExpression)
        for (implicitReceiverValue in implicitReceivers) {
            val resolutionParameters = ResolutionParameters(
                singleCandidateResolutionMode = SingleCandidateResolutionMode.CHECK_EXTENSION_FOR_COMPLETION,
                callableSymbol = candidateSymbol.symbol,
                implicitReceiver = implicitReceiverValue,
                explicitReceiver = explicitReceiverExpression
            )
            resolver.resolveSingleCandidate(resolutionParameters)?.let {
                return when {
                    candidateSymbol is FirVariable && candidateSymbol.returnTypeRef.coneType.receiverType(rootModuleSession) != null -> {
                        KtExtensionApplicabilityResult.ApplicableAsFunctionalVariableCall
                    }
                    else -> {
                        KtExtensionApplicabilityResult.ApplicableAsExtensionCallable
                    }
                }
            }
        }
        return KtExtensionApplicabilityResult.NonApplicable
    }

    private fun getImplicitReceivers(
        originalFile: KtFile,
        firFile: FirFile,
        fakeNameExpression: KtSimpleNameExpression
    ): Sequence<ImplicitReceiverValue<*>?> {
        val towerDataContext = analysisSession.firResolveState.getTowerContextProvider()
            .getClosestAvailableParentContext(fakeNameExpression)
            ?: error("Cannot find enclosing declaration for ${fakeNameExpression.getElementTextInContext()}")

        return sequence {
            yield(null) // otherwise explicit receiver won't be checked when there are no implicit receivers in completion position
            yieldAll(towerDataContext.implicitReceiverStack)
        }
    }
}
