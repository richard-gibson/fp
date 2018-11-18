package async

import arrow.core.Tuple3
import async.DeferredCakeService.bakeCake
import async.DeferredCakeService.defer
import async.DeferredCakeService.mixCake
import async.DeferredCakeService.retrieveBadButter
import async.DeferredCakeService.retrieveButter
import async.DeferredCakeService.retrieveEggs
import async.DeferredCakeService.retrieveFlour
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


object DeferredCakeService {

    fun <A> defer(scope: CoroutineScope = GlobalScope,
                  ctx: CoroutineContext = Dispatchers.Default,
                  start: CoroutineStart = CoroutineStart.LAZY, f: suspend () -> A) : Deferred<A> =
            scope.async(ctx, start) { f() }

    fun retrieveFlour(weight: Int, start: CoroutineStart = CoroutineStart.LAZY): Deferred<Flour> =
            defer(start = start){
                println("going to get some flour")
                delay(500)
                println("flour retrieved")
                Flour(weight)
            }

    fun retrieveButter(weight: Int, start: CoroutineStart = CoroutineStart.LAZY): Deferred<Butter> =
            defer(start = start){
                println("going to get some butter")
                delay(400)
                println("butter retrieved")
                Butter(weight)
            }

    fun retrieveBadButter(weight: Int, start: CoroutineStart = CoroutineStart.LAZY): Deferred<Butter> =
            CompletableDeferred<Butter>().apply { completeExceptionally(BadButterException("found some bad butter")) }

    fun retrieveEgg(size: EggSize, start: CoroutineStart = CoroutineStart.LAZY): Deferred<Egg> =
            defer(start = start){
                println("going to a $size egg")
                delay(300)
                println("egg retrieved")
                Egg(size)
            }

    fun retrieveEggs(eggSize: EggSize, noOfEggs: Int, start: CoroutineStart = CoroutineStart.LAZY): Deferred<List<Egg>> =
            defer(start = start) { (1..noOfEggs).map { retrieveEgg(eggSize, start = start) }.awaitAll() }

    fun mixCake(flour: Flour, butter: Butter, eggs: List<Egg>): Deferred<Mixture> =
            defer {
                println("going to mix ingredients")
                delay(400)
                println("cake baked")
                Mixture(flour, butter, eggs)
            }

    fun bakeCake(mixture: Mixture): Deferred<Cake> =
            defer {
                println("going to bake a cake")
                val (flour, butter, eggs) = mixture
                delay(400)
                println("cake baked")
                Cake(flour, butter, eggs)
            }
}




object DeferredCakeAttempts {

    fun <T> blockTiming(name: String, f: suspend () -> T) {
        runBlocking {
            val start = System.currentTimeMillis()
            println("----------- $name -----------------")
            println(f())
            val timetaken = System.currentTimeMillis() - start
            println("----------- timetaken $timetaken -----------------")

        }
    }


    fun deferredBadCake(): Deferred<Cake> = defer {
        val flour = retrieveFlour(100).await()
        val butter = retrieveBadButter(50).await()
        val eggs = retrieveEggs(EggSize.large, 3).await()
        val mixture = mixCake(flour, butter, eggs).await()
        bakeCake(mixture).await()
    }


    fun deferredCake(): Deferred<Cake>  = defer {
        val flour = retrieveFlour(100).await()
        val butter = retrieveButter(50).await()
        val eggs = retrieveEggs(EggSize.large, 3).await()
        val mixture = mixCake(flour, butter, eggs).await()
        bakeCake(mixture).await()
    }


    fun parIngredients(flourWeight: Int, butterWeight: Int,
                       eggSize: EggSize, noOfEggs: Int): Deferred<Tuple3<Flour, Butter, List<Egg>>> =
            defer {
                //CoroutineStart.DEFAULT set to force evaluate on initialization rather than await()
                val coroutineStart = CoroutineStart.DEFAULT
                val defferredFlour = retrieveFlour(flourWeight, coroutineStart)
                val defferredButter = retrieveButter(butterWeight, coroutineStart)
                val deferredEggs = retrieveEggs(eggSize, noOfEggs, coroutineStart)
                Tuple3(defferredFlour.await(), defferredButter.await(), deferredEggs.await())
            }


    fun parCake(): Deferred<Cake>  = defer {
        val (flour, butter, eggs) = parIngredients(100, 50, EggSize.large, 10).await()
        val mixture = mixCake(flour, butter, eggs).await()
        bakeCake(mixture).await()
    }

    suspend fun resilientCake(): Cake  = try {
         deferredBadCake().await()
    } catch (e :Exception) {
        println("fallback cake")
         deferredCake().await()
    }


    @JvmStatic
    fun main(args: Array<String>) {

    blockTiming("deferredCake") { deferredCake().await() }
//    blockTiming("deferredBadCake") { deferredBadCake().await() }
    blockTiming("resilientCake") { resilientCake() }
    blockTiming("parCake") { parCake().await() }


    }

}


