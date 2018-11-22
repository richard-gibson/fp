package async

import arrow.core.Tuple3
import arrow.data.k
import arrow.effects.*
import arrow.effects.deferredk.monad.binding

import arrow.effects.DeferredK.Companion.raiseError
import arrow.effects.deferredk.applicative.applicative
import arrow.effects.deferredk.monad.monad
import async.DeferredKCakeService.bakeCake
import async.DeferredKCakeService.mixCake
import async.DeferredKCakeService.retrieveBadButter
import async.DeferredKCakeService.retrieveButter
import async.DeferredKCakeService.retrieveEggs
import async.DeferredKCakeService.retrieveFlour
import kotlinx.coroutines.*


object DeferredKCakeService {

    fun sleep(milis: Long, scope: CoroutineScope = GlobalScope): DeferredK<Unit> =
            scope.asyncK {
                delay(milis)
            }


    fun retrieveFlour(weight: Int): DeferredK<Flour> =
            DeferredK {
                println("going to get some flour")
                sleep(500)
                println("flour retrieved")
                Flour(weight)
            }

    fun retrieveButter(weight: Int): DeferredK<Butter> =
            DeferredK {
                println("going to get some butter")
                sleep(400)
                println("butter retrieved")
                Butter(weight)
            }

    fun retrieveBadButter(weight: Int): DeferredK<Butter> =
            raiseError(BadButterException("found some bad butter"))

    fun retrieveEgg(size: EggSize): DeferredK<Egg> =
            DeferredK {
                println("going to a $size egg")
                sleep(300)
                println("egg retrieved")
                Egg(size)
            }

    fun retrieveEggs(eggSize: EggSize, noOfEggs: Int): DeferredK<List<Egg>> =
            (1..noOfEggs).toList().k().traverse(DeferredK.applicative()) { retrieveEgg(eggSize)}.fix()

    fun mixCake(flour: Flour, butter: Butter, eggs: List<Egg>): DeferredK<Mixture> =
            DeferredK {
                println("going to mix ingredients")
                sleep(400)
                println("cake baked")
                Mixture(flour, butter, eggs)
            }

    fun bakeCake(mixture: Mixture): DeferredK<Cake> =
            DeferredK {
                println("going to bake a cake")
                val (flour, butter, eggs) = mixture
                sleep(400)
                println("cake baked")
                Cake(flour, butter, eggs)
            }
}




object DeferredKCakeAttempts {

    fun <T> runTiming(name: String, df: DeferredK<T>) {
        val start = System.currentTimeMillis()
        println("----------- $name -----------------")
        println(df.unsafeAttemptSync())
        val timetaken = System.currentTimeMillis() - start
        println("----------- timetaken $timetaken -----------------")
    }


    fun deferredBadCake(): DeferredK<Cake> = binding {
        val flour = retrieveFlour(100).bind()
        val butter = retrieveBadButter(50).bind()
        val eggs = retrieveEggs(EggSize.large, 3).bind()
        val mixture = mixCake(flour, butter, eggs).bind()
        bakeCake(mixture).bind()
    }


    fun deferredCake(): DeferredK<Cake>  = binding {
        val flour = retrieveFlour(100).bind()
        val butter = retrieveButter(50).bind()
        val eggs = retrieveEggs(EggSize.large, 3).bind()
        val mixture = mixCake(flour, butter, eggs).bind()
        bakeCake(mixture).bind()
    }


    fun parIngredients(flourWeight: Int, butterWeight: Int,
                       eggSize: EggSize, noOfEggs: Int): DeferredK<Tuple3<Flour, Butter, List<Egg>>> =
            DeferredK.monad().tupled(retrieveFlour(100),
                    retrieveButter(50),
                    retrieveEggs(EggSize.large, 10)).fix()


    fun parCake(): DeferredK<Cake>  = binding {
        val (flour, butter, eggs) = parIngredients(100, 50, EggSize.large, 100).bind()
        val mixture = mixCake(flour, butter, eggs).bind()
        bakeCake(mixture).bind()
    }


    fun resilientCake(): DeferredK<Cake> =
            deferredBadCake().handleErrorWith { deferredCake() }


    @JvmStatic
    fun main(args: Array<String>) {

/*        runTiming("deferredCake", deferredCake())
        runTiming("deferredBadCake", deferredBadCake())
        runTiming("resilientCake", resilientCake())*/
        runTiming("parCake", parCake())


    }

}


