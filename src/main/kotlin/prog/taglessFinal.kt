package prog.tagless

import arrow.Kind
import arrow.effects.typeclasses.MonadDefer
import arrow.typeclasses.Monad
import arrow.typeclasses.binding
import java.util.*


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

object tagless {
    fun <F> MonadConsoleRandom<F>.commandLoop() = binding {

    }
}