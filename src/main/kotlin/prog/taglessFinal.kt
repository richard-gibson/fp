package prog.tagless

import arrow.Kind
import arrow.core.Try
import arrow.effects.IO
import arrow.effects.fix
import arrow.effects.instances.io.monadDefer.monadDefer
import arrow.effects.typeclasses.MonadDefer
import arrow.typeclasses.Monad
import java.util.*
import prog.tagless.tagless.fMain


sealed class Command {
    object Add: Command()
    object Get: Command()
    object Random: Command()
    object Print: Command()
    object Remove: Command()
    object Quit: Command()
    object Unknown: Command()
    companion object {
        operator fun invoke(cmd: String) = when(cmd.trim().toLowerCase()) {
            "add"    -> Add
            "get"    -> Get
            "random" -> Random
            "print"  -> Print
            "quit"   -> Quit
            "remove" -> Remove
            else     -> Unknown
        }
    }
}

object ORandom : Random()

interface Console<F> {
    fun putStrLn(s: String): Kind<F, Unit>
    fun getStrLn(): Kind<F, String>
}

class ConsoleInstance<F>(val delay: MonadDefer<F>) : Console<F> {
    override fun putStrLn(s: String): Kind<F, Unit> = delay { println(s) }
    override fun getStrLn(): Kind<F, String> = delay { readLine().orEmpty() }
}

interface FRandom<F> {
    fun nextInt(upper: Int): Kind<F, Int>
}

class FRandomInstance<F>(val delay: MonadDefer<F>) : FRandom<F> {
    override fun nextInt(upper: Int): Kind<F, Int> = delay { ORandom.nextInt(upper) }
}

class MonadConsoleRandom<F>(M: Monad<F>, C: Console<F>, R: FRandom<F>) : Monad<F> by M, Console<F> by C, FRandom<F> by R
/***
 * TODO
 *  - Create TestIO
 *  - fix parseInt error scenario
 *  - move when branches to separate functions
 *  - try to make when return Kind<F, List>
 *  - move state??
 */
object tagless {

    fun parseInt(s: String): Try<Int> = Try { s.toInt() }

    fun <F> MonadConsoleRandom<F>.printMenu(): Kind<F, Unit> = binding {
        putStrLn("Choose an option:").bind()
        putStrLn("\t [Add] a string to the list").bind()
        putStrLn("\t [Get] a string at a specified position").bind()
        putStrLn("\t [Random] a string at a Random position").bind()
        putStrLn("\t [Remove] a string from a specified position").bind()
        putStrLn("\t [Print] the current contents of the list").bind()
        putStrLn("\t [Quit] the program").bind()
        putStrLn("").bind()
    }

    fun <F> MonadConsoleRandom<F>.commandLoop(state: List<String>): Kind<F, Unit> = binding {
        printMenu().bind()
        val choice = getStrLn().bind()
        when(Command(choice)) {
            Command.Add -> {
                putStrLn("Enter a string to Add").bind()
                val item = getStrLn().bind()
                commandLoop(state + item)
            }
            Command.Get -> {
                putStrLn("Enter an index").bind()
                val index = getStrLn().bind()
                parseInt(index).fold({ putStrLn("$index is not a number")}){
                    putStrLn("item at $index is ${state[it]}")
                }.bind()
                commandLoop(state)
            }
            Command.Random -> {
                val rndIdx = nextInt(state.size).bind()
                putStrLn("Random item at $rndIdx is ${state[rndIdx]}").bind()
                commandLoop(state)
            }
            Command.Print -> {
                putStrLn("Contents of list are: ${state.map { "\n\t $it" }}").bind()
                commandLoop(state)
            }
            Command.Remove -> {
                putStrLn("Enter an index").bind()
                val index = getStrLn().bind()
                val idx = parseInt(index)
                        .fold({ just(-1) }) { just(it) }.bind()

                val bfrIdx = state.filterIndexed { i, _ -> i < idx }
                val aftIdx = state.filterIndexed { i, _ -> i > idx }

                putStrLn("Item ${state[idx]} removed from position $idx").bind()
                commandLoop(bfrIdx + aftIdx)
            }
            Command.Quit -> {
                putStrLn("Thanks for playing!")
                just(Unit)
            }
            Command.Unknown -> {
                putStrLn("Unknown command").bind()
                commandLoop(state)
            }

        }.bind()
    }

    fun <F> MonadConsoleRandom<F>.fMain(): Kind<F, Unit> = binding {
        commandLoop(emptyList()).bind()
    }
}

fun main(args: Array<String>) {
    val effectModule = IO.monadDefer()
    val mcr = MonadConsoleRandom(effectModule,
            ConsoleInstance(effectModule),
            FRandomInstance(effectModule))
    val prog = mcr.fMain().fix()
    prog.unsafeRunSync()
}