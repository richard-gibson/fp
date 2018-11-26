package async

import arrow.effects.DeferredK
import arrow.effects.FluxK
import arrow.effects.ObservableK
import arrow.effects.deferredk.async.async
import arrow.effects.fluxk.async.async
import arrow.effects.observablek.async.async

fun main(args: Array<String>) {
    val DfkModule = CakeModule(DeferredK.async(), DeferredKSleep())
    with(DfkModule) {

        runTiming("coroutines deferredCake") { runDeferred(cakeAttempts.deferredCake()) }
        runTiming("coroutines deferredBadCake") { runDeferred(cakeAttempts.deferredBadCake()) }
        runTiming("coroutines resilientCake") { runDeferred(cakeAttempts.resilientCake()) }
        runTiming("coroutines parCake") { runDeferred(cakeAttempts.parCake()) }
    }

    val ObskModule = CakeModule(ObservableK.async(), ObservableKSleep)

    with(ObskModule) {

        runTiming("RX deferredCake") { runObsK(cakeAttempts.deferredCake()) }
        runTiming("RX deferredBadCake") { runObsK(cakeAttempts.deferredBadCake()) }
        runTiming("RX resilientCake") { runObsK(cakeAttempts.resilientCake()) }
        runTiming("RX parCake") { runObsK(cakeAttempts.parCake()) }
    }

    val FluxkModule = CakeModule(FluxK.async(), FluxKSleep)

    with(FluxkModule) {

        runTiming("Flux deferredCake") { runFlux(cakeAttempts.deferredCake()) }
        runTiming("Flux deferredBadCake") { runFlux(cakeAttempts.deferredBadCake()) }
        runTiming("Flux resilientCake") { runFlux(cakeAttempts.resilientCake()) }
        runTiming("Flux parCake") { runFlux(cakeAttempts.parCake()) }
    }



}