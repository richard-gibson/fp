package async

import arrow.Kind
import arrow.core.Try
import arrow.core.Tuple3
import arrow.data.k
import arrow.effects.*
import arrow.effects.deferredk.async.async
import arrow.effects.fluxk.async.async
import arrow.effects.observablek.async.async
import arrow.effects.typeclasses.Async
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.concurrent.TimeUnit


interface Sleep<F> {
    fun sleep(milis: Long): Kind<F, Unit>
}


class AsyncCakeService<F>(val A: Async<F>, S: Sleep<F>): Async<F> by A, Sleep<F> by S {

    fun retrieveFlour(weight: Int): Kind<F, Flour> =
            delay {
                println("going to get some flour")
                sleep(500)
                println("flour retrieved")
                Flour(weight)
            }

    fun retrieveButter(weight: Int): Kind<F, Butter> =
            delay {
                println("going to get some butter")
                sleep(400)
                println("butter retrieved")
                Butter(weight)
            }

    fun retrieveBadButter(weight: Int): Kind<F, Butter> =
            raiseError(BadButterException("found some bad butter"))

    fun retrieveEgg(size: EggSize): Kind<F, Egg> =
            delay {
                println("going to a $size egg")
                sleep(300)
                println("egg retrieved")
                Egg(size)
            }

    fun retrieveEggs(eggSize: EggSize, noOfEggs: Int): Kind<F, List<Egg>> =
            (1..noOfEggs).toList().k().traverse(A) { retrieveEgg(eggSize)}

    fun mixCake(flour: Flour, butter: Butter, eggs: List<Egg>): Kind<F, Mixture> =
            delay {
                println("going to mix ingredients")
                sleep(400)
                println("cake baked")
                Mixture(flour, butter, eggs)
            }

    fun bakeCake(mixture: Mixture): Kind<F, Cake> =
            delay {
                println("going to bake a cake")
                val (flour, butter, eggs) = mixture
                sleep(400)
                println("cake baked")
                Cake(flour, butter, eggs)
            }
}




class AsyncCakeAttempts<F>(val A: Async<F>, val cakeService: AsyncCakeService<F>): Async<F> by A {

    fun deferredBadCake(): Kind<F, Cake> = binding {
        val flour = cakeService.retrieveFlour(100).bind()
        val butter = cakeService.retrieveBadButter(50).bind()
        val eggs = cakeService.retrieveEggs(EggSize.large, 3).bind()
        val mixture = cakeService.mixCake(flour, butter, eggs).bind()
        cakeService.bakeCake(mixture).bind()
    }


    fun deferredCake(): Kind<F, Cake> = binding {
        val flour = cakeService.retrieveFlour(100).bind()
        val butter = cakeService.retrieveButter(50).bind()
        val eggs = cakeService.retrieveEggs(EggSize.large, 3).bind()
        val mixture = cakeService.mixCake(flour, butter, eggs).bind()
        cakeService.bakeCake(mixture).bind()
    }


    fun parIngredients(flourWeight: Int, butterWeight: Int,
                       eggSize: EggSize, noOfEggs: Int): Kind<F, Tuple3<Flour, Butter, List<Egg>>> =
            tupled(cakeService.retrieveFlour(100),
                    cakeService.retrieveButter(50),
                    cakeService.retrieveEggs(EggSize.large, 10))


    fun parCake(): Kind<F, Cake>  = binding {
        val (flour, butter, eggs) = parIngredients(100, 50, EggSize.large, 100).bind()
        val mixture = cakeService.mixCake(flour, butter, eggs).bind()
        cakeService.bakeCake(mixture).bind()
    }


    fun resilientCake(): Kind<F, Cake> =
            deferredBadCake().handleErrorWith { deferredCake() }


}


class CakeModule<F>(a: Async<F>, s: Sleep<F>) {
    val cakeService = AsyncCakeService(a, s)
    val cakeAttempts = AsyncCakeAttempts(a, cakeService)

}

class DeferredKSleep(val scope: CoroutineScope = GlobalScope) : Sleep<ForDeferredK> {
    override fun sleep(milis: Long): Kind<ForDeferredK, Unit> =
            scope.asyncK { delay(milis) }

}

object ObservableKSleep: Sleep<ForObservableK> {
    override fun sleep(milis: Long): Kind<ForObservableK, Unit> =
        Observable.just(Unit).delay(milis, TimeUnit.MILLISECONDS).k()
}

object FluxKSleep: Sleep<ForFluxK> {
    override fun sleep(milis: Long): Kind<ForFluxK, Unit> =
            Flux.just(Unit).delayElements(Duration.ofMillis(milis)).k()
}


fun <T> runTiming(name: String, run: () -> Try<T>) {
    val start = System.currentTimeMillis()
    println("----------- $name -----------------")
    println(run())
    val timetaken = System.currentTimeMillis() - start
    println("----------- timetaken $timetaken -----------------")
}

fun <T> runDeferred(df: Kind<ForDeferredK, T>): Try<T> = df.fix().unsafeAttemptSync()
fun <T> runObsK(obs: Kind<ForObservableK, T>): Try<T> = Try { obs.fix().value().blockingFirst() }
fun <T> runFlux(flux: Kind<ForFluxK, T>): Try<T> = Try { flux.fix().value().blockFirst() }

